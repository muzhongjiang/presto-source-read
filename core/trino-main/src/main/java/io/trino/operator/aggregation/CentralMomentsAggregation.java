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
package io.trino.operator.aggregation;

import io.trino.operator.aggregation.state.CentralMomentsState;
import io.trino.spi.block.BlockBuilder;
import io.trino.spi.function.AggregationFunction;
import io.trino.spi.function.AggregationState;
import io.trino.spi.function.CombineFunction;
import io.trino.spi.function.Description;
import io.trino.spi.function.InputFunction;
import io.trino.spi.function.OutputFunction;
import io.trino.spi.function.SqlType;
import io.trino.spi.type.StandardTypes;

import static io.trino.spi.type.DoubleType.DOUBLE;

@AggregationFunction
@Description("Returns the central moments of the argument as an array")
public final class CentralMomentsAggregation
{
    private CentralMomentsAggregation() {}

    @InputFunction
    public static void doubleInput(@AggregationState CentralMomentsState state, @SqlType(StandardTypes.DOUBLE) double value)
    {
        state.update(value);
    }

    @InputFunction
    public static void bigintInput(@AggregationState CentralMomentsState state, @SqlType(StandardTypes.BIGINT) long value)
    {
        state.update((double) value);
    }

    @CombineFunction
    public static void combine(@AggregationState CentralMomentsState state, @AggregationState CentralMomentsState otherState)
    {
        state.merge(otherState);
    }

    @AggregationFunction("skewness")
    @Description("Returns the skewness of the argument")
    @OutputFunction(StandardTypes.DOUBLE)
    public static void skewness(@AggregationState CentralMomentsState state, BlockBuilder out)
    {
        long n = state.getCount();

        if (n < 3) {
            out.appendNull();
        }
        else {
            double result = Math.sqrt(n) * state.getM3() / Math.pow(state.getM2(), 1.5);
            DOUBLE.writeDouble(out, result);
        }
    }

    @AggregationFunction("kurtosis")
    @Description("Returns the (excess) kurtosis of the argument")
    @OutputFunction(StandardTypes.DOUBLE)
    public static void kurtosis(@AggregationState CentralMomentsState state, BlockBuilder out)
    {
        double n = state.getCount();

        if (n < 4) {
            out.appendNull();
        }
        else {
            double m2 = state.getM2();
            double m4 = state.getM4();
            double result = ((n - 1) * n * (n + 1)) / ((n - 2) * (n - 3)) * m4 / (m2 * m2) - 3 * ((n - 1) * (n - 1)) / ((n - 2) * (n - 3));
            DOUBLE.writeDouble(out, result);
        }
    }
}
