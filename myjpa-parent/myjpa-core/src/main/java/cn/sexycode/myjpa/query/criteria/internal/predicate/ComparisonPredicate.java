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
import cn.sexycode.myjpa.query.criteria.internal.ValueHandlerFactory;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;
import cn.sexycode.myjpa.query.criteria.internal.expression.BinaryOperatorExpression;
import cn.sexycode.myjpa.query.criteria.internal.expression.LiteralExpression;

import javax.persistence.criteria.Expression;
import java.io.Serializable;

/**
 * Models a basic relational comparison predicate.
 *
 * @author Steve Ebersole
 */
public class ComparisonPredicate extends AbstractSimplePredicate
        implements BinaryOperatorExpression<Boolean>, Serializable {
    private final ComparisonOperator comparisonOperator;

    private final Expression<?> leftHandSide;

    private final Expression<?> rightHandSide;

    public ComparisonPredicate(CriteriaBuilderImpl criteriaBuilder, ComparisonOperator comparisonOperator,
            Expression<?> leftHandSide, Expression<?> rightHandSide) {
        super(criteriaBuilder);
        this.comparisonOperator = comparisonOperator;
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    @SuppressWarnings({ "unchecked" })
    public ComparisonPredicate(CriteriaBuilderImpl criteriaBuilder, ComparisonOperator comparisonOperator,
            Expression<?> leftHandSide, Object rightHandSide) {
        super(criteriaBuilder);
        this.comparisonOperator = comparisonOperator;
        this.leftHandSide = leftHandSide;
        if (ValueHandlerFactory.isNumeric(leftHandSide.getJavaType())) {
            this.rightHandSide = new LiteralExpression(criteriaBuilder,
                    ValueHandlerFactory.convert(rightHandSide, (Class<Number>) leftHandSide.getJavaType()));
        } else {
            this.rightHandSide = new LiteralExpression(criteriaBuilder, rightHandSide);
        }
    }

    @SuppressWarnings({ "unchecked" })
    public <N extends Number> ComparisonPredicate(CriteriaBuilderImpl criteriaBuilder,
            ComparisonOperator comparisonOperator, Expression<N> leftHandSide, Number rightHandSide) {
        super(criteriaBuilder);
        this.comparisonOperator = comparisonOperator;
        this.leftHandSide = leftHandSide;
        Class type = leftHandSide.getJavaType();
        if (Number.class.equals(type)) {
            this.rightHandSide = new LiteralExpression(criteriaBuilder, rightHandSide);
        } else {
            N converted = (N) ValueHandlerFactory.convert(rightHandSide, type);
            this.rightHandSide = new LiteralExpression<N>(criteriaBuilder, converted);
        }
    }

    public ComparisonOperator getComparisonOperator() {
        return getComparisonOperator(isNegated());
    }

    public ComparisonOperator getComparisonOperator(boolean isNegated) {
        return isNegated ? comparisonOperator.negated() : comparisonOperator;
    }

    @Override
    public Expression getLeftHandOperand() {
        return leftHandSide;
    }

    @Override
    public Expression getRightHandOperand() {
        return rightHandSide;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        Helper.possibleParameter(getLeftHandOperand(), registry);
        Helper.possibleParameter(getRightHandOperand(), registry);
    }

    /**
     * Defines the comparison operators.  We could also get away with
     * only 3 and use negation...
     */
    public static enum ComparisonOperator {
        EQUAL {
            public ComparisonOperator negated() {
                return NOT_EQUAL;
            }

            public String rendered() {
                return "=";
            }
        }, NOT_EQUAL {
            public ComparisonOperator negated() {
                return EQUAL;
            }

            public String rendered() {
                return "<>";
            }
        }, LESS_THAN {
            public ComparisonOperator negated() {
                return GREATER_THAN_OR_EQUAL;
            }

            public String rendered() {
                return "<";
            }
        }, LESS_THAN_OR_EQUAL {
            public ComparisonOperator negated() {
                return GREATER_THAN;
            }

            public String rendered() {
                return "<=";
            }
        }, GREATER_THAN {
            public ComparisonOperator negated() {
                return LESS_THAN_OR_EQUAL;
            }

            public String rendered() {
                return ">";
            }
        }, GREATER_THAN_OR_EQUAL {
            public ComparisonOperator negated() {
                return LESS_THAN;
            }

            public String rendered() {
                return ">=";
            }
        };

        public abstract ComparisonOperator negated();

        public abstract String rendered();
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return ((Renderable) getLeftHandOperand()).render(renderingContext) + getComparisonOperator(isNegated)
                .rendered() + ((Renderable) getRightHandOperand()).render(renderingContext);
    }
}
