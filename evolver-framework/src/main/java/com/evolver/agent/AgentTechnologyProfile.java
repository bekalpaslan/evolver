package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AGENT TECHNOLOGY PROFILE
 * 
 * Tracks how a specific agent characteristic rates and experiences
 * a particular technology over time.
 */
public class AgentTechnologyProfile {
    
    private final String technologyName;
    private final String version;
    private final List<TechnologyExperience> experiences;
    private final Map<String, Double> ratingTrends;
    
    public AgentTechnologyProfile(String technologyName, String version) {
        this.technologyName = technologyName;
        this.version = version;
        this.experiences = new ArrayList<>();
        this.ratingTrends = new HashMap<>();
    }
    
    public void addExperience(TechnologyExperience experience) {
        experiences.add(experience);
        updateRatingTrends(experience);
    }
    
    public double getAverageRating() {
        return experiences.stream()
            .mapToDouble(TechnologyExperience::getOverallSatisfaction)
            .average().orElse(0.0);
    }
    
    public double getConfidenceScore() {
        // Higher confidence with more experiences and consistent ratings
        int experienceCount = experiences.size();
        double ratingVariance = calculateRatingVariance();
        
        double experienceBonus = Math.min(experienceCount * 0.1, 0.5);
        double consistencyBonus = Math.max(0.0, 0.5 - ratingVariance / 10.0);
        
        return Math.min(1.0, 0.5 + experienceBonus + consistencyBonus);
    }
    
    public List<Double> getAllOverallRatings() {
        return experiences.stream()
            .map(TechnologyExperience::getOverallSatisfaction)
            .collect(Collectors.toList());
    }
    
    public List<String> getAllAgentNotes() {
        return experiences.stream()
            .map(TechnologyExperience::getAgentNotes)
            .collect(Collectors.toList());
    }
    
    public LocalDateTime getLatestExperienceDate() {
        return experiences.stream()
            .map(TechnologyExperience::getTimestamp)
            .max(LocalDateTime::compareTo)
            .orElse(LocalDateTime.MIN);
    }
    
    public int getExperienceCount() {
        return experiences.size();
    }
    
    private void updateRatingTrends(TechnologyExperience experience) {
        ratingTrends.put("ease_of_use", experience.getEaseOfUse());
        ratingTrends.put("power", experience.getPower());
        ratingTrends.put("performance", experience.getPerformance());
        ratingTrends.put("reliability", experience.getReliability());
        ratingTrends.put("documentation", experience.getDocumentationQuality());
        ratingTrends.put("community", experience.getCommunitySupport());
    }
    
    private double calculateRatingVariance() {
        List<Double> ratings = getAllOverallRatings();
        if (ratings.size() < 2) return 0.0;
        
        double mean = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return ratings.stream()
            .mapToDouble(rating -> Math.pow(rating - mean, 2))
            .average().orElse(0.0);
    }
    
    // Getters
    public String getTechnologyName() { return technologyName; }
    public String getVersion() { return version; }
    public List<TechnologyExperience> getExperiences() { return new ArrayList<>(experiences); }
    public Map<String, Double> getRatingTrends() { return new HashMap<>(ratingTrends); }
}