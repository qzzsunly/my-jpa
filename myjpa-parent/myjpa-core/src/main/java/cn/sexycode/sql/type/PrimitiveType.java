/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type;

import java.io.Serializable;

/**
 * Additional contract for primitive / primitive wrapper types.
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public interface PrimitiveType<T> extends LiteralType<T> {
    /**
     * Retrieve the primitive counterpart to the wrapper type identified by
     * {@link Type#getReturnedClass()}.
     *
     * @return The primitive Java type.
     */
    Class getPrimitiveClass();

    /**
     * Retrieve the string representation of the given value.
     *
     * @param value The value to be stringified.
     * @return The string representation
     */
    String toString(T value);

    /**
     * Get this type's default value.
     *
     * @return The default value.
     */
    Serializable getDefaultValue();
}
