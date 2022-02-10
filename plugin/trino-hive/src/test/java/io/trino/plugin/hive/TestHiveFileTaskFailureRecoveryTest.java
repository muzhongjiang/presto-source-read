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
import io.trino.testing.QueryRunner;
import io.trino.tpch.TpchTable;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestHiveFileTaskFailureRecoveryTest
        extends BaseHiveFailureRecoveryTest
{
    protected TestHiveFileTaskFailureRecoveryTest()
    {
        super(RetryPolicy.TASK);
    }

    @Override
    protected QueryRunner createQueryRunner(List<TpchTable<?>> requiredTpchTables, Map<String, String> configProperties, Map<String, String> coordinatorProperties)
            throws Exception
    {
        return HiveQueryRunner.builder()
                .setInitialTables(requiredTpchTables)
                .setCoordinatorProperties(coordinatorProperties)
                .setExtraProperties(ImmutableMap.<String, String>builder()
                        .putAll(configProperties)
                        // currently not supported for fault tolerant execution mode
                        .put("enable-dynamic-filtering", "false")
                        .build())
                .build();
    }

    @Override
    public void testJoinDynamicFilteringEnabled()
    {
        assertThatThrownBy(super::testJoinDynamicFilteringEnabled)
                .hasMessageContaining("Dynamic filtering is not supported with automatic task retries enabled");
    }
}
