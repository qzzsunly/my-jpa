/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.mybatis.jpa.metamodel.internal;


import javax.persistence.metamodel.EntityType;
import java.io.Serializable;

/**
 * Defines the Hibernate implementation of the JPA {@link EntityType} contract.
 *
 * @author Steve Ebersole
 * @author Emmanuel Bernard
 */
public class EntityTypeImpl<X>  extends AbstractIdentifiableType<X> implements EntityType<X>, Serializable {
	private final String jpaEntityName;

	@SuppressWarnings("unchecked")
	public EntityTypeImpl(Class javaType, AbstractIdentifiableType<? super X> superType) {
		super(
				javaType,
				javaType.getCanonicalName(),
				superType,
				false,
				true,
				false
		);/*super(
				javaType,
				persistentClass.getEntityName(),
				superType,
				persistentClass.getDeclaredIdentifierMapper() != null || ( superType != null && superType.hasIdClass() ),
				persistentClass.hasIdentifierProperty(),
				persistentClass.isVersioned()
		);*/
		this.jpaEntityName = "";
//		this.jpaEntityName = persistentClass.getJpaEntityName();
	}

	@Override
	public String getName() {
		return jpaEntityName;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.ENTITY_TYPE;
	}

	@Override
	public Class<X> getBindableJavaType() {
		return getJavaType();
	}

	@Override
	public PersistenceType getPersistenceType() {
		return PersistenceType.ENTITY;
	}
}
