/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.dialect.function;


import cn.sexycode.sql.dialect.Dialect;

import java.util.Map;
import java.util.TreeMap;

/**
 * Defines a registry for SQLFunction instances
 *
 * @author Steve Ebersole
 */
public class SQLFunctionRegistry {
    private final Map<String, SQLFunction> functionMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Constructs a SQLFunctionRegistry
     *
     * @param dialect         The dialect
     * @param userFunctionMap Any application-supplied function definitions
     */
    public SQLFunctionRegistry(Dialect dialect, Map<String, SQLFunction> userFunctionMap) {
        // Apply the Dialect functions first
        functionMap.putAll(dialect.getFunctions());
        // so that user supplied functions "override" them
        if (userFunctionMap != null) {
            functionMap.putAll(userFunctionMap);
        }
    }

    /**
     * Find a SQLFunction by name
     *
     * @param functionName The name of the function to locate
     * @return The located function, maye return {@code null}
     */
    public SQLFunction findSQLFunction(String functionName) {
        return functionMap.get(functionName);
    }

    /**
     * Does this registry contain the named function
     *
     * @param functionName The name of the function to attempt to locate
     * @return {@code true} if the registry contained that function
     */
    @SuppressWarnings("UnusedDeclaration")
    public boolean hasFunction(String functionName) {
        return functionMap.containsKey(functionName);
    }

}
