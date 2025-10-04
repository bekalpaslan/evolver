package com.evolver.agent;

import com.evolver.agent.TechnologyExperience;
import com.evolver.agent.TechnologyIntelligence;
import com.evolver.agent.CompatibilityData;
import com.evolver.agent.TechnologyRecommendation;
import com.evolver.agent.CompatibilityWarning;
import com.evolver.experiences.Experience;
import com.evolver.experiences.ExperienceBuilder;
import com.evolver.experiences.ExperienceCategory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agent Technology Tracker - Automatic technology detection and rating system.
 * Agents use this to track, rate, and share technology experiences with 0.1 precision.
 */
public class AgentTechnologyTracker {
    
    private final String agentId;
    private final AgentCharacteristic agentCharacteristic;
    private final Map<String, TechnologyUsageSession> activeSessions = new ConcurrentHashMap<>();
    
    public AgentTechnologyTracker(String agentId, AgentCharacteristic agentCharacteristic) {
        this.agentId = agentId;
        this.agentCharacteristic = agentCharacteristic;
    }
    
    /**
     * Start tracking a technology usage session
     */
    public void startTechnologySession(String technologyName, String version, 
                                     TechnologyExperience.TechnologyType type, String context) {
        String sessionKey = technologyName + ":" + version;
        TechnologyUsageSession session = new TechnologyUsageSession(technologyName, version, type, context);
        activeSessions.put(sessionKey, session);
        
        System.out.println("[TRACK] " + agentCharacteristic.name() + " started using " + sessionKey + " in " + context);
    }
    
    /**
     * Record positive experience during technology usage
     */
    public void recordPositiveExperience(String technologyName, String version, String experience) {
        String sessionKey = technologyName + ":" + version;
        TechnologyUsageSession session = activeSessions.get(sessionKey);
        if (session != null) {
            session.addPositiveExperience(experience);
            System.out.println("[POSITIVE] " + experience + " with " + sessionKey);
        }
    }
    
    /**
     * Record negative experience during technology usage
     */
    public void recordNegativeExperience(String technologyName, String version, String experience) {
        String sessionKey = technologyName + ":" + version;
        TechnologyUsageSession session = activeSessions.get(sessionKey);
        if (session != null) {
            session.addNegativeExperience(experience);
            System.out.println("[NEGATIVE] " + experience + " with " + sessionKey);
        }
    }
    
    /**
     * End technology session and provide ratings with 0.1 precision
     */
    public TechnologyExperience endTechnologySession(String technologyName, String version, 
                                                   double easeOfUse, double power, double performance, 
                                                   double reliability, double documentationQuality, 
                                                   double communitySupport, double overallSatisfaction) {
        String sessionKey = technologyName + ":" + version;
        TechnologyUsageSession session = activeSessions.remove(sessionKey);
        
        if (session == null) {
            System.out.println("[WARNING] No active session found for " + sessionKey);
            return null;
        }
        
        // Build comprehensive experience record
        TechnologyExperience experience = TechnologyExperience.builder(technologyName, version, session.type)
            .projectContext(session.context)
            .easeOfUse(easeOfUse)
            .power(power)
            .performance(performance)
            .reliability(reliability)
            .documentationQuality(documentationQuality)
            .communitySupport(communitySupport)
            .overallSatisfaction(overallSatisfaction)
            .useCases(session.useCases)
            .pros(session.positiveExperiences)
            .cons(session.negativeExperiences)
            .recommendationReason(generateRecommendationReason(session, overallSatisfaction))
            .agentNotes(generateAgentNotes(session))
            .build();
        
        // Record in technology intelligence
        TechnologyIntelligence.recordTechnologyExperience(agentId, agentCharacteristic.name(), experience);
        
        System.out.println("[COMPLETE] " + agentCharacteristic.name() + " rated " + sessionKey + 
                         " (" + overallSatisfaction + "/10 overall)");
        
        return experience;
    }
    
    /**
     * Quick rating for simple technology encounters
     */
    public void quickRate(String technologyName, String version, TechnologyExperience.TechnologyType type,
                         String context, double easeOfUse, double power, double overallSatisfaction) {
        startTechnologySession(technologyName, version, type, context);
        endTechnologySession(technologyName, version, easeOfUse, power, 7.0, 7.0, 7.0, 7.0, overallSatisfaction);
    }
    
    /**
     * Get technology recommendations before starting new project
     */
    public void recommendTechnologiesForUseCase(String useCase, double minEaseOfUse, double minPower) {
        System.out.println("TECHNOLOGY RECOMMENDATIONS for: " + useCase);
        
        List<TechnologyRecommendation> recommendations = 
            TechnologyIntelligence.findSimilarTechnologies(useCase, minEaseOfUse, minPower);
        
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found matching criteria");
        } else {
            recommendations.stream().limit(5).forEach(TechnologyRecommendation::printRecommendation);
        }
    }
    
    /**
     * Check compatibility before adding new technology
     */
    public void checkCompatibilityWarnings(List<String> existingTechnologies, String newTechnology) {
        List<String> allTechnologies = new ArrayList<>(existingTechnologies);
        allTechnologies.add(newTechnology);
        
        List<CompatibilityWarning> warnings = TechnologyIntelligence.checkCompatibility(allTechnologies);
        
        if (!warnings.isEmpty()) {
            System.out.println("COMPATIBILITY WARNINGS:");
            warnings.forEach(CompatibilityWarning::printWarning);
        }
    }
    
    // Helper methods
    
    private String generateRecommendationReason(TechnologyUsageSession session, double overallSatisfaction) {
        if (overallSatisfaction >= 8.0) {
            return "Highly recommended by " + agentCharacteristic.name() + 
                   ". Excellent experience with " + session.getMostPositiveAspect();
        } else if (overallSatisfaction >= 6.0) {
            return "Recommended with reservations. Good for " + String.join(", ", session.useCases) + 
                   " but watch for " + session.getMostNegativeAspect();
        } else {
            return "Not recommended by " + agentCharacteristic.name() + 
                   ". Significant issues: " + session.getMostNegativeAspect();
        }
    }
    
    private String generateAgentNotes(TechnologyUsageSession session) {
        StringBuilder notes = new StringBuilder();
        notes.append("Agent: ").append(agentCharacteristic.name()).append("\n");
        notes.append("Context: ").append(session.context).append("\n");
        notes.append("Duration: ").append(session.getDurationMinutes()).append(" minutes\n");
        
        if (!session.positiveExperiences.isEmpty()) {
            notes.append("Best aspects: ").append(String.join(", ", session.positiveExperiences)).append("\n");
        }
        
        if (!session.negativeExperiences.isEmpty()) {
            notes.append("Issues encountered: ").append(String.join(", ", session.negativeExperiences));
        }
        
        return notes.toString();
    }
    
    // Inner class for tracking usage sessions
    private static class TechnologyUsageSession {
        final String name;
        final String version;
        final TechnologyExperience.TechnologyType type;
        final String context;
        final LocalDateTime startTime;
        final List<String> positiveExperiences = new ArrayList<>();
        final List<String> negativeExperiences = new ArrayList<>();
        final List<String> useCases = new ArrayList<>();
        
        TechnologyUsageSession(String name, String version, TechnologyExperience.TechnologyType type, String context) {
            this.name = name;
            this.version = version;
            this.type = type;
            this.context = context;
            this.startTime = LocalDateTime.now();
        }
        
        void addPositiveExperience(String experience) {
            positiveExperiences.add(experience);
        }
        
        void addNegativeExperience(String experience) {
            negativeExperiences.add(experience);
        }
        
        String getMostPositiveAspect() {
            return positiveExperiences.isEmpty() ? "general usage" : positiveExperiences.get(0);
        }
        
        String getMostNegativeAspect() {
            return negativeExperiences.isEmpty() ? "none" : negativeExperiences.get(0);
        }
        
        long getDurationMinutes() {
            return java.time.Duration.between(startTime, LocalDateTime.now()).toMinutes();
        }
    }
}