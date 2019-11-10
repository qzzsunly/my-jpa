/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.predicate;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterRegistry;
import cn.sexycode.myjpa.query.criteria.internal.Renderable;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;
import cn.sexycode.myjpa.query.criteria.internal.expression.UnaryOperatorExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Defines a {@link javax.persistence.criteria.Predicate} for checking the
 * nullness state of an expression, aka an <tt>IS [NOT] NULL</tt> predicate.
 * <p/>
 * The <tt>NOT NULL</tt> form can be built by calling the constructor and then
 * calling {@link #not}.
 *
 * @author Steve Ebersole
 */
public class NullnessPredicate extends AbstractSimplePredicate
        implements UnaryOperatorExpression<Boolean>, Serializable {
    private final Expression<?> operand;

    /**
     * Constructs the affirmitive form of nullness checking (<i>IS NULL</i>).  To
     * construct the negative form (<i>IS NOT NULL</i>) call {@link #not} on the
     * constructed instance.
     *
     * @param criteriaBuilder The query builder from whcih this originates.
     * @param operand         The expression to check.
     */
    public NullnessPredicate(CriteriaBuilderImpl criteriaBuilder, Expression<?> operand) {
        super(criteriaBuilder);
        this.operand = operand;
    }

    @Override
    public Expression<?> getOperand() {
        return operand;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(getOperand(), registry);
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return ((Renderable) operand).render(renderingContext) + check(isNegated);
    }

    private String check(boolean negated) {
        return negated ? " is not null" : " is null";
    }
}
