/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.hive;

import com.google.common.collect.ImmutableMap;
import io.trino.operator.RetryPolicy;
import io.trino.plugin.hive.containers.HiveHadoop;
import io.trino.plugin.hive.containers.HiveMinioDataLake;
import io.trino.plugin.hive.s3.S3HiveQueryRunner;
import io.trino.testing.QueryRunner;
import io.trino.tpch.TpchTable;
import org.testng.annotations.AfterClass;

import java.util.List;
import java.util.Map;

import static io.trino.testing.sql.TestTable.randomTableSuffix;

public class TestHiveMinioQueryFailureRecoveryTest
        extends BaseHiveFailureRecoveryTest
{
    public TestHiveMinioQueryFailureRecoveryTest()
    {
        super(RetryPolicy.QUERY);
    }

    private String bucketName;
    private HiveMinioDataLake dockerizedS3DataLake;

    @Override
    protected QueryRunner createQueryRunner(List<TpchTable<?>> requiredTpchTables, Map<String, String> configProperties, Map<String, String> coordinatorProperties)
            throws Exception
    {
        this.bucketName = "test-hive-insert-overwrite-" + randomTableSuffix(); // randomizing bucket name to ensure cached TrinoS3FileSystem objects are not reused
        this.dockerizedS3DataLake = new HiveMinioDataLake(bucketName, ImmutableMap.of(), HiveHadoop.DEFAULT_IMAGE);
        this.dockerizedS3DataLake.start();

        return S3HiveQueryRunner.builder(dockerizedS3DataLake)
                .setInitialTables(requiredTpchTables)
                .setExtraProperties(configProperties)
                .setCoordinatorProperties(coordinatorProperties)
                .setHiveProperties(ImmutableMap.<String, String>builder()
                        // Streaming upload allocates non trivial amount of memory for buffering (16MB per output file by default).
                        // When streaming upload is enabled insert into a table with high number of buckets / partitions may cause
                        // the tests to run out of memory as the buffer space is eagerly allocated for each output file.
                        .put("hive.s3.streaming.enabled", "false")
                        .build())
                .build();
    }

    @AfterClass(alwaysRun = true)
    public void destroy()
            throws Exception
    {
        if (dockerizedS3DataLake != null) {
            dockerizedS3DataLake.close();
            dockerizedS3DataLake = null;
        }
    }
}
