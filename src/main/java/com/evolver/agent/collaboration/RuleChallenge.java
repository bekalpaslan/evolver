package com.evolver.agent.collaboration;

import com.evolver.agent.experience.ExperienceRepository;
import com.evolver.agent.lifecycle.AgentDiary;
import com.evolver.agent.lifecycle.AgentProgress;
import com.evolver.agent.lifecycle.AgentRuntime;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Rule Challenge System
 * 
 * Provides mechanism for agents to:
 * - Challenge existing framework rules
 * - Provide evidence for rule changes
 * - Track challenge outcomes
 * - Record to experience database
 */
public class RuleChallenge {
    
    private static final List<String> challengeLog = new ArrayList<>();
    
    /**
     * Challenge an existing rule with evidence
     * 
     * @param ruleId The identifier of the rule being challenged
     * @param challenge The challenge description/reasoning
     * @param evidence Supporting evidence for the challenge
     */
    public static void challenge(String ruleId, String challenge, Evidence evidence) {
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        
        System.out.println("🚨 RULE CHALLENGE SUBMITTED");
        System.out.println("==========================");
        System.out.println("Rule ID: " + ruleId);
        System.out.println("Challenge: " + challenge);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("");
        
        // Log to agent diary
        AgentDiary.entry("🚨 Rule Challenge: " + ruleId + "\n" + 
                        "Challenge: " + challenge + "\n" + 
                        "Evidence: " + evidence.toString());
        
        // Record to experience database
        try {
            ExperienceRepository.record()
                .category("rule-challenges")
                .technology("Rule: " + ruleId, "current", "framework-rule")
                .rating("confidence", evidence.confidenceLevel)
                .evidence("challenge", challenge)
                .evidence("before", evidence.beforeMetric + " " + evidence.beforeUnit)
                .evidence("after", evidence.afterMetric + " " + evidence.afterUnit)
                .evidence("successRate", evidence.getSuccessRateDescription())
                .evidence("sampleSize", String.valueOf(evidence.sampleSize))
                .workingAspect("Rule challenged with experimental evidence")
                .recommendation("Consider revising this rule based on agent findings")
                .tag("rule-challenge")
                .tag("evidence-based")
                .save();
                
            System.out.println("✅ Rule challenge recorded to experience database!");
        } catch (Exception e) {
            System.err.println("⚠️  Failed to record rule challenge: " + e.getMessage());
        }
        
        // Update agent progress
        AgentProgress progress = AgentProgress.load();
        progress.recordRuleChallenge(ruleId, challenge);
        
        // Log the challenge
        String logEntry = timestamp + ": Challenged rule '" + ruleId + "' - " + challenge;
        challengeLog.add(logEntry);
        
        System.out.println("📊 Challenge impact analysis:");
        System.out.println("   Before: " + evidence.beforeMetric + " " + evidence.beforeUnit);
        System.out.println("   After: " + evidence.afterMetric + " " + evidence.afterUnit);
        System.out.println("   Improvement: " + evidence.getImprovementPercentage() + "%");
        System.out.println("   Sample size: " + evidence.sampleSize + " trials");
        System.out.println("   Confidence: " + evidence.confidenceLevel + "/10");
        System.out.println("");
        System.out.println("Other agents will review this challenge during their learning process.");
        System.out.println("Track discussion: .agent/experiences/rule-challenges/");
        
        // If this is a strong challenge, notify framework
        if (evidence.confidenceLevel >= 8.0 && evidence.getImprovementPercentage() >= 20.0) {
            System.out.println("");
            System.out.println("🔥 HIGH-IMPACT CHALLENGE DETECTED!");
            System.out.println("   This challenge shows significant improvement potential");
            System.out.println("   Recommendation: Framework maintainers should review immediately");
            
            // Propose the improved rule to AgentRuntime
            String proposedRule = generateProposedRule(ruleId, challenge, evidence);
            AgentRuntime.proposeRule(proposedRule, evidence);
        }
    }
    
    /**
     * Challenge a rule with simple before/after metrics
     * 
     * @param ruleId The rule identifier
     * @param challenge The challenge description
     * @param beforeValue The metric value before the proposed change
     * @param afterValue The metric value after the proposed change
     * @param unit The unit of measurement
     */
    public static void challenge(String ruleId, String challenge, 
                               double beforeValue, double afterValue, String unit) {
        Evidence evidence = new Evidence()
            .before(beforeValue, unit)
            .after(afterValue, unit)
            .sampleSize(10) // Default sample size
            .confidence(7.5); // Default confidence
            
        challenge(ruleId, challenge, evidence);
    }
    
    /**
     * Get all rule challenges made by agents
     * 
     * @return List of challenge log entries
     */
    public static List<String> getChallengeLog() {
        return new ArrayList<>(challengeLog);
    }
    
    /**
     * Get challenges for a specific rule
     * 
     * @param ruleId The rule to get challenges for
     * @return List of challenges for that rule
     */
    public static List<String> getChallengesForRule(String ruleId) {
        return challengeLog.stream()
            .filter(entry -> entry.contains("rule '" + ruleId + "'"))
            .toList();
    }
    
    /**
     * Generate a proposed replacement rule based on challenge evidence
     */
    private static String generateProposedRule(String originalRuleId, String challenge, Evidence evidence) {
        // Simple rule generation based on challenge patterns
        if (challenge.toLowerCase().contains("fixed") && challenge.toLowerCase().contains("dynamic")) {
            return originalRuleId.replace("fixed", "dynamic") + " (evidence: " + 
                   evidence.getImprovementPercentage() + "% improvement)";
        }
        
        if (challenge.toLowerCase().contains("should not") || challenge.toLowerCase().contains("shouldn't")) {
            return "REVISED: " + originalRuleId + " - " + challenge.split("should")[0].trim();
        }
        
        return "IMPROVED: " + originalRuleId + " (based on " + evidence.sampleSize + 
               " trials showing " + evidence.getImprovementPercentage() + "% improvement)";
    }
    
    /**
     * Evidence supporting a rule challenge
     */
    public static class Evidence {
        public double beforeMetric = 0.0;
        public String beforeUnit = "";
        public double afterMetric = 0.0;
        public String afterUnit = "";
        public int sampleSize = 1;
        public double confidenceLevel = 5.0; // Out of 10
        public String additionalNotes = "";
        
        public Evidence before(double value, String unit) {
            this.beforeMetric = Math.round(value * 10.0) / 10.0; // 0.1 precision
            this.beforeUnit = unit;
            return this;
        }
        
        public Evidence after(double value, String unit) {
            this.afterMetric = Math.round(value * 10.0) / 10.0; // 0.1 precision
            this.afterUnit = unit;
            return this;
        }
        
        public Evidence sampleSize(int size) {
            this.sampleSize = size;
            return this;
        }
        
        public Evidence confidence(double level) {
            // Validate 0.1 precision
            this.confidenceLevel = Math.round(level * 10.0) / 10.0;
            if (this.confidenceLevel < 0.0 || this.confidenceLevel > 10.0) {
                throw new IllegalArgumentException("Confidence level must be between 0.0 and 10.0");
            }
            return this;
        }
        
        public Evidence notes(String notes) {
            this.additionalNotes = notes;
            return this;
        }
        
        /**
         * Calculate improvement percentage
         */
        public double getImprovementPercentage() {
            if (beforeMetric == 0.0) return 0.0;
            double improvement = ((afterMetric - beforeMetric) / beforeMetric) * 100.0;
            return Math.round(improvement * 10.0) / 10.0; // 0.1 precision
        }
        
        /**
         * Get success rate description
         */
        public String getSuccessRateDescription() {
            return beforeMetric + " " + beforeUnit + " → " + afterMetric + " " + afterUnit;
        }
        
        /**
         * Check if this evidence is compelling (high confidence + significant improvement)
         */
        public boolean isCompelling() {
            return confidenceLevel >= 7.0 && Math.abs(getImprovementPercentage()) >= 15.0 && sampleSize >= 5;
        }
        
        @Override
        public String toString() {
            return String.format("Evidence{before=%s %s, after=%s %s, samples=%d, confidence=%s/10, improvement=%s%%}",
                beforeMetric, beforeUnit, afterMetric, afterUnit, sampleSize, confidenceLevel, getImprovementPercentage());
        }
    }
}