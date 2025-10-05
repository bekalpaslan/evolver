package com.evolver.agent.identity;

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
     * Agent can evolve its characteristics based on experience
     */
    public AgentCharacteristic evolve(String reason, Map<String, Object> newTraits) {
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
    // GETTERS
    // ===============================================================
    
    public String getName() { return name; }
    public String getPersonality() { return personality; }
    public Map<String, Object> getTraits() { return new HashMap<>(traits); }
    public Map<String, Object> getEvolutionHistory() { return new HashMap<>(evolutionHistory); }
    
    @Override
    public String toString() {
        return name + ": " + personality;
    }
    
    public static void main(String[] args) {
        System.out.println("🧬 Agent Evolution Demo");
        System.out.println("======================");

        // Start with a basic agent
        AgentCharacteristic original = AgentCharacteristic.DOCUMENTATION_OBSESSIVE;
        System.out.println("Original: " + original);

        // Agent evolves based on experience
        AgentCharacteristic evolved = original.evolve("Learned that performance matters too",
            Map.of("focusAreas", Arrays.asList("documentation", "performance", "optimization")));

        System.out.println("Evolved: " + evolved);

        // Create a hybrid agent
        AgentCharacteristic hybrid = AgentCharacteristic.crossBreed(
            AgentCharacteristic.CLEAN_CODE_FREAK,
            AgentCharacteristic.PERFORMANCE_MANIAC,
            "CleanSpeed"
        );

        System.out.println("Hybrid: " + hybrid);

        // Show how evolution can lead to unexpected behaviors
        System.out.println("\nEvolution can lead to emergent behaviors:");
        System.out.println("- Original focus: " + original.getFocusAreas());
        System.out.println("- Evolved focus: " + evolved.getFocusAreas());
        System.out.println("- Hybrid focus: " + hybrid.getFocusAreas());
    }
}