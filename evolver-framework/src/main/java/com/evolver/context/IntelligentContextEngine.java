package com.evolver.context;

import com.evolver.agent.AgentCharacteristic;
import com.evolver.agent.TechnologyExperience;
import com.evolver.experiences.ExperienceRepository;
import java.util.*;
import java.util.stream.Collectors;

/**
 * INTELLIGENT CONTEXT ENGINE
 * 
 * Advanced context filling that adapts based on:
 * - Agent characteristics and preferences
 * - Framework rules and constraints
 * - Task context and requirements
 * - Previous experience patterns
 * 
 * This system intelligently selects and prioritizes context information
 * to maximize relevance while respecting token budgets and agent behavior patterns.
 */
public class IntelligentContextEngine {
    
    private final ExperienceRepository experienceRepository;
    private final Map<String, ContextCollector> collectors;
    private final FrameworkRulesEngine rulesEngine;
    private final ContextOptimizer optimizer;
    
    // Context quality tracking
    private final Map<String, ContextEffectivenessMetrics> effectivenessHistory;
    
    public IntelligentContextEngine(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
        this.collectors = new HashMap<>();
        this.rulesEngine = new FrameworkRulesEngine();
        this.optimizer = new ContextOptimizer();
        this.effectivenessHistory = new HashMap<>();
        
        // Initialize default collectors
        initializeCollectors();
    }
    
    /**
     * Intelligently fill context based on agent characteristics and task requirements
     */
    public IntelligentContextResult fillContext(IntelligentContextRequest request) {
        // 1. Analyze agent characteristics and preferences
        AgentCharacteristic characteristic = request.getAgentCharacteristic();
        ContextPreferences preferences = extractPreferences(characteristic);
        
        // 2. Apply framework rules and constraints
        ContextConstraints constraints = rulesEngine.generateConstraints(request);
        
        // 3. Identify relevant collectors based on task and agent type
        List<ContextCollector> relevantCollectors = selectRelevantCollectors(
            request, preferences, constraints);
        
        // 4. Prioritize collectors based on agent preferences and historical effectiveness
        List<PrioritizedCollector> prioritizedCollectors = prioritizeCollectors(
            relevantCollectors, preferences, request);
        
        // 5. Collect context within budget constraints
        List<ContextFragment> fragments = collectContextFragments(
            prioritizedCollectors, constraints);
        
        // 6. Optimize and structure the collected context
        StructuredContext structuredContext = optimizer.optimizeContext(
            fragments, preferences, constraints);
        
        // 7. Include agent-specific technology experiences
        Map<String, List<TechnologyExperience>> agentTechExperiences = 
            getAgentSpecificTechnologyExperiences(characteristic, request);
        
        // 8. Record effectiveness for future optimization
        recordContextEffectiveness(request, structuredContext);
        
        return IntelligentContextResult.builder()
            .structuredContext(structuredContext)
            .agentTechnologyExperiences(agentTechExperiences)
            .appliedRules(constraints.getAppliedRules())
            .usedCollectors(prioritizedCollectors.stream()
                .map(pc -> pc.getCollector().getMetadata())
                .collect(Collectors.toList()))
            .effectivenessScore(calculateEffectivenessScore(structuredContext))
            .build();
    }
    
    /**
     * Extract context preferences from agent characteristics
     */
    private ContextPreferences extractPreferences(AgentCharacteristic characteristic) {
        Map<String, Object> attributes = characteristic.getAttributes();
        
        return ContextPreferences.builder()
            .tokenBudgetPreference((Double) attributes.get("tokenBudgetPreference"))
            .relevanceThreshold((Double) attributes.get("relevanceThreshold"))
            .priorityCollectors((List<String>) attributes.get("priorityCollectors"))
            .focusAreas((List<String>) attributes.get("focusAreas"))
            .evolutionStrategy((String) attributes.get("evolutionStrategy"))
            .build();
    }
    
    /**
     * Select collectors relevant to the task and agent type
     */
    private List<ContextCollector> selectRelevantCollectors(
            IntelligentContextRequest request, 
            ContextPreferences preferences,
            ContextConstraints constraints) {
        
        return collectors.values().stream()
            .filter(collector -> isCollectorRelevant(collector, request, preferences))
            .filter(collector -> constraints.isCollectorAllowed(collector))
            .collect(Collectors.toList());
    }
    
    /**
     * Prioritize collectors based on agent preferences and historical effectiveness
     */
    private List<PrioritizedCollector> prioritizeCollectors(
            List<ContextCollector> collectors,
            ContextPreferences preferences,
            IntelligentContextRequest request) {
        
        return collectors.stream()
            .map(collector -> {
                double priority = calculateCollectorPriority(collector, preferences, request);
                return new PrioritizedCollector(collector, priority);
            })
            .sorted((a, b) -> Double.compare(b.getPriority(), a.getPriority()))
            .collect(Collectors.toList());
    }
    
    /**
     * Calculate collector priority based on agent preferences and historical data
     */
    private double calculateCollectorPriority(
            ContextCollector collector,
            ContextPreferences preferences,
            IntelligentContextRequest request) {
        
        double basePriority = collector.getPriority();
        
        // Agent preference bonus
        String collectorName = collector.getMetadata().getName();
        double agentBonus = preferences.getPriorityCollectors().contains(collectorName) ? 20.0 : 0.0;
        
        // Historical effectiveness bonus
        String effectivenessKey = generateEffectivenessKey(collector, request);
        ContextEffectivenessMetrics metrics = effectivenessHistory.get(effectivenessKey);
        double effectivenessBonus = metrics != null ? metrics.getAverageEffectiveness() * 10 : 0.0;
        
        // Focus area alignment bonus
        double focusBonus = calculateFocusAreaAlignment(collector, preferences) * 15.0;
        
        return basePriority + agentBonus + effectivenessBonus + focusBonus;
    }
    
    /**
     * Get technology experiences specific to agent characteristics
     */
    private Map<String, List<TechnologyExperience>> getAgentSpecificTechnologyExperiences(
            AgentCharacteristic characteristic, IntelligentContextRequest request) {
        
        // Get all technology experiences
        List<TechnologyExperience> allExperiences = experienceRepository.getTechnologyExperiences();
        
        // Group by agent characteristic patterns
        Map<String, List<TechnologyExperience>> groupedExperiences = new HashMap<>();
        
        // Group by technology type for this agent characteristic
        Map<String, List<TechnologyExperience>> byType = allExperiences.stream()
            .filter(exp -> isRelevantForCharacteristic(exp, characteristic))
            .collect(Collectors.groupingBy(exp -> exp.getType().toString()));
        
        // Group by agent preference focus areas
        List<String> focusAreas = (List<String>) characteristic.getAttributes().get("focusAreas");
        for (String focusArea : focusAreas) {
            List<TechnologyExperience> focusExperiences = allExperiences.stream()
                .filter(exp -> matchesFocusArea(exp, focusArea))
                .sorted((a, b) -> Double.compare(b.getOverallSatisfaction(), a.getOverallSatisfaction()))
                .limit(10) // Top 10 per focus area
                .collect(Collectors.toList());
            
            if (!focusExperiences.isEmpty()) {
                groupedExperiences.put("focus_" + focusArea, focusExperiences);
            }
        }
        
        // Add type-based groupings
        groupedExperiences.putAll(byType);
        
        // Add harmony-based recommendations
        addHarmonyBasedRecommendations(groupedExperiences, characteristic, request);
        
        return groupedExperiences;
    }
    
    /**
     * Check if technology experience is relevant for agent characteristic
     */
    private boolean isRelevantForCharacteristic(TechnologyExperience experience, AgentCharacteristic characteristic) {
        String evolutionStrategy = (String) characteristic.getAttributes().get("evolutionStrategy");
        List<String> focusAreas = (List<String>) characteristic.getAttributes().get("focusAreas");
        
        // Check if experience aligns with evolution strategy
        boolean strategyMatch = experience.getAgentNotes().toLowerCase()
            .contains(evolutionStrategy.toLowerCase());
        
        // Check if experience aligns with focus areas
        boolean focusMatch = focusAreas.stream()
            .anyMatch(focus -> matchesFocusArea(experience, focus));
        
        // Consider minimum satisfaction threshold based on agent type
        double minSatisfaction = getMinSatisfactionThreshold(characteristic);
        boolean satisfactionMatch = experience.getOverallSatisfaction() >= minSatisfaction;
        
        return strategyMatch || focusMatch || satisfactionMatch;
    }
    
    /**
     * Check if experience matches focus area
     */
    private boolean matchesFocusArea(TechnologyExperience experience, String focusArea) {
        return experience.getUseCases().stream()
            .anyMatch(useCase -> useCase.toLowerCase().contains(focusArea.toLowerCase()))
            || experience.getAgentNotes().toLowerCase().contains(focusArea.toLowerCase())
            || experience.getPros().stream()
                .anyMatch(pro -> pro.toLowerCase().contains(focusArea.toLowerCase()));
    }
    
    /**
     * Get minimum satisfaction threshold for agent characteristic
     */
    private double getMinSatisfactionThreshold(AgentCharacteristic characteristic) {
        Double relevanceThreshold = (Double) characteristic.getAttributes().get("relevanceThreshold");
        
        // Convert relevance threshold to satisfaction threshold
        if (relevanceThreshold <= 0.2) return 8.0; // High standards
        else if (relevanceThreshold <= 0.4) return 6.0; // Medium standards
        else return 4.0; // Lower standards, more inclusive
    }
    
    /**
     * Add harmony-based technology recommendations
     */
    private void addHarmonyBasedRecommendations(
            Map<String, List<TechnologyExperience>> groupedExperiences,
            AgentCharacteristic characteristic,
            IntelligentContextRequest request) {
        
        // Find high-harmony technology combinations for this agent type
        List<TechnologyExperience> allExperiences = experienceRepository.getTechnologyExperiences();
        
        List<TechnologyExperience> harmonyRecommendations = allExperiences.stream()
            .filter(exp -> !exp.getUsedTogether().isEmpty())
            .filter(exp -> exp.getUsedTogether().values().stream()
                .anyMatch(combo -> combo.getHarmonyRating() >= 8.0))
            .sorted((a, b) -> {
                double avgHarmonyA = a.getUsedTogether().values().stream()
                    .mapToDouble(combo -> combo.getHarmonyRating())
                    .average().orElse(0.0);
                double avgHarmonyB = b.getUsedTogether().values().stream()
                    .mapToDouble(combo -> combo.getHarmonyRating())
                    .average().orElse(0.0);
                return Double.compare(avgHarmonyB, avgHarmonyA);
            })
            .limit(15)
            .collect(Collectors.toList());
        
        if (!harmonyRecommendations.isEmpty()) {
            groupedExperiences.put("high_harmony_combinations", harmonyRecommendations);
        }
    }
    
    /**
     * Initialize default context collectors
     */
    private void initializeCollectors() {
        // Add framework-specific collectors
        collectors.put("framework_rules", new FrameworkRulesCollector());
        collectors.put("technology_experience", new TechnologyExperienceCollector(experienceRepository));
        collectors.put("agent_memory", new AgentMemoryCollector());
        collectors.put("code_structure", new CodeStructureCollector());
        collectors.put("documentation", new DocumentationCollector());
        collectors.put("dependency", new DependencyCollector());
        collectors.put("performance", new PerformanceMetricsCollector());
    }
    
    // Helper classes and methods would continue...
    
    /**
     * Record context effectiveness for future optimization
     */
    private void recordContextEffectiveness(
            IntelligentContextRequest request, 
            StructuredContext context) {
        // Implementation for tracking context effectiveness
        String key = generateEffectivenessKey(request);
        ContextEffectivenessMetrics metrics = effectivenessHistory.computeIfAbsent(
            key, k -> new ContextEffectivenessMetrics());
        
        metrics.recordUsage(calculateEffectivenessScore(context));
    }
    
    private double calculateEffectivenessScore(StructuredContext context) {
        // Calculate effectiveness based on context quality metrics
        return context.getRelevanceScore() * 0.4 + 
               context.getCompletenessScore() * 0.3 +
               context.getCoherenceScore() * 0.3;
    }
    
    private String generateEffectivenessKey(IntelligentContextRequest request) {
        return request.getTaskType() + ":" + 
               request.getAgentCharacteristic().getName() + ":" +
               request.getDomain();
    }
    
    private String generateEffectivenessKey(ContextCollector collector, IntelligentContextRequest request) {
        return collector.getMetadata().getName() + ":" + generateEffectivenessKey(request);
    }
    
    private boolean isCollectorRelevant(
            ContextCollector collector, 
            IntelligentContextRequest request, 
            ContextPreferences preferences) {
        // Check if collector matches agent preferences
        String collectorName = collector.getMetadata().getName();
        return preferences.getPriorityCollectors().contains(collectorName) ||
               collector.isApplicable(request.toContextRequest());
    }
    
    private List<ContextFragment> collectContextFragments(
            List<PrioritizedCollector> prioritizedCollectors,
            ContextConstraints constraints) {
        
        List<ContextFragment> fragments = new ArrayList<>();
        int remainingTokens = constraints.getMaxTokens();
        
        for (PrioritizedCollector prioritizedCollector : prioritizedCollectors) {
            ContextCollector collector = prioritizedCollector.getCollector();
            
            if (remainingTokens <= 0) break;
            
            int estimatedCost = collector.getEstimatedCost();
            if (estimatedCost <= remainingTokens) {
                ContextFragment fragment = collector.collect(constraints.getRequest());
                fragments.add(fragment);
                remainingTokens -= estimatedCost;
            }
        }
        
        return fragments;
    }
    
    private double calculateFocusAreaAlignment(ContextCollector collector, ContextPreferences preferences) {
        // Calculate how well collector aligns with agent focus areas
        List<String> focusAreas = preferences.getFocusAreas();
        String collectorName = collector.getMetadata().getName().toLowerCase();
        
        return focusAreas.stream()
            .mapToDouble(focus -> collectorName.contains(focus.toLowerCase()) ? 1.0 : 0.0)
            .max().orElse(0.0);
    }
}