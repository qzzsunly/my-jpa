 package cn.sexycode.myjpa.metamodel.internal;

import cn.sexycode.myjpa.metamodel.PluralPersistentAttribute;
import cn.sexycode.myjpa.metamodel.SimpleTypeDescriptor;

import java.io.Serializable;
import java.util.Collection;


/**
 * @param <D> The (D)eclaring type
 * @param <C> The {@link Collection} type
 * @param <E> The type of the Collection's elements
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public abstract class AbstractPluralAttribute<D, C, E>
		extends AbstractAttribute<D,C>
		implements PluralPersistentAttribute<D,C,E>, Serializable {

	private final Class<C> collectionClass;

	protected AbstractPluralAttribute(PluralAttributeBuilder<D,C,E,?> builder) {
		super(
				builder.getDeclaringType(),
				builder.getProperty().getName(),
				builder.getAttributeNature(),
				builder.getValueType(),
				builder.getMember()
		);

		this.collectionClass = builder.getCollectionClass();
	}

	public static <X,C,E,K> PluralAttributeBuilder<X,C,E,K> create(
			AbstractManagedType<X> ownerType,
			SimpleTypeDescriptor<E> attrType,
			Class<C> collectionClass,
			SimpleTypeDescriptor<K> keyType) {
		return new PluralAttributeBuilder<>( ownerType, attrType, collectionClass, keyType );
	}

	@Override
	public SimpleTypeDescriptor<E> getElementType() {
		return getValueGraphType();
	}

	@Override
	@SuppressWarnings("unchecked")
	public SimpleTypeDescriptor<E> getValueGraphType() {
		return (SimpleTypeDescriptor<E>) super.getValueGraphType();
	}

	@Override
	public SimpleTypeDescriptor<?> getKeyGraphType() {
		return null;
	}

	@Override
	public boolean isAssociation() {
		return getPersistentAttributeType() == PersistentAttributeType.ONE_TO_MANY
				|| getPersistentAttributeType() == PersistentAttributeType.MANY_TO_MANY;
	}

	@Override
	public boolean isCollection() {
		return true;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.PLURAL_ATTRIBUTE;
	}

	@Override
	public Class<E> getBindableJavaType() {
		return getElementType().getJavaType();
	}


	@Override
	public Class<C> getJavaType() {
		return collectionClass;
	}

}
