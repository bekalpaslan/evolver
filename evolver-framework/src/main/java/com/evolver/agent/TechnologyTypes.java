package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Supporting types for technology intelligence system
 */

/**
 * Compatibility data between technologies
 */
class CompatibilityData {
    public final String technology1;
    public final String technology2;
    public final double compatibilityScore; // 0.0 - 10.0
    public final String description;
    public final List<String> knownIssues;
    public final List<String> workarounds;
    public final LocalDateTime lastUpdated;
    
    public CompatibilityData(String tech1, String tech2, double score, String description) {
        this.technology1 = tech1;
        this.technology2 = tech2;
        this.compatibilityScore = Math.max(0.0, Math.min(10.0, score));
        this.description = description;
        this.knownIssues = new ArrayList<>();
        this.workarounds = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public CompatibilityData addIssue(String issue) {
        this.knownIssues.add(issue);
        return this;
    }
    
    public CompatibilityData addWorkaround(String workaround) {
        this.workarounds.add(workaround);
        return this;
    }
    
    public boolean isHighCompatibility() {
        return compatibilityScore >= 8.0;
    }
    
    public boolean isLowCompatibility() {
        return compatibilityScore < 5.0;
    }
}

/**
 * Technology combination data - tracks technologies used together
 * Includes harmony rating and integration notes
 */
class TechnologyCombination {
    public final String technologyName;
    public final String version;
    public final double harmonyRating; // 0.0 - 10.0 (how well they work together)
    public final String integrationNotes;
    public final String recommendedFor; // Use case where this combo works well
    public final LocalDateTime recordedAt;
    
    public TechnologyCombination(String technology, String version, double harmonyRating, 
                                String integrationNotes, String recommendedFor) {
        this.technologyName = technology;
        this.version = version;
        this.harmonyRating = roundToDecimal(harmonyRating, 1);
        this.integrationNotes = integrationNotes != null ? integrationNotes : "";
        this.recommendedFor = recommendedFor != null ? recommendedFor : "";
        this.recordedAt = LocalDateTime.now();
    }
    
    public boolean isHighHarmony() {
        return harmonyRating >= 8.0;
    }
    
    public boolean isLowHarmony() {
        return harmonyRating < 5.0;
    }
    
    public String getCompatibilityLevel() {
        if (harmonyRating >= 9.0) return "Excellent Harmony";
        if (harmonyRating >= 7.0) return "Good Harmony";
        if (harmonyRating >= 5.0) return "Acceptable Harmony";
        if (harmonyRating >= 3.0) return "Poor Harmony";
        return "Incompatible";
    }
    
    private double roundToDecimal(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(Math.max(0, Math.min(10, value)) * multiplier) / multiplier;
    }
    
    @Override
    public String toString() {
        return String.format("%s v%s (Harmony: %.1f/10 - %s)", 
            technologyName, version, harmonyRating, getCompatibilityLevel());
    }
}

/**
 * Aggregate metrics for technology analysis
 */
class AggregateMetrics {
    public final double averageEaseOfUse;
    public final double averagePower;
    public final double averageOverallSatisfaction;
    public final int totalReports;
    public final List<String> topUseCases;
    public final List<String> commonPros;
    public final List<String> commonCons;
    
    public AggregateMetrics(List<TechnologyExperience> experiences) {
        this.totalReports = experiences.size();
        this.averageEaseOfUse = experiences.stream()
            .mapToDouble(TechnologyExperience::getEaseOfUse)
            .average()
            .orElse(0.0);
        this.averagePower = experiences.stream()
            .mapToDouble(TechnologyExperience::getPower)
            .average()
            .orElse(0.0);
        this.averageOverallSatisfaction = experiences.stream()
            .mapToDouble(TechnologyExperience::getOverallSatisfaction)
            .average()
            .orElse(0.0);
        
        // Aggregate common use cases, pros, and cons
        Map<String, Integer> useCaseCount = new HashMap<>();
        Map<String, Integer> prosCount = new HashMap<>();
        Map<String, Integer> consCount = new HashMap<>();
        
        for (TechnologyExperience exp : experiences) {
            exp.getUseCases().forEach(useCase -> 
                useCaseCount.merge(useCase, 1, Integer::sum));
            exp.getPros().forEach(pro -> 
                prosCount.merge(pro, 1, Integer::sum));
            exp.getCons().forEach(con -> 
                consCount.merge(con, 1, Integer::sum));
        }
        
        this.topUseCases = getTopEntries(useCaseCount, 5);
        this.commonPros = getTopEntries(prosCount, 5);
        this.commonCons = getTopEntries(consCount, 5);
    }
    
    private List<String> getTopEntries(Map<String, Integer> countMap, int limit) {
        return countMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toList());
    }
}

/**
 * Technology intelligence report for agents
 */
class TechnologyIntelligenceReport {
    public final String technologyName;
    public final String version;
    public final AggregateMetrics metrics;
    public final Map<String, CompatibilityData> compatibilityData;
    public final List<String> agentRecommendations;
    public final LocalDateTime generatedAt;
    
    public TechnologyIntelligenceReport(String name, String version, AggregateMetrics metrics, 
                                      Map<String, CompatibilityData> compatibility) {
        this.technologyName = name;
        this.version = version;
        this.metrics = metrics;
        this.compatibilityData = new HashMap<>(compatibility);
        this.agentRecommendations = new ArrayList<>();
        this.generatedAt = LocalDateTime.now();
    }
    
    public void addRecommendation(String agentName, double rating, String recommendation) {
        agentRecommendations.add(agentName + " (" + rating + "/10): " + recommendation);
    }
    
    public String getOverallRecommendation() {
        if (metrics.averageOverallSatisfaction >= 8.0) {
            return "Highly Recommended by Agents";
        } else if (metrics.averageOverallSatisfaction >= 6.0) {
            return "Recommended with Reservations";
        } else if (metrics.averageOverallSatisfaction >= 4.0) {
            return "Use with Caution";
        } else {
            return "Not Recommended";
        }
    }
    
    public void printReport() {
        System.out.println("TECHNOLOGY INTELLIGENCE REPORT");
        System.out.println("==============================");
        System.out.println("Technology: " + technologyName + " v" + version);
        System.out.println("Overall Rating: " + String.format("%.1f", metrics.averageOverallSatisfaction) + "/10");
        System.out.println("Ease of Use: " + String.format("%.1f", metrics.averageEaseOfUse) + "/10");
        System.out.println("Power: " + String.format("%.1f", metrics.averagePower) + "/10");
        System.out.println("Reports: " + metrics.totalReports + " agent experiences");
        System.out.println("Status: " + getOverallRecommendation());
        
        if (!metrics.topUseCases.isEmpty()) {
            System.out.println("Top Use Cases: " + String.join(", ", metrics.topUseCases));
        }
        
        if (!agentRecommendations.isEmpty()) {
            System.out.println("Agent Recommendations:");
            agentRecommendations.forEach(rec -> System.out.println("  - " + rec));
        }
    }
}

/**
 * Technology recommendation with scoring
 */
class TechnologyRecommendation {
    public final String technologyName;
    public final String version;
    public final double overallScore;
    public final double easeOfUse;
    public final double power;
    public final List<String> useCases;
    public final String recommendation;
    public final double matchScore;
    
    public TechnologyRecommendation(String name, String version, AggregateMetrics metrics, 
                                  double matchScore, String recommendation) {
        this.technologyName = name;
        this.version = version;
        this.overallScore = metrics.averageOverallSatisfaction;
        this.easeOfUse = metrics.averageEaseOfUse;
        this.power = metrics.averagePower;
        this.useCases = new ArrayList<>(metrics.topUseCases);
        this.matchScore = matchScore;
        this.recommendation = recommendation;
    }
    
    public void printRecommendation() {
        System.out.println(technologyName + " v" + version + " (Score: " + String.format("%.1f", matchScore) + ")");
        System.out.println("   Overall: " + String.format("%.1f", overallScore) + "/10" + 
                         " | Ease: " + String.format("%.1f", easeOfUse) + "/10" +
                         " | Power: " + String.format("%.1f", power) + "/10");
        if (!useCases.isEmpty()) {
            System.out.println("   Use cases: " + String.join(", ", useCases));
        }
        System.out.println();
    }
}

/**
 * Compatibility warning for agent guidance
 */
class CompatibilityWarning {
    public final String technology1;
    public final String technology2;
    public final String warningLevel; // "LOW", "MEDIUM", "HIGH"
    public final String description;
    public final List<String> recommendedActions;
    
    public CompatibilityWarning(String tech1, String tech2, String level, String description) {
        this.technology1 = tech1;
        this.technology2 = tech2;
        this.warningLevel = level;
        this.description = description;
        this.recommendedActions = new ArrayList<>();
    }
    
    public CompatibilityWarning addAction(String action) {
        this.recommendedActions.add(action);
        return this;
    }
    
    public void printWarning() {
        System.out.println("[" + warningLevel + " COMPATIBILITY] " + technology1 + " + " + technology2);
        System.out.println("   " + description);
        if (!recommendedActions.isEmpty()) {
            System.out.println("   Actions: " + String.join(", ", recommendedActions));
        }
    }
}

/**
 * Trending technology information
 */
class TrendingTechnology {
    public final String name;
    public final String currentVersion;
    public final double trendScore; // 0.0 - 10.0, higher means more trending
    public final int recentExperiences; // Number of experiences in last 30 days
    public final double averageRating;
    public final List<String> emergingUseCases;
    
    public TrendingTechnology(String name, String version, double trendScore, 
                            int recentExperiences, double averageRating) {
        this.name = name;
        this.currentVersion = version;
        this.trendScore = trendScore;
        this.recentExperiences = recentExperiences;
        this.averageRating = averageRating;
        this.emergingUseCases = new ArrayList<>();
    }
    
    public TrendingTechnology addEmergingUseCase(String useCase) {
        this.emergingUseCases.add(useCase);
        return this;
    }
    
    public boolean isHotTrend() {
        return trendScore >= 8.0 && recentExperiences >= 5;
    }
    
    public void printTrend() {
        System.out.println("[TRENDING] " + name + " v" + currentVersion + 
                         " (Trend: " + String.format("%.1f", trendScore) + "/10)");
        System.out.println("   Recent experiences: " + recentExperiences + 
                         " | Average rating: " + String.format("%.1f", averageRating) + "/10");
        if (!emergingUseCases.isEmpty()) {
            System.out.println("   Emerging use cases: " + String.join(", ", emergingUseCases));
        }
    }
}

/**
 * Technology stack recommendation - group of technologies that work well together
 */
class TechnologyStack {
    public final List<String> technologies;
    public final double harmonyScore; // Average harmony rating of all combinations
    public final String recommendedFor; // Use case this stack is optimized for
    public final int experienceCount;
    public final LocalDateTime lastUpdated;
    
    public TechnologyStack(List<String> technologies, double harmonyScore, String recommendedFor) {
        this.technologies = new ArrayList<>(technologies);
        this.harmonyScore = roundToDecimal(harmonyScore, 1);
        this.recommendedFor = recommendedFor;
        this.experienceCount = 1;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public boolean isHighHarmonyStack() {
        return harmonyScore >= 8.0;
    }
    
    public String getStackName() {
        return String.join(" + ", technologies);
    }
    
    public double getHarmonyScore() {
        return harmonyScore;
    }
    
    private double roundToDecimal(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return Math.round(Math.max(0, Math.min(10, value)) * multiplier) / multiplier;
    }
    
    @Override
    public String toString() {
        return String.format("Stack: %s (Harmony: %.1f/10) - Recommended for: %s", 
            getStackName(), harmonyScore, recommendedFor);
    }
}

/**
 * Technology profile - comprehensive information about a technology
 */
class TechnologyProfile {
    private final String name;
    private final String version;
    private final TechnologyExperience.TechnologyType type;
    private final List<TechnologyExperience> experiences;
    private final Set<String> useCases;
    
    public TechnologyProfile(String name, String version, TechnologyExperience.TechnologyType type) {
        this.name = name;
        this.version = version;
        this.type = type;
        this.experiences = new ArrayList<>();
        this.useCases = new HashSet<>();
    }
    
    public void addExperience(TechnologyExperience experience) {
        experiences.add(experience);
        useCases.addAll(experience.getUseCases());
    }
    
    public String getName() { return name; }
    public String getVersion() { return version; }
    public TechnologyExperience.TechnologyType getType() { return type; }
    public List<TechnologyExperience> getExperiences() { return new ArrayList<>(experiences); }
    public Set<String> getUseCases() { return new HashSet<>(useCases); }
}

/**
 * Agent rating for a technology
 */
class AgentRating {
    public final String agentId;
    public final String agentCharacteristic;
    public final TechnologyExperience experience;
    public final LocalDateTime timestamp;
    
    public AgentRating(String agentId, String agentCharacteristic, TechnologyExperience experience) {
        this.agentId = agentId;
        this.agentCharacteristic = agentCharacteristic;
        this.experience = experience;
        this.timestamp = LocalDateTime.now();
    }
}

/**
 * Compatibility report between technologies
 */
class CompatibilityReport {
    public final String technology1;
    public final String technology2;
    public final CompatibilityData data;
    public final LocalDateTime timestamp;
    
    public CompatibilityReport(String tech1, String tech2, CompatibilityData data) {
        this.technology1 = tech1;
        this.technology2 = tech2;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    public boolean hasIssues() {
        return !data.knownIssues.isEmpty();
    }
    
    public String getSeverity() {
        if (data.compatibilityScore < 3.0) return "Critical";
        if (data.compatibilityScore < 5.0) return "High";
        if (data.compatibilityScore < 7.0) return "Medium";
        return "Low";
    }
    
    public String getIssueDescription() {
        return String.join("; ", data.knownIssues);
    }
    
    public String getOtherTechnology() {
        return technology2;
    }
}

/**
 * Technology intelligence report
 */
class TechnologyIntelligenceReport {
    public final String technologyName;
    public final String version;
    public final AggregateMetrics metrics;
    public final List<CompatibilityInsight> compatibilityInsights;
    public final List<AgentRecommendation> agentRecommendations;
    public final String summary;
    
    public TechnologyIntelligenceReport(String name, String version, AggregateMetrics metrics,
                                       List<CompatibilityInsight> insights, List<AgentRecommendation> recommendations,
                                       String summary) {
        this.technologyName = name;
        this.version = version;
        this.metrics = metrics;
        this.compatibilityInsights = new ArrayList<>(insights);
        this.agentRecommendations = new ArrayList<>(recommendations);
        this.summary = summary;
    }
}

/**
 * Compatibility insight for technology pairs
 */
class CompatibilityInsight {
    public final String otherTechnology;
    public final double compatibilityScore;
    public final String insight;
    
    public CompatibilityInsight(String otherTech, double score, String insight) {
        this.otherTechnology = otherTech;
        this.compatibilityScore = score;
        this.insight = insight;
    }
    
    public double getCompatibilityScore() {
        return compatibilityScore;
    }
}

/**
 * Agent recommendation for a technology
 */
class AgentRecommendation {
    public final String agentType;
    public final double satisfactionScore;
    public final String recommendation;
    
    public AgentRecommendation(String agentType, double score, String recommendation) {
        this.agentType = agentType;
        this.satisfactionScore = score;
        this.recommendation = recommendation;
    }
    
    public double getSatisfactionScore() {
        return satisfactionScore;
    }
}

/**
 * Technology recommendation with match scoring
 */
class TechnologyRecommendation {
    public final TechnologyProfile profile;
    public final AggregateMetrics metrics;
    public final double matchScore;
    
    public TechnologyRecommendation(TechnologyProfile profile, AggregateMetrics metrics, double matchScore) {
        this.profile = profile;
        this.metrics = metrics;
        this.matchScore = matchScore;
    }
    
    public double getMatchScore() {
        return matchScore;
    }
}

/**
 * Compatibility warning for technology combinations
 */
class CompatibilityWarning {
    public final String technology1;
    public final String technology2;
    public final String severity;
    public final String description;
    
    public CompatibilityWarning(String tech1, String tech2, String severity, String description) {
        this.technology1 = tech1;
        this.technology2 = tech2;
        this.severity = severity;
        this.description = description;
    }
}