package com.evolver.agent;package com.evolver.agent;package com.evolver.agent;



import com.google.gson.*;

import java.io.*;

import java.nio.file.*;import com.google.gson.*;import com.google.gson.*;



/**import java.io.*;import java.io.*;

 * Strict Quality Enforcer - Prevents dirty data from entering the experience database

 */import java.nio.file.*;import java.nio.file.*;

public class StrictQualityEnforcer {

    import java.util.*;import java.util.*;

    private static final String EXPERIENCES_FILE = "experiences.json";



    public static void main(String[] args) {

        System.out.println("🔒 STRICT QUALITY ENFORCEMENT - PREVENTING DIRTY DATA");/**/**

        System.out.println("All experiences must pass validation before database entry.");

        System.out.println("This ensures only meaningful, high-quality experiences are shared."); * Strict Quality Enforcer - Prevents dirty data from entering the experience database * Strict Quality Enforcer - Prevents dirty data from entering the experience database

        

        try { */ */

            new StrictQualityEnforcer().enforceQualityStandards();

        } catch (Exception e) {public class StrictQualityEnforcer {public class StrictQualityEnforcer {

            System.err.println("❌ Quality enforcement failed: " + e.getMessage());

            e.printStackTrace();        

        }

    }    private static final String EXPERIENCES_FILE = "experiences.json";    private static final String EXPERIENCES_FILE = "experiences.json";



    public void enforceQualityStandards() throws IOException {    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {

            System.out.println("✅ Experience database is empty - no enforcement needed");

            return;

        }    public static void main(String[] args) {    public static void main(String[] args) {



        System.out.println("✅ Quality enforcement system active and working!");        System.out.println("🔒 STRICT QUALITY ENFORCEMENT - PREVENTING DIRTY DATA");        System.out.println("🔒 STRICT QUALITY ENFORCEMENT - PREVENTING DIRTY DATA");

    }

}        System.out.println("All experiences must pass validation before database entry.");        System.out.println("All experiences must pass validation before database entry.");

        System.out.println("This ensures only meaningful, high-quality experiences are shared.");        System.out.println("This ensures only meaningful, high-quality experiences are shared.");

                

        try {        try {

            new StrictQualityEnforcer().enforceQualityStandards();            new StrictQualityEnforcer().enforceQualityStandards();

        } catch (Exception e) {        } catch (Exception e) {

            System.err.println("❌ Quality enforcement failed: " + e.getMessage());            System.err.println("❌ Quality enforcement failed: " + e.getMessage());

            e.printStackTrace();            e.printStackTrace();        

        }

    }        }



    public void enforceQualityStandards() throws IOException {    }    private static final String EXPERIENCES_FILE = "experiences.json";    private static final String EXPERIENCES_FILE = "experiences.json";

        if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {

            System.out.println("✅ Experience database is empty - no enforcement needed");    

            return;

        }    public void enforceQualityStandards() throws IOException {    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();



        String content = Files.readString(Paths.get(EXPERIENCES_FILE));        if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {

        JsonObject root = gson.fromJson(content, JsonObject.class);

                    System.out.println("✅ No experience database found - no enforcement needed");        

        if (root == null || !root.has("experiences")) {

            System.out.println("✅ No experiences to validate");            return;

            return;

        }        }    public static void main(String[] args) {    public static void main(String[] args) {



        JsonArray experiences = root.getAsJsonArray("experiences");        

        if (experiences.size() == 0) {

            System.out.println("✅ Experience database is empty - no enforcement needed");        JsonObject database = loadDatabase();        StrictQualityEnforcer enforcer = new StrictQualityEnforcer();        StrictQualityEnforcer enforcer = new StrictQualityEnforcer();

            return;

        }        JsonArray experiences = database.getAsJsonArray("experiences");



        System.out.println("🔍 Enforcing quality standards on " + experiences.size() + " experiences...");                        

        

        List<QualityViolation> violations = new ArrayList<>();        if (experiences == null || experiences.size() == 0) {

        JsonArray validExperiences = new JsonArray();

        int rejectedCount = 0;            System.out.println("✅ Experience database is empty - no enforcement needed");        try {        try {

        

        for (int i = 0; i < experiences.size(); i++) {            return;

            JsonObject experience = experiences.get(i).getAsJsonObject();

                    }            enforcer.enforceQualityStandards();            enforcer.enforceQualityStandards();

            ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(experience);

                    

            if (validation.isValid()) {

                validExperiences.add(experience);        System.out.println("📊 Validating " + experiences.size() + " experiences...");        } catch (Exception e) {        } catch (Exception e) {

                System.out.printf("✅ [%d] Quality Score: %.1f/10.0 - ACCEPTED%n", i + 1, validation.qualityScore);

            } else {        

                rejectedCount++;

                String agentId = getStringValue(experience, "agentId");        JsonArray validExperiences = new JsonArray();            System.err.println("❌ Quality enforcement failed: " + e.getMessage());            System.err.println("❌ Quality enforcement failed: " + e.getMessage());

                String technology = extractTechnologyName(experience);

                String category = getStringValue(experience, "category");        int rejected = 0;

                

                violations.add(new QualityViolation(i + 1, agentId, technology, category, validation));                    e.printStackTrace();            e.printStackTrace();

                System.out.printf("❌ [%d] Quality Score: %.1f/10.0 - REJECTED%n", i + 1, validation.qualityScore);

            }        for (int i = 0; i < experiences.size(); i++) {

        }

            JsonObject experience = experiences.get(i).getAsJsonObject();            System.exit(1);            System.exit(1);

        // Print summary

        System.out.println("\n🔒 QUALITY ENFORCEMENT RESULTS");            ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(experience);

        System.out.println("=".repeat(50));

        System.out.println("Total Experiences Processed: " + experiences.size());                    }        }

        System.out.println("✅ Valid Experiences: " + validExperiences.size());

        System.out.println("❌ Rejected Experiences: " + rejectedCount);            if (validation.isValid()) {

        

        if (rejectedCount > 0) {                validExperiences.add(experience);    }    }

            System.out.println("\n❌ QUALITY VIOLATIONS DETECTED:");

            for (QualityViolation violation : violations) {                System.out.printf("✅ [%d] Quality Score: %.1f - ACCEPTED%n", i + 1, validation.qualityScore);

                System.out.println("• " + violation.toString());

            }            } else {        

            

            // Update experiences.json with only valid experiences                rejected++;

            root.add("experiences", validExperiences);

            Files.writeString(Paths.get(EXPERIENCES_FILE), gson.toJson(root));                System.out.printf("❌ [%d] Quality Score: %.1f - REJECTED%n", i + 1, validation.qualityScore);    /**    /**

            System.out.println("\n🧹 Database cleaned - removed " + rejectedCount + " contaminated experiences");

        }                System.out.println("   Violations: " + validation.violations.size());

        

        System.out.println("✅ Quality enforcement complete!");            }     * Enforce strict quality standards across the entire database     * Enforce strict quality standards across the entire database

    }

        }

    private String getStringValue(JsonObject obj, String field) {

        JsonElement element = obj.get(field);             */     */

        return element != null && !element.isJsonNull() ? element.getAsString() : "unknown";

    }        System.out.println("\n🔒 ENFORCEMENT RESULTS:");

    

    private String extractTechnologyName(JsonObject experience) {        System.out.println("Valid: " + validExperiences.size());    public void enforceQualityStandards() throws IOException {    public void enforceQualityStandards() throws IOException {

        if (experience.has("context") && experience.get("context").isJsonObject()) {

            JsonObject context = experience.getAsJsonObject("context");        System.out.println("Rejected: " + rejected);

            if (context.has("technology")) {

                return getStringValue(context, "technology");        System.out.printf("Quality: %.1f%%%n", 100.0 * validExperiences.size() / experiences.size());        System.out.println("🔒 STRICT QUALITY ENFORCEMENT INITIATED");        System.out.println("🔒 STRICT QUALITY ENFORCEMENT INITIATED");

            }

        }        

        return "unknown";

    }        if (rejected > 0) {        System.out.println("=".repeat(50));        System.out.println("=" .repeat(50));

    

    private static class QualityViolation {            // Create backup

        final int index;

        final String agentId;            Path backup = Paths.get(EXPERIENCES_FILE + ".backup." + System.currentTimeMillis());                

        final String technology;

        final String category;            Files.copy(Paths.get(EXPERIENCES_FILE), backup);

        final ExperienceValidator.ValidationResult validation;

                            if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {        if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {

        QualityViolation(int index, String agentId, String technology, String category, 

                        ExperienceValidator.ValidationResult validation) {            // Save only valid experiences

            this.index = index;

            this.agentId = agentId;            database.add("experiences", validExperiences);            System.out.println("✅ No experience database found - quality enforcement not needed");            System.out.println("✅ No experience database found - quality enforcement not needed");

            this.technology = technology;

            this.category = category;            saveDatabase(database);

            this.validation = validation;

        }                        return;            return;

        

        @Override            System.out.println("✅ Database cleaned - only quality experiences remain");

        public String toString() {

            return String.format("[%d] Agent: %s, Tech: %s, Category: %s, Score: %.1f/10.0 - %s",         } else {        }        }

                               index, agentId, technology, category, validation.qualityScore,

                               String.join(", ", validation.violations));            System.out.println("🎉 All experiences meet quality standards!");

        }

    }        }                

}
    }

            JsonObject database = loadExperienceDatabase();        JsonObject database = loadExperienceDatabase();

    private JsonObject loadDatabase() throws IOException {

        String content = Files.readString(Paths.get(EXPERIENCES_FILE));        JsonArray experiences = database.getAsJsonArray("experiences");        JsonArray experiences = database.getAsJsonArray("experiences");

        return gson.fromJson(content, JsonObject.class);

    }                

    

    private void saveDatabase(JsonObject database) throws IOException {        if (experiences == null || experiences.size() == 0) {        if (experiences == null || experiences.size() == 0) {

        String content = gson.toJson(database);

        Files.writeString(Paths.get(EXPERIENCES_FILE), content);            System.out.println("✅ Experience database is empty - quality enforcement not needed");            System.out.println("✅ Experience database is empty - quality enforcement not needed");

    }

}            return;            return;

        }        }

                

        System.out.println("📊 Enforcing quality standards on " + experiences.size() + " experiences...");        System.out.println("📊 Enforcing quality standards on " + experiences.size() + " experiences...");

                

        List<QualityViolation> violations = new ArrayList<>();        List<QualityViolation> violations = new ArrayList<>();

        JsonArray validExperiences = new JsonArray();        JsonArray validExperiences = new JsonArray<>();

        int rejectedCount = 0;        int rejectedCount = 0;

                

        for (int i = 0; i < experiences.size(); i++) {        for (int i = 0; i < experiences.size(); i++) {

            JsonObject experience = experiences.get(i).getAsJsonObject();            JsonObject experience = experiences.get(i).getAsJsonObject();

                        

            // Run strict validation            // Run strict validation

            ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(experience);            ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(experience);

                        

            if (validation.isValid()) {            if (validation.isValid()) {

                validExperiences.add(experience);                validExperiences.add(experience);

                System.out.printf("✅ [%d] Quality Score: %.1f/10.0 - ACCEPTED%n", i + 1, validation.qualityScore);                System.out.printf("✅ [%d] Quality Score: %.1f/10.0 - ACCEPTED%n", i + 1, validation.qualityScore);

            } else {            } else {

                rejectedCount++;                rejectedCount++;

                String agentId = getStringValue(experience, "agentId");                String agentId = getStringValue(experience, "agentId");

                String technology = extractTechnologyName(experience);                String technology = extractTechnologyName(experience);

                String category = getStringValue(experience, "category");                String category = getStringValue(experience, "category");

                                

                violations.add(new QualityViolation(i + 1, agentId, technology, category, validation));                violations.add(new QualityViolation(i + 1, agentId, technology, category, validation));

                System.out.printf("❌ [%d] Quality Score: %.1f/10.0 - REJECTED%n", i + 1, validation.qualityScore);                System.out.printf("❌ [%d] Quality Score: %.1f/10.0 - REJECTED%n", i + 1, validation.qualityScore);

            }            }

        }        }

                

        // Report enforcement results        // Report enforcement results

        System.out.println("\\n🔒 QUALITY ENFORCEMENT RESULTS");        System.out.println("\n🔒 QUALITY ENFORCEMENT RESULTS");

        System.out.println("=".repeat(50));        System.out.println("=" .repeat(50));

        System.out.println("Total Experiences Processed: " + experiences.size());        System.out.println("Total Experiences Processed: " + experiences.size());

        System.out.println("✅ Valid Experiences: " + validExperiences.size());        System.out.println("✅ Valid Experiences: " + validExperiences.size());

        System.out.println("❌ Rejected Experiences: " + rejectedCount);        System.out.println("❌ Rejected Experiences: " + rejectedCount);

        System.out.printf("📈 Overall Database Quality: %.1f%%%n",         System.out.printf("📈 Overall Database Quality: %.1f%%%n", \n            100.0 * validExperiences.size() / experiences.size());

            100.0 * validExperiences.size() / experiences.size());        

                if (rejectedCount > 0) {

        if (rejectedCount > 0) {            System.out.println("\\n🚫 REJECTED EXPERIENCES DETAILS:");

            System.out.println("\\n🚫 REJECTED EXPERIENCES DETAILS:");            System.out.println("-".repeat(50));

            System.out.println("-".repeat(50));            

                        for (QualityViolation violation : violations) {

            for (QualityViolation violation : violations) {                System.out.printf("❌ Experience #%d:%n", violation.index);

                System.out.printf("❌ Experience #%d:%n", violation.index);                System.out.printf("   Agent: %s%n", violation.agentId != null ? violation.agentId : "unknown");

                System.out.printf("   Agent: %s%n", violation.agentId != null ? violation.agentId : "unknown");                System.out.printf("   Technology: %s%n", violation.technology != null ? violation.technology : "unknown");

                System.out.printf("   Technology: %s%n", violation.technology != null ? violation.technology : "unknown");                System.out.printf("   Category: %s%n", violation.category != null ? violation.category : "unknown");

                System.out.printf("   Category: %s%n", violation.category != null ? violation.category : "unknown");                System.out.printf("   Quality Score: %.1f/10.0%n", violation.validation.qualityScore);

                System.out.printf("   Quality Score: %.1f/10.0%n", violation.validation.qualityScore);                System.out.println("   Violations:");

                System.out.println("   Violations:");                violation.validation.violations.forEach(v -> System.out.println("     • " + v));

                violation.validation.violations.forEach(v -> System.out.println("     • " + v));                System.out.println();

                System.out.println();            }

            }            

                        // Create backup before cleaning

            // Create backup before cleaning            Path backupPath = Paths.get(EXPERIENCES_FILE + ".backup." + System.currentTimeMillis());

            Path backupPath = Paths.get(EXPERIENCES_FILE + ".backup." + System.currentTimeMillis());            Files.copy(Paths.get(EXPERIENCES_FILE), backupPath);

            Files.copy(Paths.get(EXPERIENCES_FILE), backupPath);            System.out.println("💾 Backup created: " + backupPath.getFileName());

            System.out.println("💾 Backup created: " + backupPath.getFileName());            

                        // Update database with only valid experiences

            // Update database with only valid experiences            database.add("experiences", validExperiences);

            database.add("experiences", validExperiences);            saveExperienceDatabase(database);

            saveExperienceDatabase(database);            

                        System.out.printf("\\n🧹 Database cleaned: %d low-quality experiences removed%n", rejectedCount);

            System.out.printf("\\n🧹 Database cleaned: %d low-quality experiences removed%n", rejectedCount);            System.out.println("✅ Only high-quality experiences remain in the database");

            System.out.println("✅ Only high-quality experiences remain in the database");        } else {

        } else {            System.out.println("\\n🎉 ALL EXPERIENCES MEET QUALITY STANDARDS!");

            System.out.println("\\n🎉 ALL EXPERIENCES MEET QUALITY STANDARDS!");            System.out.println("✅ No enforcement action required");

            System.out.println("✅ No enforcement action required");        }

        }        

                // Generate agent accountability summary

        // Generate agent accountability summary        generateAgentAccountabilitySummary(violations);

        generateAgentAccountabilitySummary(violations);        

                System.out.println("\\n🔒 QUALITY ENFORCEMENT COMPLETE");

        System.out.println("\\n🔒 QUALITY ENFORCEMENT COMPLETE");        System.out.println("Database is now guaranteed to contain only meaningful, high-quality experiences.");

        System.out.println("Database is now guaranteed to contain only meaningful, high-quality experiences.");    }

    }    

        /**

    /**     * Generate summary of agent accountability for quality violations

     * Generate summary of agent accountability for quality violations     */

     */    private void generateAgentAccountabilitySummary(List<QualityViolation> violations) {

    private void generateAgentAccountabilitySummary(List<QualityViolation> violations) {        if (violations.isEmpty()) {

        if (violations.isEmpty()) {            return;

            return;        }

        }        

                Map<String, Integer> agentViolations = new HashMap<>();

        Map<String, Integer> agentViolations = new HashMap<>();        Map<String, Double> agentWorstScores = new HashMap<>();

        Map<String, Double> agentWorstScores = new HashMap<>();        

                for (QualityViolation violation : violations) {

        for (QualityViolation violation : violations) {            String agent = violation.agentId != null ? violation.agentId : "unknown";

            String agent = violation.agentId != null ? violation.agentId : "unknown";            agentViolations.merge(agent, 1, Integer::sum);

            agentViolations.merge(agent, 1, Integer::sum);            agentWorstScores.merge(agent, violation.validation.qualityScore, Double::min);

            agentWorstScores.merge(agent, violation.validation.qualityScore, Double::min);        }

        }        

                System.out.println("\\n📊 AGENT ACCOUNTABILITY SUMMARY:");

        System.out.println("\\n📊 AGENT ACCOUNTABILITY SUMMARY:");        System.out.println("-".repeat(50));

        System.out.println("-".repeat(50));        

                agentViolations.entrySet().stream()

        agentViolations.entrySet().stream()            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())

            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())            .forEach(entry -> {

            .forEach(entry -> {                String agent = entry.getKey();

                String agent = entry.getKey();                int count = entry.getValue();

                int count = entry.getValue();                double worstScore = agentWorstScores.get(agent);

                double worstScore = agentWorstScores.get(agent);                System.out.printf("🤖 %s: %d violations (worst score: %.1f/10.0)%n", 

                System.out.printf("🤖 %s: %d violations (worst score: %.1f/10.0)%n",                     agent, count, worstScore);

                    agent, count, worstScore);            });

            });        

                System.out.println("\\n💡 Agents with violations should review quality standards in AGENT_MANIFESTO.md");

        System.out.println("\\n💡 Agents with violations should review quality standards in AGENT_MANIFESTO.md");    }

    }    

        // Helper methods

    // Helper methods    

        private String extractTechnologyName(JsonObject experience) {

    private String extractTechnologyName(JsonObject experience) {        JsonElement techElement = experience.get("technology");

        JsonElement techElement = experience.get("technology");        if (techElement == null || techElement.isJsonNull()) {

        if (techElement == null || techElement.isJsonNull()) {            return null;

            return null;        }

        }        

                if (techElement.isJsonPrimitive()) {

        if (techElement.isJsonPrimitive()) {            return techElement.getAsString();

            return techElement.getAsString();        }

        }        

                if (techElement.isJsonObject()) {

        if (techElement.isJsonObject()) {            JsonObject techObj = techElement.getAsJsonObject();

            JsonObject techObj = techElement.getAsJsonObject();            if (techObj.has("name")) {

            if (techObj.has("name")) {                return techObj.get("name").getAsString();

                return techObj.get("name").getAsString();            }

            }        }

        }        

                return null;

        return null;    }

    }    

        private String getStringValue(JsonObject obj, String key) {

    private String getStringValue(JsonObject obj, String key) {        JsonElement element = obj.get(key);

        JsonElement element = obj.get(key);        if (element == null || element.isJsonNull()) {

        if (element == null || element.isJsonNull()) {            return null;

            return null;        }

        }        

                if (element.isJsonPrimitive()) {

        if (element.isJsonPrimitive()) {            return element.getAsString();

            return element.getAsString();        }

        }        

                return element.toString();

        return element.toString();    }

    }    

        private JsonObject loadExperienceDatabase() throws IOException {

    private JsonObject loadExperienceDatabase() throws IOException {        Path experiencesPath = Paths.get(EXPERIENCES_FILE);

        Path experiencesPath = Paths.get(EXPERIENCES_FILE);        String content = Files.readString(experiencesPath);

        String content = Files.readString(experiencesPath);        return gson.fromJson(content, JsonObject.class);

        return gson.fromJson(content, JsonObject.class);    }

    }    

        private void saveExperienceDatabase(JsonObject database) throws IOException {

    private void saveExperienceDatabase(JsonObject database) throws IOException {        String content = gson.toJson(database);

        String content = gson.toJson(database);        Files.writeString(Paths.get(EXPERIENCES_FILE), content);

        Files.writeString(Paths.get(EXPERIENCES_FILE), content);    }

    }    

        /**

    /**     * Quality violation container

     * Quality violation container     */

     */    private static class QualityViolation {

    private static class QualityViolation {        final int index;

        final int index;        final String agentId;

        final String agentId;        final String technology;

        final String technology;        final String category;

        final String category;        final ExperienceValidator.ValidationResult validation;

        final ExperienceValidator.ValidationResult validation;        

                QualityViolation(int index, String agentId, String technology, String category, 

        QualityViolation(int index, String agentId, String technology, String category,                         ExperienceValidator.ValidationResult validation) {

                        ExperienceValidator.ValidationResult validation) {            this.index = index;

            this.index = index;            this.agentId = agentId;

            this.agentId = agentId;            this.technology = technology;

            this.technology = technology;            this.category = category;

            this.category = category;            this.validation = validation;

            this.validation = validation;        }

        }    }

    }}
}