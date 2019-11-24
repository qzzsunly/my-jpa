package cn.sexycode.myjpa.boot;

/**
 * Context for determining the implicit name of an entity's primary table
 *
 * @author Steve Ebersole
 */
public interface ImplicitEntityNameSource extends ImplicitNameSource {
    /**
     * Access to the entity's name information
     *
     * @return The entity's name information
     */
    EntityNaming getEntityNaming();
}
