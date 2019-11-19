/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package cn.sexycode.myjpa.metamodel.model.domain.spi;

import org.hibernate.metamodel.model.domain.MappedSuperclassDomainType;

import javax.persistence.metamodel.MappedSuperclassType;

/**
 * Hibernate extension to the JPA {@link MappedSuperclassType} descriptor
 *
 * @author Steve Ebersole
 */
public interface MappedSuperclassTypeDescriptor<J>
        extends MappedSuperclassDomainType<J>, IdentifiableTypeDescriptor<J> {
}
