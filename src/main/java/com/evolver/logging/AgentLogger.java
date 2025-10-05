package com.evolver.logging;

import java.util.Map;

/**
 * AGENT LOGGING INTERFACE
 *
 * Defines how agents generate summaries and logs of their interactions
 * with the framework. Different implementations can provide different
 * logging styles and destinations.
 */
public interface AgentLogger {

    /**
     * Log a general interaction with the framework
     * @param agentId Unique identifier for the agent
     * @param action The action being performed (ASK, INITIALIZING, etc.)
     * @param summary Brief summary of what happened
     * @param metadata Additional details about the interaction
     */
    void logInteraction(String agentId, String action, String summary, Map<String, Object> metadata);

    /**
     * Log a decision made by the agent
     * @param agentId Unique identifier for the agent
     * @param decision The decision that was made
     * @param reasoning Why this decision was made
     */
    void logDecision(String agentId, String decision, String reasoning);

    /**
     * Log when an agent evolves or changes
     * @param agentId Unique identifier for the agent
     * @param evolution Description of what evolved
     * @param changes The specific changes that occurred
     */
    void logEvolution(String agentId, String evolution, Map<String, Object> changes);

    /**
     * Log learning events
     * @param agentId Unique identifier for the agent
     * @param concept What was learned
     * @param confidence How confident the agent is in this learning
     */
    void logLearning(String agentId, String concept, double confidence);

    /**
     * Log conflicts or issues
     * @param agentId Unique identifier for the agent
     * @param issue Description of the issue
     * @param resolution How it was resolved (or null if unresolved)
     */
    void logConflict(String agentId, String issue, String resolution);
}