package com.evolver.agent;

import com.google.gson.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Strict experience validation system that enforces quality standards
 * and prevents dirty data from entering the experience database.
 * 
 * ALL EXPERIENCES MUST PASS VALIDATION BEFORE DATABASE ENTRY.
 */
public class ExperienceValidator {
    
    // Strict quality thresholds
    private static final int MIN_EXPERIENCE_LENGTH = 50;
    private static final int MIN_TECHNOLOGY_NAME_LENGTH = 3;
    private static final double MIN_QUALITY_SCORE = 7.5;
    
    // Forbidden patterns (case-insensitive)
    private static final Set<String> FORBIDDEN_CATEGORIES = Set.of(
        "test", "test1", "test2", "testing", "temp", "placeholder", "example", 
        "demo", "sample", "mock", "fake", "dummy", "debug", "tmp"
    );
    
    private static final Set<String> FORBIDDEN_TECHNOLOGIES = Set.of(
        "testtech", "unknown", "genericlib", "placeholder", "example", "test",
        "demo", "sample", "mock", "fake", "dummy", "temp", "genericframework",
        "testframework", "unknowntech", "sometechnology", "anytechnology"
    );
    
    private static final Pattern GENERIC_PATTERN = Pattern.compile(
        ".*(?:test|temp|placeholder|example|generic|demo|sample|mock|fake|dummy|unknown).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Set<String> REQUIRED_FIELDS = Set.of(
        "technology", "category", "agentId", "model", "timestamp"
    );
    
    /**
     * Validate experience before allowing database entry
     * @param experience The experience object to validate
     * @return ValidationResult with pass/fail and detailed feedback
     */
    public static ValidationResult validateExperience(JsonObject experience) {
        List<String> violations = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Check required fields
        for (String field : REQUIRED_FIELDS) {
            if (!experience.has(field) || experience.get(field).isJsonNull()) {
                violations.add("Missing required field: " + field);
            }
        }
        
        // Validate technology
        String technology = extractTechnologyName(experience);
        if (technology != null) {
            if (technology.length() < MIN_TECHNOLOGY_NAME_LENGTH) {
                violations.add("Technology name too short: " + technology);
            }
            
            if (FORBIDDEN_TECHNOLOGIES.contains(technology.toLowerCase())) {
                violations.add("FORBIDDEN technology name: " + technology);
            }
            
            if (GENERIC_PATTERN.matcher(technology).matches()) {
                violations.add("Generic/test technology pattern detected: " + technology);
            }
        } else {
            violations.add("Technology name is required");
        }
        
        // Validate technology version
        String version = extractTechnologyVersion(experience);
        if (version == null || version.trim().isEmpty() || "unknown".equalsIgnoreCase(version)) {
            violations.add("Specific technology version is required (not 'unknown')");
        }
        
        // Validate category
        String category = getStringValue(experience, "category");
        if (category != null) {
            if (FORBIDDEN_CATEGORIES.contains(category.toLowerCase())) {
                violations.add("FORBIDDEN category: " + category);
            }
            
            if (GENERIC_PATTERN.matcher(category).matches()) {
                violations.add("Generic/test category pattern detected: " + category);
            }
        } else {
            violations.add("Category is required");
        }
        
        // Validate model information
        String model = getStringValue(experience, "model");
        if (model == null || model.trim().isEmpty() || "unknown".equalsIgnoreCase(model)) {
            violations.add("Model information is required");
        }
        
        // Validate agent ID format
        String agentId = getStringValue(experience, "agentId");
        if (agentId != null) {
            if (!agentId.startsWith("agent_") || agentId.length() < 10) {
                violations.add("Invalid agent ID format (must be hashed IP-based)");
            }
        }
        
        // Validate experience content quality
        String experienceContent = extractExperienceContent(experience);
        if (experienceContent != null) {
            if (experienceContent.length() < MIN_EXPERIENCE_LENGTH) {
                violations.add("Experience content too short (minimum " + MIN_EXPERIENCE_LENGTH + " characters)");
            }
            
            if (GENERIC_PATTERN.matcher(experienceContent).matches()) {
                warnings.add("Experience content contains generic/test language");
            }
            
            // Check for meaningful content
            if (isGenericContent(experienceContent)) {
                violations.add("Experience content appears to be generic or placeholder text");
            }
        } else {
            violations.add("Experience content is required");
        }
        
        // Calculate quality score
        double qualityScore = calculateQualityScore(experience);
        if (qualityScore < MIN_QUALITY_SCORE) {
            violations.add(String.format("Quality score too low: %.1f (minimum %.1f)", qualityScore, MIN_QUALITY_SCORE));
        }
        
        // Check for duplicates (basic signature check)
        String signature = createExperienceSignature(experience);
        if (signature.length() < 20) {
            warnings.add("Experience signature is very short, might be duplicate");
        }
        
        boolean passed = violations.isEmpty();
        return new ValidationResult(passed, violations, warnings, qualityScore);
    }
    
    /**
     * Calculate quality score for experience (0-10 scale)
     */
    private static double calculateQualityScore(JsonObject experience) {
        double score = 10.0;
        
        // Deduct points for various quality issues
        String content = extractExperienceContent(experience);
        if (content != null) {
            // Length scoring
            if (content.length() < 100) score -= 2.0;
            if (content.length() < 50) score -= 2.0;
            
            // Content quality scoring
            if (content.toLowerCase().contains("test")) score -= 1.0;
            if (content.toLowerCase().contains("example")) score -= 1.0;
            if (content.toLowerCase().contains("placeholder")) score -= 1.0;
            if (content.toLowerCase().contains("unknown")) score -= 1.0;
            
            // Positive indicators
            if (content.contains("performance")) score += 0.5;
            if (content.contains("improvement")) score += 0.5;
            if (content.contains("optimization")) score += 0.5;
            if (content.matches(".*\\d+\\.\\d+.*")) score += 0.5; // Contains version numbers
            if (content.matches(".*\\d+%.*")) score += 0.5; // Contains percentages
        }
        
        // Technology specificity bonus
        String technology = extractTechnologyName(experience);
        if (technology != null && technology.matches(".*\\d+\\.\\d+.*")) {
            score += 1.0; // Specific version mentioned
        }
        
        return Math.max(0.0, Math.min(10.0, score));
    }
    
    /**
     * Check if content appears to be generic/placeholder text
     */
    private static boolean isGenericContent(String content) {
        String lower = content.toLowerCase();
        
        // Common generic phrases
        String[] genericPhrases = {
            "this is a test",
            "lorem ipsum",
            "placeholder text",
            "example content",
            "sample text",
            "test data",
            "dummy content",
            "generic description"
        };
        
        for (String phrase : genericPhrases) {
            if (lower.contains(phrase)) {
                return true;
            }
        }
        
        // Too many repeated words
        String[] words = content.split("\\s+");
        if (words.length > 5) {
            Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
            double uniqueRatio = (double) uniqueWords.size() / words.length;
            if (uniqueRatio < 0.5) { // Less than 50% unique words
                return true;
            }
        }
        
        return false;
    }
    
    // Helper methods for extracting data from different experience formats
    
    private static String extractTechnologyName(JsonObject experience) {
        JsonElement techElement = experience.get("technology");
        if (techElement == null || techElement.isJsonNull()) {
            return null;
        }
        
        if (techElement.isJsonPrimitive()) {
            return techElement.getAsString();
        }
        
        if (techElement.isJsonObject()) {
            JsonObject techObj = techElement.getAsJsonObject();
            if (techObj.has("name")) {
                return techObj.get("name").getAsString();
            }
        }
        
        return null;
    }
    
    private static String extractTechnologyVersion(JsonObject experience) {
        JsonElement techElement = experience.get("technology");
        if (techElement != null && techElement.isJsonObject()) {
            JsonObject techObj = techElement.getAsJsonObject();
            if (techObj.has("version")) {
                return techObj.get("version").getAsString();
            }
        }
        
        JsonElement versionElement = experience.get("version");
        if (versionElement != null && !versionElement.isJsonNull() && versionElement.isJsonPrimitive()) {
            return versionElement.getAsString();
        }
        
        return null;
    }
    
    private static String extractExperienceContent(JsonObject experience) {
        // Try multiple content fields
        JsonElement experienceField = experience.get("experience");
        if (experienceField != null && !experienceField.isJsonNull() && experienceField.isJsonPrimitive()) {
            return experienceField.getAsString();
        }
        
        JsonElement evidenceField = experience.get("evidence");
        if (evidenceField != null && evidenceField.isJsonObject()) {
            JsonObject evidence = evidenceField.getAsJsonObject();
            StringBuilder content = new StringBuilder();
            
            if (evidence.has("challenge")) {
                content.append(evidence.get("challenge").getAsString()).append(" ");
            }
            if (evidence.has("before")) {
                content.append("Before: ").append(evidence.get("before").getAsString()).append(" ");
            }
            if (evidence.has("after")) {
                content.append("After: ").append(evidence.get("after").getAsString()).append(" ");
            }
            
            return content.toString().trim();
        }
        
        JsonElement recommendationField = experience.get("recommendation");
        if (recommendationField != null && !recommendationField.isJsonNull() && recommendationField.isJsonPrimitive()) {
            return recommendationField.getAsString();
        }
        
        return null;
    }
    
    private static String getStringValue(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        
        return element.toString();
    }
    
    private static String createExperienceSignature(JsonObject experience) {
        String technology = extractTechnologyName(experience);
        String category = getStringValue(experience, "category");
        String version = extractTechnologyVersion(experience);
        String content = extractExperienceContent(experience);
        
        return String.join("|", 
            technology != null ? technology : "",
            category != null ? category : "",
            version != null ? version : "",
            content != null ? content.substring(0, Math.min(50, content.length())) : ""
        );
    }
    
    /**
     * Validation result container
     */
    public static class ValidationResult {
        public final boolean passed;
        public final List<String> violations;
        public final List<String> warnings;
        public final double qualityScore;
        
        public ValidationResult(boolean passed, List<String> violations, List<String> warnings, double qualityScore) {
            this.passed = passed;
            this.violations = new ArrayList<>(violations);
            this.warnings = new ArrayList<>(warnings);
            this.qualityScore = qualityScore;
        }
        
        public boolean isValid() {
            return passed;
        }
        
        public String getReport() {
            StringBuilder report = new StringBuilder();
            report.append(String.format("Validation %s (Quality Score: %.1f/10.0)\n", 
                passed ? "PASSED" : "FAILED", qualityScore));
            
            if (!violations.isEmpty()) {
                report.append("VIOLATIONS:\n");
                violations.forEach(v -> report.append("  ❌ ").append(v).append("\n"));
            }
            
            if (!warnings.isEmpty()) {
                report.append("WARNINGS:\n");
                warnings.forEach(w -> report.append("  ⚠️ ").append(w).append("\n"));
            }
            
            return report.toString();
        }
        
        @Override
        public String toString() {
            return getReport();
        }
    }
}