package com.evolver.context;

/**
 * Interface for custom filter rules
 */
@FunctionalInterface
public interface FilterRule {
    /**
     * Test if a fragment should be included
     */
    boolean test(ContextFragment fragment, ContextRequest request);
}
