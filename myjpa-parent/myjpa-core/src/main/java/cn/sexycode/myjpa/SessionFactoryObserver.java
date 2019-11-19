package cn.sexycode.myjpa;

import cn.sexycode.myjpa.session.SessionFactory;

import java.io.Serializable;

/**
 * Allows reaction to basic {@link SessionFactory} occurrences.
 *
 * @author Steve Ebersole
 */
public interface SessionFactoryObserver extends Serializable {
    /**
     * Callback to indicate that the given factory has been created and is now ready for use.
     *
     * @param factory The factory initialized.
     */
    default void sessionFactoryCreated(SessionFactory factory) {
        // nothing to do by default
    }

    /**
     * Callback to indicate that the given factory is about close.  The passed factory reference should be usable
     * since it is only about to close.
     * <p/>
     * NOTE : defined as default to allow for existing SessionFactoryObserver impls to work
     * in 5.2.  Starting in 6.0 the default will be removed and SessionFactoryObserver impls
     * will be required to implement this new method.
     *
     * @param factory The factory about to be closed.
     * @since 5.2
     */
    default void sessionFactoryClosing(SessionFactory factory) {
        // nothing to do by default
    }

    /**
     * Callback to indicate that the given factory has been closed.  Care should be taken
     * in how (if at all) the passed factory reference is used since it is closed.
     *
     * @param factory The factory closed.
     */
    default void sessionFactoryClosed(SessionFactory factory) {
        // nothing to do by default
    }
}
