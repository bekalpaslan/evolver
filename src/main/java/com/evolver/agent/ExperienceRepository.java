package com.evolver.agent;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;
import java.util.logging.Logger;

/**
 * Experience Repository - Collective Agent Learning
 *
 * CENTRALIZED DATABASE APPROACH:
 * - Single experiences.json file at project root
 * - Auto-loaded on every framework execution
 * - Committed to GitHub for sharing across instances
 * - All agents read from and write to same database
 *
 * THREAD-SAFE: All public methods are synchronized
 * ATOMIC WRITES: Uses temp file + atomic move to prevent corruption
 * VALIDATED: All inputs are validated before saving
 */
public class ExperienceRepository {

    private static final Logger logger = Logger.getLogger(ExperienceRepository.class.getName());
    private static final Path DATABASE_FILE = Paths.get("experiences.json");
    private static final Path TEMP_FILE = Paths.get("experiences.tmp");
    private static final Path BACKUP_FILE = Paths.get("experiences.backup.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Thread-safety: Use lock object for synchronization
    private static final Object LOCK = new Object();
    private static volatile ExperienceDatabase database = null;

    // Safety limits
    private static final long MAX_FILE_SIZE = 10_000_000; // 10MB
    private static final int MAX_EXPERIENCES = 10_000;
    private static final int MAX_STRING_LENGTH = 10_000;

    /**
     * AUTO-LOAD DATABASE
     * Called automatically by AgentBootstrap
     */
    public static void loadOnBootstrap() {
        synchronized (LOCK) {
            loadDatabase();

            if (database != null && database.experiences != null) {
                System.out.println("📚 Loading shared experience database...");
                System.out.println("[OK] Found " + database.experiences.size() + " experiences from agents");
                System.out.println("  Database: experiences.json (tracked in git)");

                // Show summary - safe null handling
                Map<String, Long> byCategory = database.experiences.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(
                        e -> e.category != null ? e.category : "uncategorized",
                        Collectors.counting()
                    ));
                System.out.println("  Categories: " + byCategory);
            } else {
                System.out.println("📚 No shared experiences found. Creating new database...");
                database = new ExperienceDatabase();
                saveDatabase();
            }
        }
    }

    /**
     * Record a new experience - writes to centralized database
     */
    public static ExperienceBuilder record() {
        return new ExperienceBuilder();
    }

    /**
     * Search experiences by category (thread-safe)
     */
    public static List<ExperienceEntry> search(String category) {
        synchronized (LOCK) {
            loadDatabaseIfNeeded();

            if (database == null || database.experiences == null) {
                return Collections.emptyList();
            }

            return database.experiences.stream()
                .filter(Objects::nonNull)
                .filter(e -> "all".equals(category) || category.equals(e.category))
                .collect(Collectors.toList());
        }
    }

    /**
     * Search by tags (thread-safe)
     */
    public static List<ExperienceEntry> searchByTags(String... tags) {
        synchronized (LOCK) {
            loadDatabaseIfNeeded();

            if (database == null || database.experiences == null) {
                return Collections.emptyList();
            }

            return database.experiences.stream()
                .filter(Objects::nonNull)
                .filter(e -> e.tags != null && Arrays.stream(tags).anyMatch(tag -> e.tags.contains(tag)))
                .collect(Collectors.toList());
        }
    }

    /**
     * Get all experiences (thread-safe, returns defensive copy)
     */
    public static List<ExperienceEntry> getAll() {
        synchronized (LOCK) {
            loadDatabaseIfNeeded();
            return database != null && database.experiences != null ?
                new ArrayList<>(database.experiences) : Collections.emptyList();
        }
    }

    /**
     * Get database statistics (thread-safe)
     */
    public static void printStats() {
        synchronized (LOCK) {
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
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                    e -> e.category != null ? e.category : "uncategorized",
                    Collectors.counting()
                ));
            System.out.println("  By category: " + byCategory);

            Set<String> agents = database.experiences.stream()
                .filter(Objects::nonNull)
                .map(e -> e.agent)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            System.out.println("  Contributing agents: " + agents);
        }
    }

    // Internal methods

    private static void loadDatabaseIfNeeded() {
        // Double-checked locking for thread safety
        if (database == null) {
            synchronized (LOCK) {
                if (database == null) {
                    loadDatabase();
                }
            }
        }
    }

    private static void loadDatabase() {
        try {
            if (Files.exists(DATABASE_FILE)) {
                // Check file size before loading
                long fileSize = Files.size(DATABASE_FILE);
                if (fileSize > MAX_FILE_SIZE) {
                    throw new IOException("Database file too large: " + fileSize + " bytes (max: " + MAX_FILE_SIZE + ")");
                }

                if (fileSize == 0) {
                    logger.warning("Database file is empty, creating new database");
                    database = new ExperienceDatabase();
                    return;
                }

                String json = Files.readString(DATABASE_FILE);
                ExperienceDatabase loaded = gson.fromJson(json, ExperienceDatabase.class);

                // Validate loaded database
                if (loaded == null) {
                    throw new JsonParseException("Failed to parse database");
                }

                // Initialize collections if null
                if (loaded.experiences == null) {
                    loaded.experiences = new ArrayList<>();
                }
                if (loaded.statistics == null) {
                    loaded.statistics = new Statistics();
                }

                database = loaded;
                logger.info("Loaded " + database.experiences.size() + " experiences from database");
            } else {
                database = new ExperienceDatabase();
                logger.info("No existing database found, created new database");
            }
        } catch (JsonParseException e) {
            logger.severe("Failed to parse JSON database: " + e.getMessage());
            tryLoadBackup();
        } catch (IOException e) {
            logger.severe("Failed to load experience database: " + e.getMessage());
            tryLoadBackup();
        } catch (Exception e) {
            logger.severe("Unexpected error loading database: " + e.getMessage());
            database = new ExperienceDatabase();
        }
    }

    private static void tryLoadBackup() {
        try {
            if (Files.exists(BACKUP_FILE)) {
                logger.info("Attempting to load backup database...");
                String json = Files.readString(BACKUP_FILE);
                database = gson.fromJson(json, ExperienceDatabase.class);
                logger.info("Successfully loaded backup database");
            } else {
                logger.warning("No backup available, creating new database");
                database = new ExperienceDatabase();
            }
        } catch (Exception e) {
            logger.severe("Failed to load backup: " + e.getMessage());
            database = new ExperienceDatabase();
        }
    }

    private static void saveDatabase() {
        try {
            // Create backup before saving
            if (Files.exists(DATABASE_FILE)) {
                Files.copy(DATABASE_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
            }

            database.lastUpdated = Instant.now().toString();
            database.updateStatistics();

            String json = gson.toJson(database);

            // Atomic write: write to temp file, then atomic move
            Files.writeString(TEMP_FILE, json,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(TEMP_FILE, DATABASE_FILE,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);

            System.out.println("[OK] Experience saved to database: experiences.json");
            System.out.println("  📌 Commit this file to git to share with other agents!");

            logger.info("Database saved successfully: " + database.experiences.size() + " experiences");
        } catch (IOException e) {
            System.err.println("❌ Failed to save experience database: " + e.getMessage());
            logger.severe("Failed to save database: " + e.getMessage());

            // Clean up temp file if it exists
            try {
                Files.deleteIfExists(TEMP_FILE);
            } catch (IOException cleanupError) {
                logger.warning("Failed to clean up temp file: " + cleanupError.getMessage());
            }
        }
    }

    /**
     * Experience Builder with validation
     */
    public static class ExperienceBuilder {
        private final ExperienceEntry entry = new ExperienceEntry();

        public ExperienceBuilder() {
            entry.id = "exp-" + UUID.randomUUID().toString().substring(0, 8);
            entry.timestamp = Instant.now().toString();
            entry.agent = "agent_" + System.getProperty("user.name", "unknown");
        }

        public ExperienceBuilder category(String category) {
            validateString(category, "category");
            entry.category = category;
            return this;
        }

        public ExperienceBuilder technology(String name, String version, String type) {
            validateString(name, "technology name");
            validateString(version, "technology version");
            validateString(type, "technology type");
            entry.technology = new Technology(name, version, type);
            return this;
        }

        public ExperienceBuilder rating(String aspect, double value) {
            validateString(aspect, "rating aspect");
            validateRating(value);
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
            validateString(tech, "harmony technology");
            validateRating(rating);
            if (notes != null) {
                validateString(notes, "harmony notes");
            }
            if (entry.harmony == null) entry.harmony = new ArrayList<>();
            entry.harmony.add(new Harmony(tech, rating, notes));
            return this;
        }

        public ExperienceBuilder evidence(String key, String value) {
            validateString(key, "evidence key");
            validateString(value, "evidence value");
            if (entry.evidence == null) entry.evidence = new HashMap<>();
            entry.evidence.put(key, value);
            return this;
        }

        public ExperienceBuilder workingAspect(String aspect) {
            validateString(aspect, "working aspect");
            if (entry.workingAspects == null) entry.workingAspects = new ArrayList<>();
            entry.workingAspects.add(aspect);
            return this;
        }

        public ExperienceBuilder improvementArea(String area) {
            validateString(area, "improvement area");
            if (entry.improvementAreas == null) entry.improvementAreas = new ArrayList<>();
            entry.improvementAreas.add(area);
            return this;
        }

        public ExperienceBuilder recommendation(String rec) {
            validateString(rec, "recommendation");
            entry.recommendation = rec;
            return this;
        }

        public ExperienceBuilder tag(String tag) {
            validateString(tag, "tag");
            if (entry.tags == null) entry.tags = new ArrayList<>();
            entry.tags.add(tag);
            return this;
        }

        public void save() {
            synchronized (LOCK) {
                // Validate required fields
                if (entry.category == null || entry.category.trim().isEmpty()) {
                    throw new IllegalStateException("Category is required");
                }

                loadDatabaseIfNeeded();

                // Check size limit
                if (database.experiences.size() >= MAX_EXPERIENCES) {
                    throw new IllegalStateException("Database is full (max: " + MAX_EXPERIENCES + " experiences)");
                }

                database.experiences.add(entry);
                saveDatabase();
            }
        }

        // Validation helpers

        private void validateRating(double value) {
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Rating cannot be NaN or Infinite");
            }
            if (value < 0.0 || value > 10.0) {
                throw new IllegalArgumentException("Rating must be between 0.0 and 10.0, got: " + value);
            }
            // Check 0.1 precision
            double rounded = Math.round(value * 10.0) / 10.0;
            if (Math.abs(value - rounded) > 0.001) {
                throw new IllegalArgumentException(
                    "Rating must have 0.1 decimal precision (e.g., 7.3, 8.5), got: " + value +
                    ". Did you mean: " + rounded + "?"
                );
            }
        }

        private void validateString(String value, String fieldName) {
            if (value == null) {
                throw new IllegalArgumentException(fieldName + " cannot be null");
            }
            if (value.trim().isEmpty()) {
                throw new IllegalArgumentException(fieldName + " cannot be empty");
            }
            if (value.length() > MAX_STRING_LENGTH) {
                throw new IllegalArgumentException(
                    fieldName + " is too long: " + value.length() + " (max: " + MAX_STRING_LENGTH + ")"
                );
            }
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
                .filter(Objects::nonNull)
                .map(e -> e.category != null ? e.category : "uncategorized")
                .distinct()
                .collect(Collectors.toList());
            statistics.contributingAgents = experiences.stream()
                .filter(Objects::nonNull)
                .map(e -> e.agent)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        }
    }

    static class Statistics {
        int totalExperiences;
        List<String> categories = new ArrayList<>();
        List<String> contributingAgents = new ArrayList<>();
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
