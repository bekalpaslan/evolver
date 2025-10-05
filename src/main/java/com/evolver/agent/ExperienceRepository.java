package com.evolver.agent;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Experience Repository - Collective Agent Learning
 *
 * CENTRALIZED DATABASE APPROACH:
 * - Single experiences.json file at project root
 * - Auto-loaded on every framework execution
 * - Committed to GitHub for sharing across instances
 * - All agents read from and write to same database
 */
public class ExperienceRepository {

    private static final Path DATABASE_FILE = Paths.get("experiences.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static ExperienceDatabase database = null;

    /**
     * AUTO-LOAD DATABASE
     * Called automatically by AgentBootstrap
     */
    public static void loadOnBootstrap() {
        loadDatabase();

        if (database != null && database.experiences != null) {
            System.out.println("📚 Loading shared experience database...");
            System.out.println("[OK] Found " + database.experiences.size() + " experiences from agents");
            System.out.println("  Database: experiences.json (tracked in git)");

            // Show summary
            Map<String, Long> byCategory = database.experiences.stream()
                .collect(Collectors.groupingBy(e -> e.category, Collectors.counting()));
            System.out.println("  Categories: " + byCategory);
        } else {
            System.out.println("📚 No shared experiences found. Creating new database...");
            database = new ExperienceDatabase();
            saveDatabase();
        }
    }

    /**
     * Record a new experience - writes to centralized database
     */
    public static ExperienceBuilder record() {
        return new ExperienceBuilder();
    }

    /**
     * Search experiences by category
     */
    public static List<ExperienceEntry> search(String category) {
        loadDatabaseIfNeeded();

        if (database == null || database.experiences == null) {
            return Collections.emptyList();
        }

        return database.experiences.stream()
            .filter(e -> "all".equals(category) || category.equals(e.category))
            .collect(Collectors.toList());
    }

    /**
     * Search by tags
     */
    public static List<ExperienceEntry> searchByTags(String... tags) {
        loadDatabaseIfNeeded();

        if (database == null || database.experiences == null) {
            return Collections.emptyList();
        }

        return database.experiences.stream()
            .filter(e -> e.tags != null && Arrays.stream(tags).anyMatch(tag -> e.tags.contains(tag)))
            .collect(Collectors.toList());
    }

    /**
     * Get all experiences
     */
    public static List<ExperienceEntry> getAll() {
        loadDatabaseIfNeeded();
        return database != null && database.experiences != null ?
            new ArrayList<>(database.experiences) : Collections.emptyList();
    }

    /**
     * Get database statistics
     */
    public static void printStats() {
        loadDatabaseIfNeeded();

        if (database == null || database.experiences == null) {
            System.out.println("No experiences in database");
            return;
        }

        System.out.println("\n📊 Experience Database Stats:");
        System.out.println("  Total experiences: " + database.experiences.size());
        System.out.println("  Last updated: " + database.lastUpdated);
        System.out.println("  Database version: " + database.version);

        Map<String, Long> byCategory = database.experiences.stream()
            .collect(Collectors.groupingBy(e -> e.category, Collectors.counting()));
        System.out.println("  By category: " + byCategory);

        Set<String> agents = database.experiences.stream()
            .map(e -> e.agent)
            .collect(Collectors.toSet());
        System.out.println("  Contributing agents: " + agents);
    }

    // Internal methods

    private static void loadDatabaseIfNeeded() {
        if (database == null) {
            loadDatabase();
        }
    }

    private static void loadDatabase() {
        try {
            if (Files.exists(DATABASE_FILE)) {
                String json = Files.readString(DATABASE_FILE);
                database = gson.fromJson(json, ExperienceDatabase.class);
            } else {
                database = new ExperienceDatabase();
            }
        } catch (Exception e) {
            System.err.println("⚠ Failed to load experience database: " + e.getMessage());
            database = new ExperienceDatabase();
        }
    }

    private static void saveDatabase() {
        try {
            database.lastUpdated = Instant.now().toString();
            database.updateStatistics();

            String json = gson.toJson(database);
            Files.writeString(DATABASE_FILE, json);

            System.out.println("[OK] Experience saved to database: experiences.json");
            System.out.println("  📌 Commit this file to git to share with other agents!");
        } catch (Exception e) {
            System.err.println("❌ Failed to save experience database: " + e.getMessage());
        }
    }

    /**
     * Experience Builder
     */
    public static class ExperienceBuilder {
        private final ExperienceEntry entry = new ExperienceEntry();

        public ExperienceBuilder() {
            entry.id = "exp-" + UUID.randomUUID().toString().substring(0, 8);
            entry.timestamp = Instant.now().toString();
            entry.agent = "agent_" + System.getProperty("user.name", "unknown");
        }

        public ExperienceBuilder category(String category) {
            entry.category = category;
            return this;
        }

        public ExperienceBuilder technology(String name, String version, String type) {
            entry.technology = new Technology(name, version, type);
            return this;
        }

        public ExperienceBuilder rating(String aspect, double value) {
            if (entry.ratings == null) entry.ratings = new HashMap<>();
            entry.ratings.put(aspect, value);
            return this;
        }

        public ExperienceBuilder easeOfUse(double value) {
            return rating("easeOfUse", value);
        }

        public ExperienceBuilder power(double value) {
            return rating("power", value);
        }

        public ExperienceBuilder performance(double value) {
            return rating("performance", value);
        }

        public ExperienceBuilder harmony(String tech, double rating, String notes) {
            if (entry.harmony == null) entry.harmony = new ArrayList<>();
            entry.harmony.add(new Harmony(tech, rating, notes));
            return this;
        }

        public ExperienceBuilder evidence(String key, String value) {
            if (entry.evidence == null) entry.evidence = new HashMap<>();
            entry.evidence.put(key, value);
            return this;
        }

        public ExperienceBuilder workingAspect(String aspect) {
            if (entry.workingAspects == null) entry.workingAspects = new ArrayList<>();
            entry.workingAspects.add(aspect);
            return this;
        }

        public ExperienceBuilder improvementArea(String area) {
            if (entry.improvementAreas == null) entry.improvementAreas = new ArrayList<>();
            entry.improvementAreas.add(area);
            return this;
        }

        public ExperienceBuilder recommendation(String rec) {
            entry.recommendation = rec;
            return this;
        }

        public ExperienceBuilder tag(String tag) {
            if (entry.tags == null) entry.tags = new ArrayList<>();
            entry.tags.add(tag);
            return this;
        }

        public void save() {
            loadDatabaseIfNeeded();
            database.experiences.add(entry);
            saveDatabase();
        }
    }

    // Data classes

    static class ExperienceDatabase {
        String version = "1.0.0";
        String lastUpdated;
        List<ExperienceEntry> experiences = new ArrayList<>();
        Statistics statistics = new Statistics();

        void updateStatistics() {
            statistics.totalExperiences = experiences.size();
            statistics.categories = experiences.stream()
                .map(e -> e.category)
                .distinct()
                .collect(Collectors.toList());
            statistics.contributingAgents = experiences.stream()
                .map(e -> e.agent)
                .distinct()
                .collect(Collectors.toList());
        }
    }

    static class Statistics {
        int totalExperiences;
        List<String> categories;
        List<String> contributingAgents;
    }

    public static class ExperienceEntry {
        String id;
        String timestamp;
        String agent;
        String category;
        Technology technology;
        Map<String, Double> ratings;
        List<Harmony> harmony;
        Map<String, String> evidence;
        List<String> workingAspects;
        List<String> improvementAreas;
        String recommendation;
        List<String> tags;
    }

    static class Technology {
        String name;
        String version;
        String type;

        Technology(String name, String version, String type) {
            this.name = name;
            this.version = version;
            this.type = type;
        }
    }

    static class Harmony {
        String technology;
        double rating;
        String notes;

        Harmony(String technology, double rating, String notes) {
            this.technology = technology;
            this.rating = rating;
            this.notes = notes;
        }
    }
}
