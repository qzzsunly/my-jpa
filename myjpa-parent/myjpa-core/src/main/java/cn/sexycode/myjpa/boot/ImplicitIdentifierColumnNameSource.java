package cn.sexycode.myjpa.boot;

/**
 * Context for determining the implicit name of an entity's identifier
 * column.
 *
 * @author Steve Ebersole
 */
public interface ImplicitIdentifierColumnNameSource extends ImplicitNameSource {
    /**
     * Access the entity name information
     *
     * @return The entity name information
     */
    EntityNaming getEntityNaming();

    /**
     * Access to the AttributePath for the entity's identifier attribute.
     *
     * @return The AttributePath for the entity's identifier attribute.
     */
    AttributePath getIdentifierAttributePath();
}
