/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package cn.sexycode.sql.type.descriptor.sql;

import java.sql.Types;

/**
 * Descriptor for {@link Types#BINARY BINARY} handling.
 *
 * @author Steve Ebersole
 */
public class BinaryTypeDescriptor extends VarbinaryTypeDescriptor {
    public static final BinaryTypeDescriptor INSTANCE = new BinaryTypeDescriptor();

    public BinaryTypeDescriptor() {
    }

    @Override
    public int getSqlType() {
        return Types.BINARY;
    }
}
