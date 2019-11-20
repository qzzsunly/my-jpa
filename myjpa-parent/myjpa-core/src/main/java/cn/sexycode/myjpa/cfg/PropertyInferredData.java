package cn.sexycode.myjpa.cfg;

import cn.sexycode.myjpa.binding.MappingException;
import cn.sexycode.util.core.cls.ReflectionManager;
import cn.sexycode.util.core.cls.XClass;
import cn.sexycode.util.core.cls.XProperty;

/**
 * Retrieve all inferred data from an annnoted element
 *
 * @author Emmanuel Bernard
 * @author Paolo Perrotta
 */
public class PropertyInferredData implements PropertyData {
    private final AccessType defaultAccess;

    private final XProperty property;

    private final ReflectionManager reflectionManager;

    private final XClass declaringClass;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PropertyInferredData");
        sb.append("{property=").append(property);
        sb.append(", declaringClass=").append(declaringClass);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Take the annoted element for lazy process
     */
    public PropertyInferredData(XClass declaringClass, XProperty property, String propertyAccessor,
            ReflectionManager reflectionManager) {
        this.declaringClass = declaringClass;
        this.property = property;
        this.defaultAccess = AccessType.getAccessStrategy(propertyAccessor);
        this.reflectionManager = reflectionManager;
    }

    @Override
    public AccessType getDefaultAccess() throws MappingException {
        AccessType accessType = defaultAccess;

        AccessType hibernateAccessType = AccessType.DEFAULT;
        AccessType jpaAccessType = AccessType.DEFAULT;

		/*org.hibernate.annotations.AccessType accessTypeAnnotation = property.getAnnotation( org.hibernate.annotations.AccessType.class );
		if ( accessTypeAnnotation != null ) {
			hibernateAccessType = AccessType.getAccessStrategy( accessTypeAnnotation.value() );
		}*/

		/*Access access = property.getAnnotation( Access.class );
		if ( access != null ) {
			jpaAccessType = AccessType.getAccessStrategy( access.value() );
		}

		if ( hibernateAccessType != AccessType.DEFAULT
				&& jpaAccessType != AccessType.DEFAULT
				&& hibernateAccessType != jpaAccessType ) {

			StringBuilder builder = new StringBuilder();
			builder.append( property.toString() );
			builder.append(
					" defines @AccessType and @Access with contradicting values. Use of @Access only is recommended."
			);
			throw new MappingException( builder.toString() );
		}

		if ( hibernateAccessType != AccessType.DEFAULT ) {
			accessType = hibernateAccessType;
		}
		else if ( jpaAccessType != AccessType.DEFAULT ) {
			accessType = jpaAccessType;
		}*/
        return accessType;
    }

    @Override
    public String getPropertyName() throws MappingException {
        return property.getName();
    }

    @Override
    public XClass getPropertyClass() throws MappingException {
		/*if ( property.isAnnotationPresent( Target.class ) ) {
			return reflectionManager.toXClass( property.getAnnotation( Target.class ).value() );
		}
		else {*/
        return property.getType();
        //		}
    }

    @Override
    public XClass getClassOrElement() throws MappingException {
		/*if ( property.isAnnotationPresent( Target.class ) ) {
			return reflectionManager.toXClass( property.getAnnotation( Target.class ).value() );
		}
		else {*/
        return property.getClassOrElementClass();
        //		}
    }

    @Override
    public String getClassOrElementName() throws MappingException {
        return getClassOrElement().getName();
    }

    @Override
    public String getTypeName() throws MappingException {
        return getPropertyClass().getName();
    }

    @Override
    public XProperty getProperty() {
        return property;
    }

    @Override
    public XClass getDeclaringClass() {
        return declaringClass;
    }
}
