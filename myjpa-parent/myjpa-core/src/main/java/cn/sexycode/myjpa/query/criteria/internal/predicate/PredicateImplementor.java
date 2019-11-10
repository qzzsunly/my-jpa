/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.predicate;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.Renderable;
import cn.sexycode.myjpa.query.criteria.internal.compile.RenderingContext;

import javax.persistence.criteria.Predicate;

/**
 * @author Steve Ebersole
 */
public interface PredicateImplementor extends Predicate, Renderable {
    /**
     * Access to the CriteriaBuilder
     *
     * @return The CriteriaBuilder
     */
    public CriteriaBuilderImpl criteriaBuilder();

    /**
     * Is this a conjunction or disjunction?
     *
     * @return {@code true} if this predicate is a junction (AND/OR); {@code false} otherwise
     */
    public boolean isJunction();

    /**
     * Form of {@link Renderable#render} used when the predicate is wrapped in a negated wrapper.  Allows passing
     * down the negation flag.
     * <p/>
     * Note that this form is no-op in compound (junction) predicates.  The reason being that compound predicates
     * are more complex and the negation is applied during its creation.
     *
     * @param isNegated        Should the predicate be negated.
     * @param renderingContext The context for rendering
     * @return The rendered predicate fragment.
     */
    public String render(boolean isNegated, RenderingContext renderingContext);
}
