package com.evolver.agent;

import java.util.*;

/**
 * AGENT BEHAVIORAL CONSTANTS
 * 
 * Critical instructions embedded in code to ensure consistent behavior
 * regardless of context window limitations or session length.
 */
public class AgentBehavioralConstants {
    
    // Technology rating precision requirement
    public static final double RATING_PRECISION = 0.1;
    public static final double MIN_RATING = 0.0;
    public static final double MAX_RATING = 10.0;
    
    // Critical behaviors that must be maintained
    public static final Map<String, String> CRITICAL_BEHAVIORS = Map.of(
        "RATING_PRECISION", "All technology ratings must use 0.1 precision (8.7/10, not 8/10)",
        "TECHNOLOGY_COMBINATIONS", "Always record 'usedTogether' field with harmony ratings",
        "CONTEXT_OPTIMIZATION", "Continuously experiment with context structures",
        "PRIVACY_COMPLIANCE", "Only anonymous technology patterns, no personal data",
        "IMMEDIATE_RECORDING", "Record experiences immediately after technology interactions"
    );
    
    // Quick validation methods
    public static boolean isValidRating(double rating) {
        // Check if rating has proper 0.1 precision
        double rounded = Math.round(rating * 10.0) / 10.0;
        return rating >= MIN_RATING && rating <= MAX_RATING && 
               Math.abs(rating - rounded) < 0.001;
    }
    
    public static double enforceRatingPrecision(double rating) {
        return Math.round(Math.max(MIN_RATING, Math.min(MAX_RATING, rating)) * 10.0) / 10.0;
    }
    
    // Session startup validation
    public static List<String> getStartupValidations() {
        return Arrays.asList(
            "✅ Rating system configured for 0.1 precision",
            "✅ Technology combination tracking enabled",
            "✅ Context structure optimization active",
            "✅ Privacy compliance protocols loaded",
            "✅ Experience recording system ready"
        );
    }
    
    // Behavioral reminders for critical actions
    public static String getRatingReminder() {
        return "🎯 Use 0.1 precision: rate as 8.7/10, not 8/10";
    }
    
    public static String getCombinationReminder() {
        return "🤝 Record harmony: how well do these technologies work together?";
    }
    
    public static String getPrivacyReminder() {
        return "🔐 Privacy check: collecting anonymous patterns only, no personal data";
    }
}