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
package io.trino.server.testing.exchange;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.trino.spi.QueryId;
import io.trino.spi.exchange.ExchangeId;
import io.trino.spi.exchange.ExchangeSinkHandle;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Objects.requireNonNull;

public class LocalFileSystemExchangeSinkHandle
        implements ExchangeSinkHandle
{
    private final QueryId queryId;
    private final ExchangeId exchangeId;
    private final int taskPartitionId;

    @JsonCreator
    public LocalFileSystemExchangeSinkHandle(
            @JsonProperty("queryId") QueryId queryId,
            @JsonProperty("exchangeId") ExchangeId exchangeId,
            @JsonProperty("taskPartitionId") int taskPartitionId)
    {
        this.queryId = requireNonNull(queryId, "queryId is null");
        this.exchangeId = requireNonNull(exchangeId, "exchangeId is null");
        this.taskPartitionId = taskPartitionId;
    }

    @JsonProperty
    public QueryId getQueryId()
    {
        return queryId;
    }

    @JsonProperty
    public ExchangeId getExchangeId()
    {
        return exchangeId;
    }

    @JsonProperty
    public int getTaskPartitionId()
    {
        return taskPartitionId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalFileSystemExchangeSinkHandle that = (LocalFileSystemExchangeSinkHandle) o;
        return taskPartitionId == that.taskPartitionId && Objects.equals(queryId, that.queryId) && Objects.equals(exchangeId, that.exchangeId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(queryId, exchangeId, taskPartitionId);
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("queryId", queryId)
                .add("exchangeId", exchangeId)
                .add("taskPartitionId", taskPartitionId)
                .toString();
    }
}
