package cn.sexycode.myjpa.metamodel.internal;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import java.io.Serializable;
import java.lang.reflect.Member;

/**
 * @author qinzaizhen
 */
public class SingularAttributeImpl<X, Y>
        extends AbstractAttribute<X, Y>
        implements SingularAttribute<X, Y>, Serializable {
    private final boolean isIdentifier;
    private final boolean isVersion;
    private final boolean isOptional;
    private final Type<Y> attributeType;

    public SingularAttributeImpl(
            String name,
            Class<Y> javaType,
            AbstractManagedType<X> declaringType,
            Member member,
            boolean isIdentifier,
            boolean isVersion,
            boolean isOptional,
            Type<Y> attributeType,
            PersistentAttributeType persistentAttributeType) {
        super(name, javaType, declaringType, member, persistentAttributeType);
        this.isIdentifier = isIdentifier;
        this.isVersion = isVersion;
        this.isOptional = isOptional;
        this.attributeType = attributeType;
    }

    /**
     * Subclass used to simply instantiation of singular attributes representing an entity's
     * identifier.
     */
    public static class Identifier<X, Y> extends SingularAttributeImpl<X, Y> {
        public Identifier(
                String name,
                Class<Y> javaType,
                AbstractManagedType<X> declaringType,
                Member member,
                Type<Y> attributeType,
                PersistentAttributeType persistentAttributeType) {
            super(name, javaType, declaringType, member, true, false, false, attributeType, persistentAttributeType);
        }
    }

    /**
     * Subclass used to simply instantiation of singular attributes representing an entity's
     * version.
     */
    public static class Version<X, Y> extends SingularAttributeImpl<X, Y> {
        public Version(
                String name,
                Class<Y> javaType,
                AbstractManagedType<X> declaringType,
                Member member,
                Type<Y> attributeType,
                PersistentAttributeType persistentAttributeType) {
            super(name, javaType, declaringType, member, false, true, false, attributeType, persistentAttributeType);
        }
    }

    @Override
    public boolean isId() {
        return isIdentifier;
    }

    @Override
    public boolean isVersion() {
        return isVersion;
    }

    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public Type<Y> getType() {
        return attributeType;
    }

    @Override
    public boolean isAssociation() {
        return getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE
                || getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public BindableType getBindableType() {
        return BindableType.SINGULAR_ATTRIBUTE;
    }

    @Override
    public Class<Y> getBindableJavaType() {
        return attributeType.getJavaType();
    }
}
