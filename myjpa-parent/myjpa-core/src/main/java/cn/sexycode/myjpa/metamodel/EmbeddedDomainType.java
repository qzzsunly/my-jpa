/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel;

import javax.persistence.metamodel.EmbeddableType;

/**
 * Hibernate extension to the JPA {@link } contract.
 *
 * @apiNote Notice that this describes {@link javax.persistence.Embedded}, not
 * {@link javax.persistence.Embeddable} - like {@link CollectionDomainType}, it includes
 * mapping information per usage (attribute)
 *
 * @author Steve Ebersole
 */
public interface EmbeddedDomainType<J> extends SimpleDomainType<J>, EmbeddableType<J> {
}
