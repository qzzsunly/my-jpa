/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression.function;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models the ANSI SQL <tt>LOWER</tt> function.
 *
 * @author Steve Ebersole
 */
public class LowerFunction extends ParameterizedFunctionExpression<String> implements Serializable {
    public static final String NAME = "lower";

    public LowerFunction(CriteriaBuilderImpl criteriaBuilder, Expression<String> string) {
        super(criteriaBuilder, String.class, NAME, string);
    }

    @Override
    protected boolean isStandardJpaFunction() {
        return true;
    }
}
