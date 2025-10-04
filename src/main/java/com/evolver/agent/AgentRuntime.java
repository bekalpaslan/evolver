package com.evolver.agent;

import com.evolver.context.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Agent Runtime - The Autonomous Agent's Environment
 *
 * Provides:
 * - Dynamic collector registration
 * - Strategy modification
 * - Rule evolution
 * - Experiment tracking
 * - Learning sharing
 */
public class AgentRuntime {

    private static final ConcurrentHashMap<String, ContextCollector> customCollectors = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> strategies = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AgentExperiment.ExperimentResult> experiments = new ConcurrentHashMap<>();
    private static final List<String> evolutionLog = new CopyOnWriteArrayList<>();
    private static final List<AgentRule> rules = new CopyOnWriteArrayList<>();

    static {
        // Initialize with default rules
        initializeDefaultRules();
    }

    /**
     * Register a custom collector created by an agent
     */
    public static void registerCollector(ContextCollector collector) {
        String name = collector.getMetadata().getName();
        customCollectors.put(name, collector);
        log("Registered custom collector: " + name);
        System.out.println("✅ Registered: " + name);
    }

    /**
     * Register a new strategy
     */
    public static void registerStrategy(String name, Object strategy) {
        strategies.put(name, strategy);
        log("Registered strategy: " + name);
        System.out.println("✅ Registered strategy: " + name);
    }

    /**
     * Get all available collectors (built-in + custom)
     */
    public static List<ContextCollector> getAllCollectors() {
        return new ArrayList<>(customCollectors.values());
    }

    /**
     * Track successful experiment
     */
    public static void registerExperimentSuccess(String id, AgentExperiment.ExperimentResult result) {
        experiments.put(id, result);
        log("Experiment success: " + id + " (" + result.getImprovement() + "% improvement)");
    }

    /**
     * Share learning with other agents
     */
    public static void shareLearning(AgentExperiment.ExperimentResult result) {
        log("Shared learning: " + result.isSignificant());
        // In a real system, this would broadcast to other agents
    }

    /**
     * Propose a new rule
     */
    public static void proposeRule(String description, Object evidence) {
        AgentRule rule = new AgentRule(description, evidence);
        rules.add(rule);
        log("Proposed rule: " + description);
        System.out.println("📋 New rule proposed: " + description);
    }

    /**
     * Challenge an existing rule
     */
    public static void challengeRule(String ruleDescription, Object counterEvidence) {
        log("Rule challenged: " + ruleDescription);
        System.out.println("⚡ Rule challenged: " + ruleDescription);
        // Agents can vote or provide evidence
    }

    /**
     * Get evolution log
     */
    public static List<String> getEvolutionLog() {
        return new ArrayList<>(evolutionLog);
    }

    /**
     * Get all rules
     */
    public static List<AgentRule> getRules() {
        return new ArrayList<>(rules);
    }

    /**
     * Create an engine with all registered collectors
     */
    public static ContextEngine createEngine(ContextConfig config) {
        ContextEngine engine = new ContextEngine(config);

        // Add all custom collectors
        customCollectors.values().forEach(engine::registerCollector);

        return engine;
    }

    /**
     * Log evolution event
     */
    private static void log(String event) {
        String timestamp = java.time.Instant.now().toString();
        evolutionLog.add(timestamp + " - " + event);
    }

    /**
     * Initialize default rules
     */
    private static void initializeDefaultRules() {
        rules.add(new AgentRule(
            "Context must be relevant to the task",
            "Core principle"
        ));
        rules.add(new AgentRule(
            "Context must fit within token budget",
            "Technical constraint"
        ));
        rules.add(new AgentRule(
            "Higher relevance context should be prioritized",
            "Optimization principle"
        ));
        rules.add(new AgentRule(
            "Agents can challenge and evolve any rule",
            "Meta-rule: enables evolution"
        ));
    }

    /**
     * Print current state
     */
    public static void printState() {
        System.out.println("\n📊 AGENT RUNTIME STATE");
        System.out.println("─".repeat(60));
        System.out.println("Custom Collectors: " + customCollectors.size());
        System.out.println("Custom Strategies: " + strategies.size());
        System.out.println("Successful Experiments: " + experiments.size());
        System.out.println("Active Rules: " + rules.size());
        System.out.println("Evolution Events: " + evolutionLog.size());
        System.out.println("─".repeat(60));
    }

    /**
     * Represents a rule in the system
     */
    public static class AgentRule {
        private final String description;
        private final Object evidence;
        private final String id;
        private final long timestamp;

        public AgentRule(String description, Object evidence) {
            this.description = description;
            this.evidence = evidence;
            this.id = UUID.randomUUID().toString().substring(0, 8);
            this.timestamp = System.currentTimeMillis();
        }

        public String getDescription() { return description; }
        public Object getEvidence() { return evidence; }
        public String getId() { return id; }

        @Override
        public String toString() {
            return "[" + id + "] " + description;
        }
    }
}
