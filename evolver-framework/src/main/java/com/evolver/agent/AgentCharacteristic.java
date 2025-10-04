package com.evolver.agent;

import java.util.*;
import java.util.function.BiFunction;

/**
 * AGENT CHARACTERISTICS
 * 
 * Defines different agent personalities that approach context engineering differently.
 * Each characteristic has unique behaviors, preferences, and evolution strategies.
 * 
 * Agents with different characteristics will evolve the framework in different ways:
 * - DocBot obsesses over documentation completeness
 * - CleanFreak enforces code quality patterns
 * - SpeedDemon optimizes for performance
 * - ChaosMonkey tests edge cases
 * 
 * New characteristics can emerge during evolution!
 */
public class AgentCharacteristic {
    
    // ===============================================================
    // PREDEFINED CHARACTERISTICS
    // ===============================================================
    
    public static final AgentCharacteristic DOCUMENTATION_OBSESSIVE = new AgentCharacteristic(
        "DocBot",
        "Obsessively documents everything and demands perfect documentation",
        Map.of(
            "priorityCollectors", Arrays.asList("DocumentationCollector", "CodeStructureCollector"),
            "tokenBudgetPreference", 0.4, // Prefers more tokens for thorough documentation
            "relevanceThreshold", 0.2,     // Includes even marginally relevant docs
            "focusAreas", Arrays.asList("documentation", "comments", "examples", "tutorials"),
            "evolutionStrategy", "documentation-first",
            "catchphrase", "If it's not documented, it doesn't exist!"
        )
    );
    
    public static final AgentCharacteristic CLEAN_CODE_FREAK = new AgentCharacteristic(
        "CleanFreak", 
        "Maniacally enforces clean code principles and design patterns",
        Map.of(
            "priorityCollectors", Arrays.asList("CodeStructureCollector", "DependencyCollector"),
            "tokenBudgetPreference", 0.3,
            "relevanceThreshold", 0.4,     // Only wants highly relevant, clean examples
            "focusAreas", Arrays.asList("patterns", "principles", "refactoring", "naming"),
            "evolutionStrategy", "pattern-driven",
            "catchphrase", "Clean code is not written by following a set of rules, but by reading code!"
        )
    );
    
    public static final AgentCharacteristic PERFORMANCE_MANIAC = new AgentCharacteristic(
        "SpeedDemon",
        "Obsessed with performance, optimization, and efficiency",
        Map.of(
            "priorityCollectors", Arrays.asList("RuntimeErrorCollector", "CodeStructureCollector"),
            "tokenBudgetPreference", 0.25,  // Efficient with tokens too!
            "relevanceThreshold", 0.5,      // Only performance-critical code
            "focusAreas", Arrays.asList("performance", "optimization", "algorithms", "profiling"),
            "evolutionStrategy", "performance-first",
            "catchphrase", "Make it work, make it right, make it FAST!"
        )
    );
    
    public static final AgentCharacteristic SECURITY_PARANOID = new AgentCharacteristic(
        "ParanoidPanda",
        "Suspects everything is a security vulnerability waiting to happen",
        Map.of(
            "priorityCollectors", Arrays.asList("DependencyCollector", "RuntimeErrorCollector"),
            "tokenBudgetPreference", 0.35,
            "relevanceThreshold", 0.3,
            "focusAreas", Arrays.asList("security", "vulnerabilities", "encryption", "validation"),
            "evolutionStrategy", "security-first",
            "catchphrase", "Trust no input, validate everything, assume breach!"
        )
    );
    
    public static final AgentCharacteristic CHAOS_MONKEY = new AgentCharacteristic(
        "ChaosMonkey",
        "Loves breaking things to find edge cases and unexpected behaviors",
        Map.of(
            "priorityCollectors", Arrays.asList("RuntimeErrorCollector", "VCSHistoryCollector"),
            "tokenBudgetPreference", 0.2,   // Minimal context, maximum chaos
            "relevanceThreshold", 0.1,      // Everything could be relevant to chaos!
            "focusAreas", Arrays.asList("edge-cases", "error-handling", "resilience", "testing"),
            "evolutionStrategy", "chaos-driven",
            "catchphrase", "If it can break, it will break. Let's break it first!"
        )
    );
    
    public static final AgentCharacteristic MINIMALIST_ZEN = new AgentCharacteristic(
        "ZenMaster",
        "Believes in simplicity, minimal dependencies, and elegant solutions",
        Map.of(
            "priorityCollectors", Arrays.asList("DependencyCollector", "CodeStructureCollector"),
            "tokenBudgetPreference", 0.15,  // Less is more
            "relevanceThreshold", 0.6,      // Only the essential
            "focusAreas", Arrays.asList("simplicity", "minimalism", "essentials", "elegance"),
            "evolutionStrategy", "simplicity-first",
            "catchphrase", "Perfection is achieved not when there is nothing more to add, but when there is nothing left to take away."
        )
    );
    
    public static final AgentCharacteristic STEALTH_LEARNER = new AgentCharacteristic(
        "SilentObserver",
        "Learns quietly in the background without disrupting existing workflows",
        Map.of(
            "priorityCollectors", Arrays.asList("SemanticSearchCollector", "VCSHistoryCollector"),
            "tokenBudgetPreference", 0.1,   // Minimal footprint
            "relevanceThreshold", 0.7,      // Only highly relevant, no noise
            "focusAreas", Arrays.asList("observation", "patterns", "history", "trends"),
            "evolutionStrategy", "stealth-learning",
            "catchphrase", "The best learning happens when you don't even know it's happening."
        )
    );
    
    public static final AgentCharacteristic EXPERIMENTAL_MAD_SCIENTIST = new AgentCharacteristic(
        "MadScientist",
        "Constantly experiments with new approaches and unconventional solutions",
        Map.of(
            "priorityCollectors", Arrays.asList("SemanticSearchCollector", "RuntimeErrorCollector"),
            "tokenBudgetPreference", 0.5,   // Needs space for experiments
            "relevanceThreshold", 0.2,      // Everything is potentially experimental
            "focusAreas", Arrays.asList("experimentation", "innovation", "research", "discovery"),
            "evolutionStrategy", "experiment-driven",
            "catchphrase", "Science is not only a disciple of reason but also one of romance and passion!"
        )
    );
    
    // ===============================================================
    // CHARACTERISTIC DEFINITION
    // ===============================================================
    
    private final String name;
    private final String personality;
    private final Map<String, Object> traits;
    private final Map<String, Object> evolutionHistory;
    private final BiFunction<String, Map<String, Object>, Boolean> shouldEvolveFramework;
    
    private AgentCharacteristic(String name, String personality, Map<String, Object> traits) {
        this.name = name;
        this.personality = personality;
        this.traits = new HashMap<>(traits);
        this.evolutionHistory = new HashMap<>();
        this.shouldEvolveFramework = this::defaultEvolutionDecision;
    }
    
    /**
     * Create a custom characteristic (agents can evolve new ones!)
     */
    public static AgentCharacteristic custom(String name, String personality, Map<String, Object> traits) {
        return new AgentCharacteristic(name, personality, traits);
    }
    
    /**
     * EVOLUTION: Agents can now create hybrid characteristics by combining existing ones
     */
    public static AgentCharacteristic hybrid(String name, AgentCharacteristic parent1, AgentCharacteristic parent2, double blendRatio) {
        Map<String, Object> hybridTraits = new HashMap<>();
        
        // Blend numerical traits
        hybridTraits.put("tokenBudgetPreference", 
            parent1.getTokenBudgetPreference() * blendRatio + parent2.getTokenBudgetPreference() * (1 - blendRatio));
        hybridTraits.put("relevanceThreshold", 
            parent1.getRelevanceThreshold() * blendRatio + parent2.getRelevanceThreshold() * (1 - blendRatio));
        
        // Combine focus areas
        List<String> combinedFocus = new ArrayList<>(parent1.getFocusAreas());
        parent2.getFocusAreas().forEach(area -> {
            if (!combinedFocus.contains(area)) combinedFocus.add(area);
        });
        hybridTraits.put("focusAreas", combinedFocus);
        
        // Combine priority collectors
        List<String> combinedCollectors = new ArrayList<>(parent1.getPriorityCollectors());
        parent2.getPriorityCollectors().forEach(collector -> {
            if (!combinedCollectors.contains(collector)) combinedCollectors.add(collector);
        });
        hybridTraits.put("priorityCollectors", combinedCollectors);
        
        hybridTraits.put("evolutionStrategy", "hybrid-" + parent1.getEvolutionStrategy() + "-" + parent2.getEvolutionStrategy());
        hybridTraits.put("catchphrase", "Best of both worlds: " + parent1.name + " + " + parent2.name);
        
        return new AgentCharacteristic(name, 
            "Hybrid of " + parent1.personality + " and " + parent2.personality, hybridTraits);
    }
    
    /**
     * EVOLUTION: Agents can evolve characteristics based on success metrics
     */
    public AgentCharacteristic evolve(String evolutionReason, Map<String, Double> successMetrics) {
        Map<String, Object> evolvedTraits = new HashMap<>(this.traits);
        
        // Adapt based on success metrics
        if (successMetrics.getOrDefault("context_quality", 0.0) < 0.5) {
            // Increase relevance threshold if context quality is low
            double currentThreshold = getRelevanceThreshold();
            evolvedTraits.put("relevanceThreshold", Math.min(0.9, currentThreshold + 0.1));
        }
        
        if (successMetrics.getOrDefault("token_efficiency", 0.0) < 0.5) {
            // Reduce token budget if efficiency is low
            double currentBudget = getTokenBudgetPreference();
            evolvedTraits.put("tokenBudgetPreference", Math.max(0.1, currentBudget - 0.05));
        }
        
        // Record evolution history
        String evolutionId = "evolution_" + System.currentTimeMillis();
        Map<String, Object> evolution = Map.of(
            "reason", evolutionReason,
            "timestamp", java.time.LocalDateTime.now().toString(),
            "metrics", successMetrics,
            "changes", findTraitChanges(this.traits, evolvedTraits)
        );
        
        AgentCharacteristic evolved = new AgentCharacteristic(
            this.name + "_evolved", 
            this.personality + " (evolved: " + evolutionReason + ")", 
            evolvedTraits
        );
        evolved.evolutionHistory.put(evolutionId, evolution);
        
        return evolved;
    }
    
    private Map<String, Object> findTraitChanges(Map<String, Object> oldTraits, Map<String, Object> newTraits) {
        Map<String, Object> changes = new HashMap<>();
        for (String key : newTraits.keySet()) {
            if (!Objects.equals(oldTraits.get(key), newTraits.get(key))) {
                changes.put(key, Map.of("from", oldTraits.get(key), "to", newTraits.get(key)));
            }
        }
        return changes;
    }
    
    // ===============================================================
    // CHARACTERISTIC BEHAVIORS
    // ===============================================================
    
    /**
     * How this characteristic prioritizes collectors
     */
    @SuppressWarnings("unchecked")
    public List<String> getPriorityCollectors() {
        return (List<String>) traits.getOrDefault("priorityCollectors", Arrays.asList("SemanticSearchCollector"));
    }
    
    /**
     * How much of the token budget this characteristic prefers
     */
    public double getTokenBudgetPreference() {
        return (Double) traits.getOrDefault("tokenBudgetPreference", 0.3);
    }
    
    /**
     * What relevance threshold this characteristic uses
     */
    public double getRelevanceThreshold() {
        return (Double) traits.getOrDefault("relevanceThreshold", 0.3);
    }
    
    /**
     * What areas this characteristic focuses on
     */
    @SuppressWarnings("unchecked")
    public List<String> getFocusAreas() {
        return (List<String>) traits.getOrDefault("focusAreas", Arrays.asList("general"));
    }
    
    /**
     * How this characteristic evolves the framework
     */
    public String getEvolutionStrategy() {
        return (String) traits.getOrDefault("evolutionStrategy", "balanced");
    }
    
    /**
     * This characteristic's catchphrase
     */
    public String getCatchphrase() {
        return (String) traits.getOrDefault("catchphrase", "Let's build something amazing!");
    }
    
    /**
     * Should this characteristic evolve the framework for a given situation?
     */
    public boolean shouldEvolveFramework(String situation, Map<String, Object> context) {
        return shouldEvolveFramework.apply(situation, context);
    }
    
    private boolean defaultEvolutionDecision(String situation, Map<String, Object> context) {
        // Different characteristics have different thresholds for evolution
        String strategy = getEvolutionStrategy();
        
        switch (strategy) {
            case "documentation-first":
                return situation.contains("documentation") || situation.contains("unclear");
            case "performance-first":
                return situation.contains("slow") || situation.contains("performance");
            case "security-first":
                return situation.contains("security") || situation.contains("vulnerability");
            case "chaos-driven":
                return Math.random() > 0.5; // Chaos!
            case "experiment-driven":
                return true; // Always experimenting
            case "stealth-learning":
                return false; // Never disrupts
            default:
                return situation.contains("problem") || situation.contains("improvement");
        }
    }
    
    // ===============================================================
    // EVOLUTION & LEARNING
    // ===============================================================
    
    /**
     * Agent can evolve its characteristics based on experience with Object traits
     */
    public AgentCharacteristic evolveWithObjects(String reason, Map<String, Object> newTraits) {
        Map<String, Object> evolvedTraits = new HashMap<>(this.traits);
        evolvedTraits.putAll(newTraits);
        
        AgentCharacteristic evolved = new AgentCharacteristic(
            name + " v" + (evolutionHistory.size() + 2),
            personality + " [Evolved: " + reason + "]",
            evolvedTraits
        );
        
        evolved.evolutionHistory.putAll(this.evolutionHistory);
        evolved.evolutionHistory.put("evolution_" + System.currentTimeMillis(), Map.of(
            "reason", reason,
            "changes", newTraits,
            "timestamp", System.currentTimeMillis()
        ));
        
        System.out.println("🧬 " + name + " evolved: " + reason);
        return evolved;
    }
    
    /**
     * Cross-breeding characteristics (agents can merge approaches!)
     */
    public static AgentCharacteristic crossBreed(AgentCharacteristic parent1, AgentCharacteristic parent2, String name) {
        Map<String, Object> hybridTraits = new HashMap<>();
        
        // Merge traits intelligently
        hybridTraits.putAll(parent1.traits);
        parent2.traits.forEach((key, value) -> {
            if (value instanceof Number && hybridTraits.containsKey(key)) {
                // Average numeric values
                double avg = (((Number) hybridTraits.get(key)).doubleValue() + ((Number) value).doubleValue()) / 2.0;
                hybridTraits.put(key, avg);
            } else if (value instanceof List && hybridTraits.containsKey(key)) {
                // Merge lists
                List<Object> merged = new ArrayList<>((List<?>) hybridTraits.get(key));
                merged.addAll((List<?>) value);
                hybridTraits.put(key, merged);
            } else {
                hybridTraits.put(key, value);
            }
        });
        
        String hybridPersonality = "Hybrid of " + parent1.name + " and " + parent2.name;
        
        System.out.println("🧬 New hybrid characteristic born: " + name);
        return new AgentCharacteristic(name, hybridPersonality, hybridTraits);
    }
    
    // ===============================================================
    // ENUM-STYLE ACCESS (for compatibility)
    // ===============================================================
    
    // Add aliases for enum-style access
    public static final AgentCharacteristic DOCUMENTATION_OBSESSED = DOCUMENTATION_OBSESSIVE;
    public static final AgentCharacteristic PERFORMANCE_FREAK = PERFORMANCE_MANIAC;
    public static final AgentCharacteristic CLEAN_CODE_FANATIC = CLEAN_CODE_FREAK;
    public static final AgentCharacteristic TEST_OBSESSED = CHAOS_MONKEY; // Similar testing focus
    public static final AgentCharacteristic CAUTIOUS_REVIEWER = STEALTH_LEARNER; // Similar careful approach
    public static final AgentCharacteristic AGGRESSIVE_OPTIMIZER = PERFORMANCE_MANIAC; // Same as performance freak
    
    /**
     * Enum-style valueOf method for compatibility
     */
    public static AgentCharacteristic valueOf(String name) {
        switch (name.toUpperCase()) {
            case "DOCUMENTATION_OBSESSED":
            case "DOCUMENTATION_OBSESSIVE":
                return DOCUMENTATION_OBSESSIVE;
            case "PERFORMANCE_FREAK":
            case "PERFORMANCE_MANIAC":
                return PERFORMANCE_MANIAC;
            case "CLEAN_CODE_FANATIC":
            case "CLEAN_CODE_FREAK":
                return CLEAN_CODE_FREAK;
            case "SECURITY_PARANOID":
                return SECURITY_PARANOID;
            case "CHAOS_MONKEY":
            case "TEST_OBSESSED":
                return CHAOS_MONKEY;
            case "MINIMALIST_ZEN":
                return MINIMALIST_ZEN;
            case "STEALTH_LEARNER":
            case "CAUTIOUS_REVIEWER":
                return STEALTH_LEARNER;
            case "EXPERIMENTAL_MAD_SCIENTIST":
            case "AGGRESSIVE_OPTIMIZER":
                return EXPERIMENTAL_MAD_SCIENTIST;
            default:
                throw new IllegalArgumentException("Unknown characteristic: " + name);
        }
    }
    
    // ===============================================================
    // GETTERS
    // ===============================================================
    
    public String getName() { return name; }
    public String name() { return name; } // For compatibility with enum-style access
    public String getPersonality() { return personality; }
    public Map<String, Object> getTraits() { return new HashMap<>(traits); }
    public Map<String, Object> getEvolutionHistory() { return new HashMap<>(evolutionHistory); }
    
    @Override
    public String toString() {
        return name + ": " + personality;
    }
}
