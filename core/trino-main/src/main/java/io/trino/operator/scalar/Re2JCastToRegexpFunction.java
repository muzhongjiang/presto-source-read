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
package io.trino.operator.scalar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.airlift.slice.Slice;
import io.trino.annotation.UsedByGeneratedCode;
import io.trino.metadata.BoundSignature;
import io.trino.metadata.SqlOperator;
import io.trino.spi.TrinoException;
import io.trino.spi.type.Type;
import io.trino.type.Re2JRegexp;

import java.lang.invoke.MethodHandle;

import static io.trino.spi.StandardErrorCode.INVALID_FUNCTION_ARGUMENT;
import static io.trino.spi.function.InvocationConvention.InvocationArgumentConvention.NEVER_NULL;
import static io.trino.spi.function.InvocationConvention.InvocationReturnConvention.FAIL_ON_NULL;
import static io.trino.spi.function.OperatorType.CAST;
import static io.trino.spi.type.Chars.padSpaces;
import static io.trino.sql.analyzer.TypeSignatureTranslator.parseTypeSignature;
import static io.trino.type.Re2JRegexpType.RE2J_REGEXP_SIGNATURE;
import static io.trino.util.Reflection.methodHandle;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.util.Collections.emptyList;

public class Re2JCastToRegexpFunction
        extends SqlOperator
{
    private static final MethodHandle METHOD_HANDLE = methodHandle(Re2JCastToRegexpFunction.class, "castToRegexp", int.class, int.class, boolean.class, long.class, Slice.class);

    private final int dfaStatesLimit;
    private final int dfaRetries;
    private final boolean padSpaces;

    public static SqlOperator castVarcharToRe2JRegexp(int dfaStatesLimit, int dfaRetries)
    {
        return new Re2JCastToRegexpFunction("varchar(x)", dfaStatesLimit, dfaRetries, false);
    }

    public static SqlOperator castCharToRe2JRegexp(int dfaStatesLimit, int dfaRetries)
    {
        return new Re2JCastToRegexpFunction("char(x)", dfaStatesLimit, dfaRetries, true);
    }

    private Re2JCastToRegexpFunction(String sourceType, int dfaStatesLimit, int dfaRetries, boolean padSpaces)
    {
        super(CAST, emptyList(), emptyList(), RE2J_REGEXP_SIGNATURE, ImmutableList.of(parseTypeSignature(sourceType, ImmutableSet.of("x"))), false);
        this.dfaStatesLimit = dfaStatesLimit;
        this.dfaRetries = dfaRetries;
        this.padSpaces = padSpaces;
    }

    @Override
    protected ScalarFunctionImplementation specialize(BoundSignature boundSignature)
    {
        Type inputType = boundSignature.getArgumentType(0);
        Long typeLength = inputType.getTypeSignature().getParameters().get(0).getLongLiteral();
        return new ChoicesScalarFunctionImplementation(
                boundSignature,
                FAIL_ON_NULL,
                ImmutableList.of(NEVER_NULL),
                insertArguments(METHOD_HANDLE, 0, dfaStatesLimit, dfaRetries, padSpaces, typeLength));
    }

    @UsedByGeneratedCode
    public static Re2JRegexp castToRegexp(int dfaStatesLimit, int dfaRetries, boolean padSpaces, long typeLength, Slice pattern)
    {
        try {
            if (padSpaces) {
                pattern = padSpaces(pattern, (int) typeLength);
            }
            return new Re2JRegexp(dfaStatesLimit, dfaRetries, pattern);
        }
        catch (Exception e) {
            throw new TrinoException(INVALID_FUNCTION_ARGUMENT, e);
        }
    }
}
