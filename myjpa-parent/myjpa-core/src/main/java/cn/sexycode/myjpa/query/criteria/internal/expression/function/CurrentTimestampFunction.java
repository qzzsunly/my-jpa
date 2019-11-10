/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression.function;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Models the ANSI SQL <tt>CURRENT_TIMESTAMP</tt> function.
 *
 * @author Steve Ebersole
 */
public class CurrentTimestampFunction extends BasicFunctionExpression<Timestamp> implements Serializable {
    public static final String NAME = "current_timestamp";

    public CurrentTimestampFunction(CriteriaBuilderImpl criteriaBuilder) {
        super(criteriaBuilder, Timestamp.class, NAME);
    }
}
