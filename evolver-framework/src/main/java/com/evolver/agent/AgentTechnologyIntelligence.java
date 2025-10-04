package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AGENT-SPECIFIC TECHNOLOGY INTELLIGENCE
 * 
 * Enhanced technology experience system that groups ratings and recommendations
 * by agent characteristics. Different agent types will have different preferences
 * and rating patterns for the same technologies.
 */
public class AgentTechnologyIntelligence {
    
    private final Map<String, Map<String, AgentTechnologyProfile>> intelligenceDatabase;
    private final Map<String, TechnologyExperience> globalExperiences;
    
    public AgentTechnologyIntelligence() {
        this.intelligenceDatabase = new HashMap<>();
        this.globalExperiences = new HashMap<>();
    }
    
    /**
     * Record technology experience for specific agent characteristic
     */
    public void recordAgentExperience(String agentCharacteristicName, TechnologyExperience experience) {
        // Store in global database
        globalExperiences.put(experience.getTechnologyKey(), experience);
        
        // Create agent-specific profile if not exists
        intelligenceDatabase.computeIfAbsent(agentCharacteristicName, k -> new HashMap<>());
        
        Map<String, AgentTechnologyProfile> agentProfiles = intelligenceDatabase.get(agentCharacteristicName);
        
        String techKey = experience.getTechnologyKey();
        AgentTechnologyProfile profile = agentProfiles.computeIfAbsent(
            techKey, k -> new AgentTechnologyProfile(experience.getName(), experience.getVersion()));
        
        profile.addExperience(experience);
    }
    
    /**
     * Get technology recommendations for specific agent characteristic
     */
    public AgentTechnologyRecommendations getRecommendationsForAgent(String agentCharacteristicName, String domain) {
        Map<String, AgentTechnologyProfile> agentProfiles = intelligenceDatabase.get(agentCharacteristicName);
        
        if (agentProfiles == null || agentProfiles.isEmpty()) {
            return createFallbackRecommendations(agentCharacteristicName, domain);
        }
        
        // Group recommendations by categories relevant to agent
        List<TechnologyRecommendation> topRated = getTopRatedForAgent(agentProfiles, 10);
        List<TechnologyRecommendation> harmonious = getHarmoniousForAgent(agentProfiles, 10);
        List<TechnologyRecommendation> domainSpecific = getDomainSpecificForAgent(agentProfiles, domain, 10);
        List<TechnologyRecommendation> emerging = getEmergingForAgent(agentProfiles, 5);
        
        return AgentTechnologyRecommendations.builder()
            .agentCharacteristic(agentCharacteristicName)
            .domain(domain)
            .topRated(topRated)
            .harmoniousCombinations(harmonious)
            .domainSpecific(domainSpecific)
            .emergingTechnologies(emerging)
            .build();
    }
    
    /**
     * Get agent-specific rating analysis for a technology
     */
    public AgentRatingAnalysis getAgentRatingAnalysis(String technology, String version) {
        String techKey = technology.toLowerCase() + ":" + version;
        
        Map<String, List<Double>> ratingsByAgent = new HashMap<>();
        Map<String, List<String>> notesByAgent = new HashMap<>();
        
        // Collect ratings from all agent types
        for (Map.Entry<String, Map<String, AgentTechnologyProfile>> agentEntry : intelligenceDatabase.entrySet()) {
            String agentType = agentEntry.getKey();
            AgentTechnologyProfile profile = agentEntry.getValue().get(techKey);
            
            if (profile != null) {
                ratingsByAgent.put(agentType, profile.getAllOverallRatings());
                notesByAgent.put(agentType, profile.getAllAgentNotes());
            }
        }
        
        return AgentRatingAnalysis.builder()
            .technology(technology)
            .version(version)
            .ratingsByAgent(ratingsByAgent)
            .notesByAgent(notesByAgent)
            .agentConsensus(calculateAgentConsensus(ratingsByAgent))
            .agentDivergence(calculateAgentDivergence(ratingsByAgent))
            .build();
    }
    
    /**
     * Get technology harmony matrix for agent characteristic
     */
    public Map<String, Map<String, Double>> getHarmonyMatrixForAgent(String agentCharacteristicName) {
        Map<String, AgentTechnologyProfile> agentProfiles = intelligenceDatabase.get(agentCharacteristicName);
        
        if (agentProfiles == null) {
            return new HashMap<>();
        }
        
        Map<String, Map<String, Double>> harmonyMatrix = new HashMap<>();
        
        for (AgentTechnologyProfile profile : agentProfiles.values()) {
            String techName = profile.getTechnologyName();
            Map<String, Double> techHarmony = new HashMap<>();
            
            // Collect harmony ratings from all experiences
            for (TechnologyExperience experience : profile.getExperiences()) {
                for (Map.Entry<String, TechnologyCombination> entry : experience.getUsedTogether().entrySet()) {
                    String otherTech = entry.getKey();
                    double harmonyRating = entry.getValue().getHarmonyRating();
                    
                    // Use weighted average if multiple experiences exist
                    techHarmony.merge(otherTech, harmonyRating, (existing, newRating) -> 
                        (existing + newRating) / 2.0);
                }
            }
            
            if (!techHarmony.isEmpty()) {
                harmonyMatrix.put(techName, techHarmony);
            }
        }
        
        return harmonyMatrix;
    }
    
    /**
     * Get comparative analysis across agent types
     */
    public ComparativeAgentAnalysis getComparativeAnalysis(String technology, String version) {
        String techKey = technology.toLowerCase() + ":" + version;
        
        Map<String, AgentTechnologyProfile> profilesByAgent = new HashMap<>();
        
        // Collect profiles from all agent types
        for (Map.Entry<String, Map<String, AgentTechnologyProfile>> agentEntry : intelligenceDatabase.entrySet()) {
            String agentType = agentEntry.getKey();
            AgentTechnologyProfile profile = agentEntry.getValue().get(techKey);
            
            if (profile != null) {
                profilesByAgent.put(agentType, profile);
            }
        }
        
        return ComparativeAgentAnalysis.builder()
            .technology(technology)
            .version(version)
            .profilesByAgent(profilesByAgent)
            .agentPreferencePatterns(analyzeAgentPreferencePatterns(profilesByAgent))
            .ratingDistribution(analyzeRatingDistribution(profilesByAgent))
            .consensusMetrics(calculateConsensusMetrics(profilesByAgent))
            .build();
    }
    
    /**
     * Get top-rated technologies for specific agent type
     */
    private List<TechnologyRecommendation> getTopRatedForAgent(
            Map<String, AgentTechnologyProfile> agentProfiles, int limit) {
        
        return agentProfiles.values().stream()
            .filter(profile -> profile.getAverageRating() >= 7.0)
            .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
            .limit(limit)
            .map(profile -> TechnologyRecommendation.builder()
                .technology(profile.getTechnologyName())
                .version(profile.getVersion())
                .rating(profile.getAverageRating())
                .reason("High agent satisfaction: " + String.format("%.1f", profile.getAverageRating()))
                .category("top_rated")
                .confidence(profile.getConfidenceScore())
                .build())
            .collect(Collectors.toList());
    }
    
    /**
     * Get harmonious technology combinations for agent
     */
    private List<TechnologyRecommendation> getHarmoniousForAgent(
            Map<String, AgentTechnologyProfile> agentProfiles, int limit) {
        
        List<TechnologyRecommendation> harmonious = new ArrayList<>();
        
        for (AgentTechnologyProfile profile : agentProfiles.values()) {
            // Find technologies with high harmony ratings
            for (TechnologyExperience experience : profile.getExperiences()) {
                for (Map.Entry<String, TechnologyCombination> entry : experience.getUsedTogether().entrySet()) {
                    TechnologyCombination combo = entry.getValue();
                    if (combo.getHarmonyRating() >= 8.0) {
                        harmonious.add(TechnologyRecommendation.builder()
                            .technology(experience.getName())
                            .version(experience.getVersion())
                            .rating(combo.getHarmonyRating())
                            .reason("High harmony with " + entry.getKey() + ": " + combo.getIntegrationNotes())
                            .category("harmonious_combination")
                            .confidence(0.9)
                            .build());
                    }
                }
            }
        }
        
        return harmonious.stream()
            .sorted((a, b) -> Double.compare(b.getRating(), a.getRating()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get domain-specific recommendations for agent
     */
    private List<TechnologyRecommendation> getDomainSpecificForAgent(
            Map<String, AgentTechnologyProfile> agentProfiles, String domain, int limit) {
        
        return agentProfiles.values().stream()
            .filter(profile -> profile.getExperiences().stream()
                .anyMatch(exp -> exp.getProjectContext().toLowerCase().contains(domain.toLowerCase())))
            .sorted((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()))
            .limit(limit)
            .map(profile -> TechnologyRecommendation.builder()
                .technology(profile.getTechnologyName())
                .version(profile.getVersion())
                .rating(profile.getAverageRating())
                .reason("Proven in " + domain + " context")
                .category("domain_specific")
                .confidence(profile.getConfidenceScore())
                .build())
            .collect(Collectors.toList());
    }
    
    /**
     * Get emerging technologies for agent
     */
    private List<TechnologyRecommendation> getEmergingForAgent(
            Map<String, AgentTechnologyProfile> agentProfiles, int limit) {
        
        LocalDateTime recentThreshold = LocalDateTime.now().minusMonths(6);
        
        return agentProfiles.values().stream()
            .filter(profile -> profile.getLatestExperienceDate().isAfter(recentThreshold))
            .filter(profile -> profile.getExperienceCount() <= 3) // Still experimental
            .filter(profile -> profile.getAverageRating() >= 6.0) // Promising
            .sorted((a, b) -> b.getLatestExperienceDate().compareTo(a.getLatestExperienceDate()))
            .limit(limit)
            .map(profile -> TechnologyRecommendation.builder()
                .technology(profile.getTechnologyName())
                .version(profile.getVersion())
                .rating(profile.getAverageRating())
                .reason("Emerging technology with promising results")
                .category("emerging")
                .confidence(0.7) // Lower confidence for emerging
                .build())
            .collect(Collectors.toList());
    }
    
    /**
     * Create fallback recommendations when no agent-specific data exists
     */
    private AgentTechnologyRecommendations createFallbackRecommendations(String agentCharacteristicName, String domain) {
        // Use global experiences as fallback
        List<TechnologyRecommendation> fallback = globalExperiences.values().stream()
            .filter(exp -> exp.getOverallSatisfaction() >= 7.0)
            .sorted((a, b) -> Double.compare(b.getOverallSatisfaction(), a.getOverallSatisfaction()))
            .limit(10)
            .map(exp -> TechnologyRecommendation.builder()
                .technology(exp.getName())
                .version(exp.getVersion())
                .rating(exp.getOverallSatisfaction())
                .reason("General recommendation - no agent-specific data available")
                .category("fallback")
                .confidence(0.5)
                .build())
            .collect(Collectors.toList());
        
        return AgentTechnologyRecommendations.builder()
            .agentCharacteristic(agentCharacteristicName)
            .domain(domain)
            .topRated(fallback)
            .harmoniousCombinations(new ArrayList<>())
            .domainSpecific(new ArrayList<>())
            .emergingTechnologies(new ArrayList<>())
            .build();
    }
    
    // Analysis helper methods
    private double calculateAgentConsensus(Map<String, List<Double>> ratingsByAgent) {
        if (ratingsByAgent.size() < 2) return 1.0;
        
        List<Double> averagesByAgent = ratingsByAgent.values().stream()
            .map(ratings -> ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
            .collect(Collectors.toList());
        
        double mean = averagesByAgent.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = averagesByAgent.stream()
            .mapToDouble(avg -> Math.pow(avg - mean, 2))
            .average().orElse(0.0);
        
        // Return inverse of standard deviation normalized (higher = more consensus)
        return Math.max(0.0, 1.0 - Math.sqrt(variance) / 10.0);
    }
    
    private double calculateAgentDivergence(Map<String, List<Double>> ratingsByAgent) {
        return 1.0 - calculateAgentConsensus(ratingsByAgent);
    }
    
    private Map<String, String> analyzeAgentPreferencePatterns(Map<String, AgentTechnologyProfile> profilesByAgent) {
        Map<String, String> patterns = new HashMap<>();
        
        for (Map.Entry<String, AgentTechnologyProfile> entry : profilesByAgent.entrySet()) {
            String agentType = entry.getKey();
            AgentTechnologyProfile profile = entry.getValue();
            
            // Analyze rating patterns and preferences
            String pattern = analyzeIndividualAgentPattern(profile);
            patterns.put(agentType, pattern);
        }
        
        return patterns;
    }
    
    private String analyzeIndividualAgentPattern(AgentTechnologyProfile profile) {
        List<TechnologyExperience> experiences = profile.getExperiences();
        
        // Analyze rating tendencies
        double avgEaseOfUse = experiences.stream().mapToDouble(TechnologyExperience::getEaseOfUse).average().orElse(0.0);
        double avgPower = experiences.stream().mapToDouble(TechnologyExperience::getPower).average().orElse(0.0);
        double avgDocQuality = experiences.stream().mapToDouble(TechnologyExperience::getDocumentationQuality).average().orElse(0.0);
        
        StringBuilder pattern = new StringBuilder();
        
        if (avgEaseOfUse > 7.0) pattern.append("Values ease of use. ");
        if (avgPower > 7.0) pattern.append("Prefers powerful tools. ");
        if (avgDocQuality > 7.0) pattern.append("Requires good documentation. ");
        
        return pattern.toString().trim();
    }
    
    private Map<String, Double> analyzeRatingDistribution(Map<String, AgentTechnologyProfile> profilesByAgent) {
        Map<String, Double> distribution = new HashMap<>();
        
        for (Map.Entry<String, AgentTechnologyProfile> entry : profilesByAgent.entrySet()) {
            distribution.put(entry.getKey(), entry.getValue().getAverageRating());
        }
        
        return distribution;
    }
    
    private Map<String, Object> calculateConsensusMetrics(Map<String, AgentTechnologyProfile> profilesByAgent) {
        Map<String, Object> metrics = new HashMap<>();
        
        List<Double> averages = profilesByAgent.values().stream()
            .map(AgentTechnologyProfile::getAverageRating)
            .collect(Collectors.toList());
        
        double mean = averages.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double standardDeviation = Math.sqrt(averages.stream()
            .mapToDouble(avg -> Math.pow(avg - mean, 2))
            .average().orElse(0.0));
        
        metrics.put("mean_rating", mean);
        metrics.put("standard_deviation", standardDeviation);
        metrics.put("consensus_level", Math.max(0.0, 1.0 - standardDeviation / 10.0));
        
        return metrics;
    }
}