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
package io.trino.plugin.clickhouse;

import io.trino.plugin.jdbc.BaseJdbcConnectorSmokeTest;
import io.trino.testing.TestingConnectorBehavior;
import org.testng.annotations.Test;

public abstract class BaseClickHouseConnectorSmokeTest
        extends BaseJdbcConnectorSmokeTest
{
    @Override
    protected boolean hasBehavior(TestingConnectorBehavior connectorBehavior)
    {
        switch (connectorBehavior) {
            case SUPPORTS_DELETE:
                return false;
            default:
                return super.hasBehavior(connectorBehavior);
        }
    }

    // TODO (https://github.com/trinodb/trino/issues/10653) Disable until fixing the flaky test issue
    @Test(enabled = false)
    @Override
    public void testRenameSchema()
    {
        super.testRenameSchema();
    }
}
