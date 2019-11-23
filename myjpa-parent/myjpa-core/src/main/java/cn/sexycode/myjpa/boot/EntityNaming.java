/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.myjpa.boot;

/**
 * @author Steve Ebersole
 */
public interface EntityNaming {
    /**
     * Retrieve the fully-qualified entity class name.  Note that for
     * dynamic entities, this may return (what???).
     * <p>
     * todo : what should this return for dynamic entities?  null?  The entity name?
     *
     * @return The entity class name.
     */
    String getClassName();

    /**
     * The Hibernate entity name.  This might be either:<ul>
     * <li>The explicitly specified entity name, if one</li>
     * <li>The unqualified entity class name if no entity name was explicitly specified</li>
     * </ul>
     *
     * @return The Hibernate entity name
     */
    String getEntityName();

    /**
     * The JPA-specific entity name.  See {@link javax.persistence.Entity#name()} for details.
     *
     * @return The JPA entity name, if one was specified.  May return {@code null} if one
     * was not explicitly specified.
     */
    String getJpaEntityName();
}
