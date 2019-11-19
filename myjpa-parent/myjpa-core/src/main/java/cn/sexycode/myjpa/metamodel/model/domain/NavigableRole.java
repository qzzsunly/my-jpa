package cn.sexycode.myjpa.metamodel.model.domain;

import cn.sexycode.util.core.str.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * A representation of the static "Navigable" path relative to some "root entity".
 *
 * @author Steve Ebersole
 */
public class NavigableRole implements Serializable {
    public static final String IDENTIFIER_MAPPER_PROPERTY = "_identifierMapper";

    private final NavigableRole parent;

    private final String navigableName;

    private final String fullPath;

    public NavigableRole(NavigableRole parent, String navigableName) {
        this.parent = parent;
        this.navigableName = navigableName;

        // the _identifierMapper is a "hidden" property on entities with composite keys.
        // concatenating it will prevent the path from correctly being used to look up
        // various things such as criteria paths and fetch profile association paths
        if (IDENTIFIER_MAPPER_PROPERTY.equals(navigableName)) {
            this.fullPath = parent != null ? parent.getFullPath() : "";
        } else {
            final String prefix;
            if (parent != null) {
                final String resolvedParent = parent.getFullPath();
                if (StringUtils.isEmpty(resolvedParent)) {
                    prefix = "";
                } else {
                    prefix = resolvedParent + '.';
                }
            } else {
                prefix = "";
            }

            this.fullPath = prefix + navigableName;
        }
    }

    public NavigableRole(String navigableName) {
        this(null, navigableName);
    }

    public NavigableRole() {
        this("");
    }

    public NavigableRole append(String property) {
        return new NavigableRole(this, property);
    }

    public NavigableRole getParent() {
        return parent;
    }

    public String getNavigableName() {
        return navigableName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isRoot() {
        return parent == null && StringUtils.isEmpty(navigableName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + fullPath + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NavigableRole that = (NavigableRole) o;
        return Objects.equals(getFullPath(), that.getFullPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullPath());
    }
}
