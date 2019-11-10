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

import javax.persistence.criteria.Subquery;
import java.io.Serializable;

/**
 * Models an <tt>EXISTS(<subquery>)</tt> predicate
 *
 * @author Steve Ebersole
 */
public class ExistsPredicate extends AbstractSimplePredicate implements Serializable {
    private final Subquery<?> subquery;

    public ExistsPredicate(CriteriaBuilderImpl criteriaBuilder, Subquery<?> subquery) {
        super(criteriaBuilder);
        this.subquery = subquery;
    }

    public Subquery<?> getSubquery() {
        return subquery;
    }

    @Override
    public void registerParameters(ParameterRegistry registry) {
        // nothing to do here
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        return (isNegated ? "not " : "") + "exists " + ((Renderable) getSubquery()).render(renderingContext);
    }
}
