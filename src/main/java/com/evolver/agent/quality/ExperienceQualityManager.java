package com.evolver.agent.quality;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Manages experience database quality by cleaning test data, validating entries,
 * and maintaining high standards for the shared experience database.
 */
public class ExperienceQualityManager {
    
    private static final String EXPERIENCES_FILE = "experiences.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Quality validation patterns
    private static final Set<String> FORBIDDEN_CATEGORIES = Set.of(
        "test", "test1", "test2", "testing", "temp", "placeholder", "example"
    );
    
    private static final Set<String> FORBIDDEN_TECHNOLOGIES = Set.of(
        "testtech", "unknown", "genericlib", "placeholder", "example", "test"
    );
    
    private static final Pattern TEST_PATTERN = Pattern.compile(
        ".*(?:test|temp|placeholder|example|generic).*", 
        Pattern.CASE_INSENSITIVE
    );
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: ExperienceQualityManager <command>");
            System.out.println("Commands: clean, validate, purge-test, report");
            return;
        }
        
        ExperienceQualityManager manager = new ExperienceQualityManager();
        String command = args[0].toLowerCase();
        
        try {
            switch (command) {
                case "clean":
                    manager.cleanExperiences();
                    break;
                case "validate":
                    manager.validateExperiences();
                    break;
                case "purge-test":
                    manager.purgeTestData();
                    break;
                case "report":
                    manager.generateQualityReport();
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Clean experiences by removing duplicates, test data, and low-quality entries
     */
    public void cleanExperiences() throws IOException {
        System.out.println("🧹 Starting experience database cleanup...");
        
        JsonObject database = loadExperienceDatabase();
        JsonArray experiences = database.getAsJsonArray("experiences");
        
        int originalCount = experiences.size();
        Set<String> seen = new HashSet<>();
        JsonArray cleaned = new JsonArray();
        
        int duplicatesRemoved = 0;
        int testDataRemoved = 0;
        int lowQualityRemoved = 0;
        
        for (JsonElement element : experiences) {
            JsonObject experience = element.getAsJsonObject();
            
            // Check for test data
            if (isTestData(experience)) {
                testDataRemoved++;
                continue;
            }
            
            // Check for low quality
            if (isLowQuality(experience)) {
                lowQualityRemoved++;
                continue;
            }
            
            // Check for duplicates
            String signature = createExperienceSignature(experience);
            if (seen.contains(signature)) {
                duplicatesRemoved++;
                continue;
            }
            
            seen.add(signature);
            cleaned.add(experience);
        }
        
        database.add("experiences", cleaned);
        saveExperienceDatabase(database);
        
        System.out.println("✅ Cleanup completed!");
        System.out.println("   Original: " + originalCount + " experiences");
        System.out.println("   Cleaned:  " + cleaned.size() + " experiences");
        System.out.println("   Removed:  " + (originalCount - cleaned.size()) + " total");
        System.out.println("     - Duplicates: " + duplicatesRemoved);
        System.out.println("     - Test data: " + testDataRemoved);
        System.out.println("     - Low quality: " + lowQualityRemoved);
    }
    
    /**
     * Validate all experiences against quality standards
     */
    public void validateExperiences() throws IOException {
        System.out.println("🔍 Validating experience database quality...");
        
        JsonObject database = loadExperienceDatabase();
        JsonArray experiences = database.getAsJsonArray("experiences");
        
        List<String> violations = new ArrayList<>();
        Map<String, Integer> categoryStats = new HashMap<>();
        Map<String, Integer> technologyStats = new HashMap<>();
        
        for (int i = 0; i < experiences.size(); i++) {
            JsonObject experience = experiences.get(i).getAsJsonObject();
            String index = "[" + (i + 1) + "]";
            
            // Validate technology
            String technology = extractTechnologyName(experience);
            String version = extractTechnologyVersion(experience);
            
            if (technology != null) {
                technologyStats.merge(technology.toLowerCase(), 1, Integer::sum);
                
                if (FORBIDDEN_TECHNOLOGIES.contains(technology.toLowerCase())) {
                    violations.add(index + " Forbidden technology: " + technology);
                }
                
                if (TEST_PATTERN.matcher(technology).matches()) {
                    violations.add(index + " Test-like technology: " + technology);
                }
            }
            
            // Validate category
            String category = getStringValue(experience, "category");
            if (category != null) {
                categoryStats.merge(category, 1, Integer::sum);
                
                if (FORBIDDEN_CATEGORIES.contains(category.toLowerCase())) {
                    violations.add(index + " Forbidden category: " + category);
                }
            }
            
            // Validate version
            if (version == null || version.equals("unknown") || version.trim().isEmpty()) {
                violations.add(index + " Missing or unknown version");
            }
            
            // Validate experience content
            String experienceText = extractExperienceContent(experience);
            if (experienceText == null || experienceText.length() < 20) {
                violations.add(index + " Experience too short or missing");
            }
        }
        
        System.out.println("📊 Validation Results:");
        System.out.println("   Total experiences: " + experiences.size());
        System.out.println("   Violations found: " + violations.size());
        
        if (!violations.isEmpty()) {
            System.out.println("\n❌ Quality Violations:");
            violations.stream().limit(10).forEach(v -> System.out.println("   " + v));
            if (violations.size() > 10) {
                System.out.println("   ... and " + (violations.size() - 10) + " more");
            }
        }
        
        System.out.println("\n📈 Top Categories:");
        categoryStats.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> System.out.println("   " + e.getKey() + ": " + e.getValue()));
            
        if (violations.isEmpty()) {
            System.out.println("✅ All experiences meet quality standards!");
        } else {
            System.out.println("\n💡 Run 'gradle cleanExperiences' to fix these issues");
        }
    }
    
    /**
     * Purge all test data and placeholder entries
     */
    public void purgeTestData() throws IOException {
        System.out.println("🗑️  Purging test data from experience database...");
        
        JsonObject database = loadExperienceDatabase();
        JsonArray experiences = database.getAsJsonArray("experiences");
        
        int originalCount = experiences.size();
        JsonArray purged = new JsonArray();
        
        for (JsonElement element : experiences) {
            JsonObject experience = element.getAsJsonObject();
            if (!isTestData(experience)) {
                purged.add(experience);
            }
        }
        
        database.add("experiences", purged);
        saveExperienceDatabase(database);
        
        int removed = originalCount - purged.size();
        System.out.println("✅ Test data purge completed!");
        System.out.println("   Removed: " + removed + " test experiences");
        System.out.println("   Remaining: " + purged.size() + " quality experiences");
    }
    
    /**
     * Generate comprehensive quality report
     */
    public void generateQualityReport() throws IOException {
        System.out.println("📊 Generating Experience Database Quality Report");
        System.out.println("=".repeat(50));
        
        JsonObject database = loadExperienceDatabase();
        JsonArray experiences = database.getAsJsonArray("experiences");
        
        Map<String, Integer> categoryStats = new HashMap<>();
        Map<String, Integer> technologyStats = new HashMap<>();
        Map<String, Integer> agentStats = new HashMap<>();
        Set<String> suspiciousEntries = new HashSet<>();
        
        int totalExperiences = experiences.size();
        int testDataCount = 0;
        int unknownVersions = 0;
        int shortExperiences = 0;
        
        for (JsonElement element : experiences) {
            JsonObject experience = element.getAsJsonObject();
            
            // Collect statistics
            String category = getStringValue(experience, "category");
            String technology = extractTechnologyName(experience);
            String agentId = getStringValue(experience, "agentId");
            String version = extractTechnologyVersion(experience);
            String experienceText = extractExperienceContent(experience);
            
            if (category != null) categoryStats.merge(category, 1, Integer::sum);
            if (technology != null) technologyStats.merge(technology, 1, Integer::sum);
            if (agentId != null) agentStats.merge(agentId, 1, Integer::sum);
            
            // Quality checks
            if (isTestData(experience)) {
                testDataCount++;
                suspiciousEntries.add("Test data: " + technology + " (" + category + ")");
            }
            
            if (version == null || version.equals("unknown")) {
                unknownVersions++;
            }
            
            if (experienceText == null || experienceText.length() < 20) {
                shortExperiences++;
            }
        }
        
        // Report
        System.out.println("📈 Database Overview:");
        System.out.println("   Total Experiences: " + totalExperiences);
        System.out.println("   Contributing Agents: " + agentStats.size());
        System.out.println("   Unique Categories: " + categoryStats.size());
        System.out.println("   Unique Technologies: " + technologyStats.size());
        
        System.out.println("\n⚠️  Quality Issues:");
        System.out.println("   Test Data Entries: " + testDataCount + " (" + 
                         String.format("%.1f%%", 100.0 * testDataCount / totalExperiences) + ")");
        System.out.println("   Unknown Versions: " + unknownVersions);
        System.out.println("   Short Experiences: " + shortExperiences);
        
        System.out.println("\n🏆 Top Categories:");
        categoryStats.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> System.out.println("   " + e.getKey() + ": " + e.getValue()));
            
        System.out.println("\n🤖 Contributing Agents:");
        agentStats.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> System.out.println("   " + e.getKey() + ": " + e.getValue() + " experiences"));
        
        if (!suspiciousEntries.isEmpty()) {
            System.out.println("\n🚨 Suspicious Entries (first 5):");
            suspiciousEntries.stream().limit(5)
                .forEach(entry -> System.out.println("   " + entry));
        }
        
        double qualityScore = 100.0 * (totalExperiences - testDataCount - unknownVersions - shortExperiences) / totalExperiences;
        System.out.println("\n🎯 Overall Quality Score: " + String.format("%.1f%%", qualityScore));
        
        if (qualityScore < 80) {
            System.out.println("💡 Recommendation: Run 'gradle cleanExperiences' to improve quality");
        }
    }
    
    // Helper methods
    
    private boolean isTestData(JsonObject experience) {
        String technology = extractTechnologyName(experience);
        String category = getStringValue(experience, "category");
        
        if (technology != null && FORBIDDEN_TECHNOLOGIES.contains(technology.toLowerCase())) {
            return true;
        }
        
        if (category != null && FORBIDDEN_CATEGORIES.contains(category.toLowerCase())) {
            return true;
        }
        
        if (technology != null && TEST_PATTERN.matcher(technology).matches()) {
            return true;
        }
        
        return false;
    }
    
    private boolean isLowQuality(JsonObject experience) {
        String experienceText = extractExperienceContent(experience);
        String version = extractTechnologyVersion(experience);
        
        // Too short
        if (experienceText == null || experienceText.length() < 20) {
            return true;
        }
        
        // Unknown version
        if (version == null || version.equals("unknown") || version.trim().isEmpty()) {
            return true;
        }
        
        return false;
    }
    
    private String extractExperienceContent(JsonObject experience) {
        // Try "experience" field first (legacy format)
        JsonElement experienceField = experience.get("experience");
        if (experienceField != null && !experienceField.isJsonNull() && experienceField.isJsonPrimitive()) {
            return experienceField.getAsString();
        }
        
        // Try extracting from evidence (new format)
        JsonElement evidenceField = experience.get("evidence");
        if (evidenceField != null && evidenceField.isJsonObject()) {
            JsonObject evidence = evidenceField.getAsJsonObject();
            StringBuilder content = new StringBuilder();
            
            // Combine relevant evidence fields
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
        
        // Try recommendation field
        JsonElement recommendationField = experience.get("recommendation");
        if (recommendationField != null && !recommendationField.isJsonNull() && recommendationField.isJsonPrimitive()) {
            return recommendationField.getAsString();
        }
        
        // Try workingAspects array
        JsonElement workingAspectsField = experience.get("workingAspects");
        if (workingAspectsField != null && workingAspectsField.isJsonArray()) {
            StringBuilder content = new StringBuilder();
            for (JsonElement aspect : workingAspectsField.getAsJsonArray()) {
                content.append(aspect.getAsString()).append(" ");
            }
            return content.toString().trim();
        }
        
        return null;
    }
    
    private String extractTechnologyName(JsonObject experience) {
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
    
    private String extractTechnologyVersion(JsonObject experience) {
        JsonElement techElement = experience.get("technology");
        if (techElement == null || techElement.isJsonNull()) {
            return null;
        }
        
        if (techElement.isJsonObject()) {
            JsonObject techObj = techElement.getAsJsonObject();
            if (techObj.has("version")) {
                return techObj.get("version").getAsString();
            }
        }
        
        // Also check for direct version field
        JsonElement versionElement = experience.get("version");
        if (versionElement != null && !versionElement.isJsonNull() && versionElement.isJsonPrimitive()) {
            return versionElement.getAsString();
        }
        
        return null;
    }
    
    private String createExperienceSignature(JsonObject experience) {
        String technology = extractTechnologyName(experience);
        String category = getStringValue(experience, "category");
        String version = extractTechnologyVersion(experience);
        String experienceText = extractExperienceContent(experience);
        
        return String.join("|", 
            technology != null ? technology : "",
            category != null ? category : "",
            version != null ? version : "",
            experienceText != null ? experienceText.substring(0, Math.min(50, experienceText.length())) : ""
        );
    }
    
    private String getStringValue(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }
        
        // Handle nested objects (like technology objects with name/version)
        if (element.isJsonObject()) {
            JsonObject nested = element.getAsJsonObject();
            // Try common fields
            if (nested.has("name")) {
                return nested.get("name").getAsString();
            }
            if (nested.has("technology")) {
                return nested.get("technology").getAsString();
            }
            // Return string representation of object
            return nested.toString();
        }
        
        return element.toString();
    }
    
    private JsonObject loadExperienceDatabase() throws IOException {
        Path experiencesPath = Paths.get(EXPERIENCES_FILE);
        if (!Files.exists(experiencesPath)) {
            throw new FileNotFoundException("Experience database not found: " + EXPERIENCES_FILE);
        }
        
        String content = Files.readString(experiencesPath);
        return gson.fromJson(content, JsonObject.class);
    }
    
    private void saveExperienceDatabase(JsonObject database) throws IOException {
        // Create backup
        Path experiencesPath = Paths.get(EXPERIENCES_FILE);
        Path backupPath = Paths.get(EXPERIENCES_FILE + ".backup." + System.currentTimeMillis());
        Files.copy(experiencesPath, backupPath);
        
        // Save cleaned database
        String content = gson.toJson(database);
        Files.writeString(experiencesPath, content);
        
        System.out.println("💾 Backup created: " + backupPath.getFileName());
    }
}