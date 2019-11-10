/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.query.criteria.internal.expression;

import cn.sexycode.myjpa.query.criteria.internal.CriteriaBuilderImpl;
import cn.sexycode.myjpa.query.criteria.internal.ParameterContainer;
import cn.sexycode.myjpa.query.criteria.internal.SelectionImplementor;
import cn.sexycode.myjpa.query.criteria.internal.ValueHandlerFactory;

import javax.persistence.criteria.Selection;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The Hibernate implementation of the JPA {@link Selection}
 * contract.
 *
 * @author Steve Ebersole
 */
public abstract class SelectionImpl<X> extends AbstractTupleElement<X>
        implements SelectionImplementor<X>, ParameterContainer, Serializable {
    public SelectionImpl(CriteriaBuilderImpl criteriaBuilder, Class<X> javaType) {
        super(criteriaBuilder, javaType);
    }

    public Selection<X> alias(String alias) {
        setAlias(alias);
        return this;
    }

    public boolean isCompoundSelection() {
        return false;
    }

    public List<ValueHandlerFactory.ValueHandler> getValueHandlers() {
        return getValueHandler() == null
                ? null
                : Collections.singletonList((ValueHandlerFactory.ValueHandler) getValueHandler());
    }

    public List<Selection<?>> getCompoundSelectionItems() {
        throw new IllegalStateException("Not a compound selection");
    }
}
