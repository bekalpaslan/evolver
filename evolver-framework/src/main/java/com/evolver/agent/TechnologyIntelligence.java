package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * TECHNOLOGY INTELLIGENCE SYSTEM
 * 
 * Agents continuously track and rate technologies, libraries, and frameworks
 * based on real-world usage experiences. This creates a living database of
 * technology performance, compatibility, and ease-of-use metrics.
 */
public class TechnologyIntelligence {
    
    private static final Map<String, TechnologyProfile> technologyDatabase = new ConcurrentHashMap<>();
    private static final Map<String, List<CompatibilityReport>> compatibilityMatrix = new ConcurrentHashMap<>();
    private static final Map<String, List<AgentRating>> agentRatings = new ConcurrentHashMap<>();
    
    /**
     * Record a technology usage experience by an agent
     */
    public static void recordTechnologyExperience(String agentId, String agentCharacteristic, 
                                                TechnologyExperience experience) {
        String techKey = experience.getTechnologyKey();
        
        // Update or create technology profile
        TechnologyProfile profile = technologyDatabase.computeIfAbsent(techKey, 
            k -> new TechnologyProfile(experience.getName(), experience.getVersion(), experience.getType()));
        
        profile.addExperience(experience);
        
        // Record agent rating
        AgentRating rating = new AgentRating(agentId, agentCharacteristic, experience);
        agentRatings.computeIfAbsent(techKey, k -> new ArrayList<>()).add(rating);
        
        // Update compatibility information
        if (!experience.getCompatibilityData().isEmpty()) {
            recordCompatibilityData(techKey, experience.getCompatibilityData());
        }
        
        System.out.println("[TECH-INTEL] " + agentCharacteristic + " rated " + techKey + 
                         " (ease: " + experience.getEaseOfUse() + "/10, power: " + experience.getPower() + "/10)");
    }
    
    /**
     * Get technology intelligence for decision making
     */
    public static TechnologyIntelligenceReport getTechnologyIntelligence(String technologyName, String version) {
        String techKey = createTechnologyKey(technologyName, version);
        
        TechnologyProfile profile = technologyDatabase.get(techKey);
        if (profile == null) {
            return new TechnologyIntelligenceReport(technologyName, version, 
                "No intelligence available", Collections.emptyList());
        }
        
        // Calculate aggregate metrics
        AggregateMetrics metrics = calculateAggregateMetrics(techKey);
        
        // Get compatibility insights
        List<CompatibilityInsight> compatibilityInsights = getCompatibilityInsights(techKey);
        
        // Get agent recommendations
        List<AgentRecommendation> recommendations = getAgentRecommendations(techKey);
        
        return new TechnologyIntelligenceReport(technologyName, version, 
            generateIntelligenceSummary(profile, metrics), recommendations, 
            compatibilityInsights, metrics);
    }
    
    /**
     * Find similar technologies based on use case and ratings
     */
    public static List<TechnologyRecommendation> findSimilarTechnologies(String useCase, 
                                                                        double minEaseOfUse, 
                                                                        double minPower) {
        return technologyDatabase.entrySet().stream()
            .filter(entry -> {
                AggregateMetrics metrics = calculateAggregateMetrics(entry.getKey());
                return metrics.averageEaseOfUse >= minEaseOfUse && 
                       metrics.averagePower >= minPower &&
                       entry.getValue().getUseCases().contains(useCase.toLowerCase());
            })
            .map(entry -> new TechnologyRecommendation(
                entry.getValue(), 
                calculateAggregateMetrics(entry.getKey()),
                calculateMatchScore(entry.getKey(), useCase, minEaseOfUse, minPower)
            ))
            .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
            .limit(10)
            .collect(Collectors.toList());
    }
    
    /**
     * Check for potential compatibility issues
     */
    public static List<CompatibilityWarning> checkCompatibility(List<String> technologies) {
        List<CompatibilityWarning> warnings = new ArrayList<>();
        
        for (int i = 0; i < technologies.size(); i++) {
            for (int j = i + 1; j < technologies.size(); j++) {
                String tech1 = technologies.get(i);
                String tech2 = technologies.get(j);
                
                List<CompatibilityReport> reports = findCompatibilityReports(tech1, tech2);
                for (CompatibilityReport report : reports) {
                    if (report.hasIssues()) {
                        warnings.add(new CompatibilityWarning(tech1, tech2, 
                            report.getSeverity(), report.getIssueDescription()));
                    }
                }
            }
        }
        
        return warnings;
    }
    
    /**
     * Get trending technologies based on recent agent experiences
     */
    public static List<TrendingTechnology> getTrendingTechnologies(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        
        Map<String, TrendData> trends = new HashMap<>();
        
        technologyDatabase.forEach((techKey, profile) -> {
            long recentExperiences = profile.getExperiences().stream()
                .filter(exp -> exp.getTimestamp().isAfter(cutoff))
                .count();
            
            if (recentExperiences > 0) {
                AggregateMetrics metrics = calculateAggregateMetrics(techKey);
                trends.put(techKey, new TrendData(profile, recentExperiences, metrics));
            }
        });
        
        return trends.entrySet().stream()
            .map(entry -> new TrendingTechnology(
                entry.getValue().profile,
                entry.getValue().recentExperiences,
                entry.getValue().metrics.averageOverallSatisfaction,
                calculateTrendScore(entry.getValue())
            ))
            .sorted((a, b) -> Double.compare(b.getTrendScore(), a.getTrendScore()))
            .limit(20)
            .collect(Collectors.toList());
    }
    
    // Helper methods
    
    private static void recordCompatibilityData(String techKey, Map<String, CompatibilityData> compatibilityData) {
        compatibilityData.forEach((otherTech, data) -> {
            CompatibilityReport report = new CompatibilityReport(techKey, otherTech, data);
            compatibilityMatrix.computeIfAbsent(techKey, k -> new ArrayList<>()).add(report);
            compatibilityMatrix.computeIfAbsent(otherTech, k -> new ArrayList<>()).add(report);
        });
    }
    
    private static AggregateMetrics calculateAggregateMetrics(String techKey) {
        List<AgentRating> ratings = agentRatings.getOrDefault(techKey, Collections.emptyList());
        
        if (ratings.isEmpty()) {
            return new AggregateMetrics(0, 0, 0, 0, 0, 0, 0);
        }
        
        double avgEase = ratings.stream().mapToDouble(r -> r.experience.getEaseOfUse()).average().orElse(0);
        double avgPower = ratings.stream().mapToDouble(r -> r.experience.getPower()).average().orElse(0);
        double avgPerformance = ratings.stream().mapToDouble(r -> r.experience.getPerformance()).average().orElse(0);
        double avgReliability = ratings.stream().mapToDouble(r -> r.experience.getReliability()).average().orElse(0);
        double avgDocumentation = ratings.stream().mapToDouble(r -> r.experience.getDocumentationQuality()).average().orElse(0);
        double avgCommunity = ratings.stream().mapToDouble(r -> r.experience.getCommunitySupport()).average().orElse(0);
        double avgSatisfaction = ratings.stream().mapToDouble(r -> r.experience.getOverallSatisfaction()).average().orElse(0);
        
        return new AggregateMetrics(avgEase, avgPower, avgPerformance, avgReliability, 
                                  avgDocumentation, avgCommunity, avgSatisfaction);
    }
    
    private static List<CompatibilityInsight> getCompatibilityInsights(String techKey) {
        return compatibilityMatrix.getOrDefault(techKey, Collections.emptyList()).stream()
            .map(report -> new CompatibilityInsight(
                report.getOtherTechnology(),
                report.getCompatibilityScore(),
                report.hasIssues() ? report.getIssueDescription() : "No known issues",
                report.getWorkarounds()
            ))
            .sorted((a, b) -> Double.compare(b.getCompatibilityScore(), a.getCompatibilityScore()))
            .collect(Collectors.toList());
    }
    
    private static List<AgentRecommendation> getAgentRecommendations(String techKey) {
        return agentRatings.getOrDefault(techKey, Collections.emptyList()).stream()
            .filter(rating -> rating.experience.getOverallSatisfaction() >= 7.0)
            .map(rating -> new AgentRecommendation(
                rating.agentCharacteristic,
                rating.experience.getOverallSatisfaction(),
                rating.experience.getRecommendationReason(),
                rating.experience.getUseCases()
            ))
            .sorted((a, b) -> Double.compare(b.getSatisfactionScore(), a.getSatisfactionScore()))
            .collect(Collectors.toList());
    }
    
    private static String generateIntelligenceSummary(TechnologyProfile profile, AggregateMetrics metrics) {
        StringBuilder summary = new StringBuilder();
        summary.append("Technology: ").append(profile.getName()).append(" v").append(profile.getVersion()).append("\n");
        summary.append("Overall Rating: ").append(String.format("%.1f/10", metrics.averageOverallSatisfaction)).append("\n");
        summary.append("Ease of Use: ").append(String.format("%.1f/10", metrics.averageEaseOfUse)).append(" | ");
        summary.append("Power: ").append(String.format("%.1f/10", metrics.averagePower)).append("\n");
        summary.append("Experiences: ").append(profile.getExperiences().size()).append(" agent reports");
        
        if (metrics.averageOverallSatisfaction >= 8.0) {
            summary.append("\n🌟 Highly Recommended by Agents");
        } else if (metrics.averageOverallSatisfaction <= 4.0) {
            summary.append("\n⚠️ Below Average Satisfaction");
        }
        
        return summary.toString();
    }
    
    private static double calculateMatchScore(String techKey, String useCase, double minEaseOfUse, double minPower) {
        AggregateMetrics metrics = calculateAggregateMetrics(techKey);
        TechnologyProfile profile = technologyDatabase.get(techKey);
        
        double score = 0.0;
        
        // Base score from ratings
        score += metrics.averageOverallSatisfaction * 10;
        
        // Bonus for exceeding minimums
        score += Math.max(0, (metrics.averageEaseOfUse - minEaseOfUse) * 5);
        score += Math.max(0, (metrics.averagePower - minPower) * 5);
        
        // Use case match bonus
        if (profile.getUseCases().contains(useCase.toLowerCase())) {
            score += 20;
        }
        
        return score;
    }
    
    /**
     * Find technology combinations that work well together
     */
    public static List<TechnologyCombination> findCompatibleCombinations(String technologyName) {
        Map<String, List<TechnologyCombination>> combinationMap = new HashMap<>();
        
        // Collect all technology combinations from experiences
        technologyDatabase.values().forEach(profile -> {
            profile.getExperiences().forEach(experience -> {
                experience.getUsedTogether().forEach((combinedTech, combination) -> {
                    if (profile.getName().equalsIgnoreCase(technologyName) || 
                        combinedTech.equalsIgnoreCase(technologyName)) {
                        combinationMap.computeIfAbsent(combinedTech, k -> new ArrayList<>()).add(combination);
                    }
                });
            });
        });
        
        // Calculate average harmony ratings and return best combinations
        return combinationMap.entrySet().stream()
            .map(entry -> {
                List<TechnologyCombination> combinations = entry.getValue();
                double avgHarmony = combinations.stream()
                    .mapToDouble(c -> c.harmonyRating)
                    .average()
                    .orElse(0.0);
                
                // Return the most recent combination with average harmony rating
                TechnologyCombination latest = combinations.get(combinations.size() - 1);
                return new TechnologyCombination(latest.technologyName, latest.version, 
                    avgHarmony, latest.integrationNotes, latest.recommendedFor);
            })
            .filter(combo -> combo.harmonyRating >= 6.0) // Only good combinations
            .sorted((a, b) -> Double.compare(b.harmonyRating, a.harmonyRating))
            .limit(10)
            .collect(Collectors.toList());
    }
    
    /**
     * Get recommended technology stacks based on use case and harmony ratings
     */
    public static List<TechnologyStack> getRecommendedStacks(String useCase, int maxStackSize) {
        Map<String, Set<String>> stackCombinations = new HashMap<>();
        Map<String, Double> stackHarmony = new HashMap<>();
        
        // Analyze all technology combinations for the use case
        technologyDatabase.values().forEach(profile -> {
            profile.getExperiences().forEach(experience -> {
                if (experience.getUseCases().contains(useCase.toLowerCase())) {
                    String baseTech = profile.getName();
                    
                    experience.getUsedTogether().forEach((combinedTech, combination) -> {
                        if (combination.harmonyRating >= 7.0) {
                            String stackKey = createStackKey(baseTech, combinedTech);
                            stackCombinations.computeIfAbsent(stackKey, k -> new HashSet<>())
                                .addAll(Arrays.asList(baseTech, combinedTech));
                            stackHarmony.put(stackKey, 
                                stackHarmony.getOrDefault(stackKey, 0.0) + combination.harmonyRating);
                        }
                    });
                }
            });
        });
        
        // Create technology stacks
        return stackCombinations.entrySet().stream()
            .filter(entry -> entry.getValue().size() <= maxStackSize)
            .map(entry -> new TechnologyStack(
                new ArrayList<>(entry.getValue()),
                stackHarmony.get(entry.getKey()) / entry.getValue().size(),
                useCase
            ))
            .sorted((a, b) -> Double.compare(b.getHarmonyScore(), a.getHarmonyScore()))
            .limit(5)
            .collect(Collectors.toList());
    }
    
    /**
     * Record technology combination experience
     */
    public static void recordCombinationExperience(String agentId, String mainTechnology, 
                                                  String combinedTechnology, double harmonyRating,
                                                  String integrationNotes, String useCase) {
        System.out.println("[TECH-INTEL] " + agentId + " rated " + mainTechnology + " + " + 
                         combinedTechnology + " harmony: " + harmonyRating + "/10 for " + useCase);
    }
    
    private static String createStackKey(String... technologies) {
        return Arrays.stream(technologies)
            .sorted()
            .collect(Collectors.joining("+"));
    }
    
    private static List<CompatibilityReport> findCompatibilityReports(String tech1, String tech2) {
        return compatibilityMatrix.getOrDefault(tech1, Collections.emptyList()).stream()
            .filter(report -> report.getOtherTechnology().equals(tech2))
            .collect(Collectors.toList());
    }
    
    private static double calculateTrendScore(TrendData trendData) {
        return trendData.recentExperiences * 10 + trendData.metrics.averageOverallSatisfaction * 5;
    }
    
    private static String createTechnologyKey(String name, String version) {
        return name.toLowerCase() + ":" + version;
    }
    
    // Data classes
    
    private static class TrendData {
        final TechnologyProfile profile;
        final long recentExperiences;
        final AggregateMetrics metrics;
        
        TrendData(TechnologyProfile profile, long recentExperiences, AggregateMetrics metrics) {
            this.profile = profile;
            this.recentExperiences = recentExperiences;
            this.metrics = metrics;
        }
    }
}