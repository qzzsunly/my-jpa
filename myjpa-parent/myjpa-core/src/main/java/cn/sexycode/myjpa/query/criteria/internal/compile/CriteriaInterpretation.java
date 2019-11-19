package cn.sexycode.myjpa.query.criteria.internal.compile;

import cn.sexycode.myjpa.session.Session;

import javax.persistence.Query;

/**
 * The interpretation of a JPA criteria object.
 *
 * @author Steve Ebersole
 */
public interface CriteriaInterpretation {
    /**
     * Generate a {@link javax.persistence.Query} instance given the interpreted criteria compiled against the
     * passed EntityManager.
     *
     * @param entityManager                The EntityManager against which to create the Query instance.
     * @param interpretedParameterMetadata parameter metadata
     * @return The created Query instance.
     */
    Query buildCompiledQuery(Session entityManager, InterpretedParameterMetadata interpretedParameterMetadata);
}