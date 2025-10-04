package com.evolver.context;

/**
 * Interface for components that collect context from various sources
 */
public interface ContextCollector {

    /**
     * Check if this collector is applicable for the given request
     */
    boolean isApplicable(ContextRequest request);

    /**
     * Collect context fragment for the request
     */
    ContextFragment collect(ContextRequest request);

    /**
     * Get the priority of this collector (higher = more important)
     */
    default int getPriority() {
        return 50;
    }

    /**
     * Get the estimated cost (in tokens or time) of this collector
     */
    default int getEstimatedCost() {
        return 100;
    }

    /**
     * Get collector metadata
     */
    CollectorMetadata getMetadata();
}
