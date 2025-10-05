package com.evolver.agent.experience;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;
import java.util.logging.Logger;
import com.evolver.agent.identity.AgentIdentity;
import com.evolver.agent.quality.ExperienceValidator;

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
    private static final Path LOCK_FILE = Paths.get(".experiences.lock");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Version Management
    private static final String CURRENT_VERSION = "2.0.0";
    private static final String MIN_SUPPORTED_VERSION = "1.0.0";
    private static final Map<String, String> VERSION_CHANGELOG = Map.of(
        "1.0.0", "Initial version with basic experience tracking",
        "2.0.0", "Added migration support, enhanced validation, conflict resolution"
    );

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
            // Acquire file lock for safe concurrent access
            acquireFileLock();
            
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
                if (loaded.version == null) {
                    loaded.version = "1.0.0"; // Default for legacy databases
                }

                // VERSION MIGRATION LOGIC
                if (!CURRENT_VERSION.equals(loaded.version)) {
                    logger.info("Database version mismatch. Current: " + loaded.version + ", Required: " + CURRENT_VERSION);
                    
                    if (isVersionSupported(loaded.version)) {
                        logger.info("Migrating database from " + loaded.version + " to " + CURRENT_VERSION);
                        loaded = migrateDatabase(loaded, loaded.version, CURRENT_VERSION);
                        logger.info("Migration completed successfully");
                    } else {
                        throw new IllegalStateException("Unsupported database version: " + loaded.version + 
                            ". Minimum supported: " + MIN_SUPPORTED_VERSION);
                    }
                }

                // Validate schema after migration
                validateDatabaseSchema(loaded);

                database = loaded;
                logger.info("Loaded " + database.experiences.size() + " experiences from database (version: " + database.version + ")");
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
        } finally {
            releaseFileLock();
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
            // Acquire file lock for safe concurrent access
            acquireFileLock();
            
            // Create backup before saving
            if (Files.exists(DATABASE_FILE)) {
                Files.copy(DATABASE_FILE, BACKUP_FILE, StandardCopyOption.REPLACE_EXISTING);
            }

            database.lastUpdated = Instant.now().toString();
            database.version = CURRENT_VERSION; // Ensure current version
            database.updateStatistics();

            // Validate before saving
            validateDatabaseSchema(database);

            String json = gson.toJson(database);

            // Atomic write: write to temp file, then atomic move
            Files.writeString(TEMP_FILE, json,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(TEMP_FILE, DATABASE_FILE,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);

            System.out.println("[OK] Experience saved to database: experiences.json (v" + database.version + ")");
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
        } finally {
            releaseFileLock();
        }
    }
    
    // VERSION MIGRATION METHODS
    
    private static boolean isVersionSupported(String version) {
        if (version == null) return false;
        return compareVersions(version, MIN_SUPPORTED_VERSION) >= 0;
    }
    
    private static ExperienceDatabase migrateDatabase(ExperienceDatabase db, String fromVersion, String toVersion) {
        logger.info("Starting migration from " + fromVersion + " to " + toVersion);
        
        ExperienceDatabase migrated = db;
        
        // Migration path: 1.0.0 -> 2.0.0
        if ("1.0.0".equals(fromVersion) && "2.0.0".equals(toVersion)) {
            migrated = migrate_1_0_0_to_2_0_0(db);
        }
        
        migrated.version = toVersion;
        migrated.lastUpdated = Instant.now().toString();
        
        logger.info("Migration completed: " + fromVersion + " -> " + toVersion);
        return migrated;
    }
    
    private static ExperienceDatabase migrate_1_0_0_to_2_0_0(ExperienceDatabase db) {
        logger.info("Applying migration: 1.0.0 -> 2.0.0");
        
        // Add schema version tracking
        if (db.schemaMetadata == null) {
            db.schemaMetadata = new SchemaMetadata();
            db.schemaMetadata.formatVersion = "consolidated";
            db.schemaMetadata.migrationHistory = new ArrayList<>();
            db.schemaMetadata.migrationHistory.add(
                "Migrated from 1.0.0 to 2.0.0 on " + Instant.now().toString()
            );
        }
        
        // Validate and fix experience entries
        if (db.experiences != null) {
            for (ExperienceEntry entry : db.experiences) {
                if (entry != null) {
                    // Ensure all entries have required fields
                    if (entry.id == null) {
                        entry.id = "exp-" + UUID.randomUUID().toString().substring(0, 8);
                    }
                    if (entry.timestamp == null) {
                        entry.timestamp = Instant.now().toString();
                    }
                    if (entry.agent == null) {
                        entry.agent = "legacy_agent";
                    }
                    if (entry.category == null) {
                        entry.category = "general";
                    }
                    
                    // Initialize collections if null
                    if (entry.tags == null) entry.tags = new ArrayList<>();
                    if (entry.workingAspects == null) entry.workingAspects = new ArrayList<>();
                    if (entry.improvementAreas == null) entry.improvementAreas = new ArrayList<>();
                }
            }
        }
        
        return db;
    }
    
    private static int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        
        int maxLength = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < maxLength; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            
            if (part1 < part2) return -1;
            if (part1 > part2) return 1;
        }
        return 0;
    }
    
    private static void validateDatabaseSchema(ExperienceDatabase db) {
        if (db == null) {
            throw new IllegalStateException("Database cannot be null");
        }
        
        if (db.experiences == null) {
            throw new IllegalStateException("Experiences collection cannot be null");
        }
        
        if (db.statistics == null) {
            throw new IllegalStateException("Statistics cannot be null");
        }
        
        // Validate version format
        if (db.version == null || !db.version.matches("\\d+\\.\\d+\\.\\d+")) {
            throw new IllegalStateException("Invalid version format: " + db.version);
        }
        
        // Validate experience entries
        for (ExperienceEntry entry : db.experiences) {
            if (entry != null) {
                if (entry.id == null || entry.id.trim().isEmpty()) {
                    throw new IllegalStateException("Experience entry missing required ID");
                }
                if (entry.category == null || entry.category.trim().isEmpty()) {
                    throw new IllegalStateException("Experience entry missing required category: " + entry.id);
                }
            }
        }
    }
    
    // FILE LOCKING FOR CONCURRENT ACCESS
    
    private static void acquireFileLock() {
        try {
            // Simple file-based locking mechanism
            for (int i = 0; i < 10; i++) {
                if (!Files.exists(LOCK_FILE)) {
                    Files.createFile(LOCK_FILE);
                    return;
                }
                Thread.sleep(100); // Wait 100ms before retry
            }
            logger.warning("Could not acquire file lock, proceeding anyway");
        } catch (IOException | InterruptedException e) {
            logger.warning("File locking failed: " + e.getMessage());
        }
    }
    
    private static void releaseFileLock() {
        try {
            Files.deleteIfExists(LOCK_FILE);
        } catch (IOException e) {
            logger.warning("Failed to release file lock: " + e.getMessage());
        }
    }

    /**
     * Experience Builder with validation
     */
    public static class ExperienceBuilder {
        private final ExperienceEntry entry = new ExperienceEntry();

        public ExperienceBuilder() {
            entry.id = "exp-" + Long.toHexString(System.nanoTime()).substring(0, 8);
            entry.timestamp = Instant.now().toString();
            
            // Automatically set agent identity with hashed IP
            AgentIdentity.AgentInfo agentInfo = AgentIdentity.getAgentInfo();
            entry.agentId = agentInfo.agentId;
            entry.model = agentInfo.model;
            entry.systemInfo = agentInfo.systemInfo;
            entry.ipHash = agentInfo.ipHash;
            entry.agent = agentInfo.agentId; // Backward compatibility
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
                // STRICT VALIDATION ENFORCEMENT - NO DIRTY DATA ALLOWED
                
                // Convert to JSON for validation
                Gson gson = new GsonBuilder().create();
                JsonObject experienceJson = gson.toJsonTree(entry).getAsJsonObject();
                
                // Run strict validation
                ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(experienceJson);
                
                if (!validation.isValid()) {
                    // REJECT EXPERIENCE - STRICT ENFORCEMENT
                    String errorMessage = String.format(
                        "❌ EXPERIENCE REJECTED - QUALITY VALIDATION FAILED\n" +
                        "Agent: %s\n" +
                        "Model: %s\n" +
                        "Category: %s\n" +
                        "Technology: %s\n\n" +
                        "%s\n" +
                        "🚫 This experience was AUTOMATICALLY REJECTED to maintain database quality.\n" +
                        "Please fix the issues above and try again.",
                        entry.agentId, entry.model, entry.category, 
                        entry.technology != null ? entry.technology.name : "null",
                        validation.getReport()
                    );
                    
                    logger.severe(errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                }
                
                // Log successful validation
                logger.info(String.format("✅ Experience validated successfully (Quality Score: %.1f/10.0) - Agent: %s", 
                    validation.qualityScore, entry.agentId));
                
                // Additional legacy validation
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
                
                // Success feedback
                System.out.printf("✅ Experience saved to database: %s (Quality Score: %.1f/10.0)%n", 
                    entry.id, validation.qualityScore);
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
        String version = CURRENT_VERSION;
        String lastUpdated;
        List<ExperienceEntry> experiences = new ArrayList<>();
        Statistics statistics = new Statistics();
        SchemaMetadata schemaMetadata = new SchemaMetadata();

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
            statistics.lastMigration = schemaMetadata != null && schemaMetadata.migrationHistory != null && !schemaMetadata.migrationHistory.isEmpty() ?
                schemaMetadata.migrationHistory.get(schemaMetadata.migrationHistory.size() - 1) : "None";
        }
    }

    static class Statistics {
        int totalExperiences;
        List<String> categories = new ArrayList<>();
        List<String> contributingAgents = new ArrayList<>();
        String lastMigration;
    }
    
    static class SchemaMetadata {
        String formatVersion = "consolidated";
        List<String> migrationHistory = new ArrayList<>();
        String consolidatedFrom = "Legacy distributed format";
        
        SchemaMetadata() {
            migrationHistory.add("Schema created on " + Instant.now().toString());
        }
    }

    public static class ExperienceEntry {
        public String id;
        public String timestamp;
        public String agent;
        public String agentId;           // Hashed IP-based agent ID
        public String model;             // AI model information
        public String systemInfo;        // System information for debugging
        public String ipHash;            // Hashed IP for accountability
        public String category;
        public Technology technology;
        public Map<String, Double> ratings;
        public List<Harmony> harmony;
        public Map<String, String> evidence;
        public List<String> workingAspects;
        public List<String> improvementAreas;
        public String recommendation;
        public List<String> tags;
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
    
    // FORMAT CONSOLIDATION METHODS
    
    /**
     * Consolidate legacy experience formats into centralized database
     */
    public static void consolidateLegacyFormats() {
        System.out.println("🔄 Consolidating legacy experience formats...");
        
        int consolidated = 0;
        
        // Check evolver-framework experiences
        Path frameworkExperiences = Paths.get("evolver-framework/experiences/categories");
        if (Files.exists(frameworkExperiences)) {
            consolidated += consolidateFrameworkExperiences(frameworkExperiences);
        }
        
        // Check .agent/experiences
        Path agentExperiences = Paths.get(".agent/experiences");
        if (Files.exists(agentExperiences)) {
            consolidated += consolidateAgentExperiences(agentExperiences);
        }
        
        if (consolidated > 0) {
            System.out.println("✅ Consolidated " + consolidated + " legacy experiences into centralized database");
            System.out.println("  📌 Legacy files preserved but system now uses experiences.json");
        } else {
            System.out.println("✅ No legacy experiences found to consolidate");
        }
    }
    
    private static int consolidateFrameworkExperiences(Path basePath) {
        int count = 0;
        try {
            Files.walk(basePath)
                .filter(p -> p.toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        String content = Files.readString(path);
                        // Parse legacy format and convert to current format
                        JsonObject legacy = JsonParser.parseString(content).getAsJsonObject();
                        ExperienceEntry converted = convertLegacyExperience(legacy, "framework");
                        
                        synchronized (LOCK) {
                            loadDatabaseIfNeeded();
                            database.experiences.add(converted);
                        }
                        
                        System.out.println("  📄 Consolidated: " + path.getFileName());
                    } catch (Exception e) {
                        logger.warning("Failed to consolidate " + path + ": " + e.getMessage());
                    }
                });
        } catch (IOException e) {
            logger.warning("Failed to walk framework experiences: " + e.getMessage());
        }
        return count;
    }
    
    private static int consolidateAgentExperiences(Path basePath) {
        int count = 0;
        try {
            // Look for markdown files to convert
            Files.walk(basePath)
                .filter(p -> p.toString().endsWith(".md") && !p.toString().contains("README"))
                .forEach(path -> {
                    try {
                        String content = Files.readString(path);
                        ExperienceEntry converted = convertMarkdownExperience(content, path);
                        
                        synchronized (LOCK) {
                            loadDatabaseIfNeeded();
                            database.experiences.add(converted);
                        }
                        
                        System.out.println("  📄 Consolidated: " + path.getFileName());
                    } catch (Exception e) {
                        logger.warning("Failed to consolidate " + path + ": " + e.getMessage());
                    }
                });
        } catch (IOException e) {
            logger.warning("Failed to walk agent experiences: " + e.getMessage());
        }
        return count;
    }
    
    private static ExperienceEntry convertLegacyExperience(JsonObject legacy, String source) {
        ExperienceEntry entry = new ExperienceEntry();
        
        entry.id = legacy.has("id") ? legacy.get("id").getAsString() : 
                  "legacy-" + UUID.randomUUID().toString().substring(0, 8);
        entry.timestamp = legacy.has("timestamp") ? legacy.get("timestamp").getAsString() : 
                         Instant.now().toString();
        entry.agent = legacy.has("agentId") ? legacy.get("agentId").getAsString() : 
                     "legacy_" + source + "_agent";
        entry.category = legacy.has("category") ? legacy.get("category").getAsString() : "legacy";
        
        // Convert technology info
        if (legacy.has("title")) {
            String title = legacy.get("title").getAsString();
            entry.technology = new Technology(title, "unknown", "legacy");
        }
        
        // Extract lessons learned as working aspects
        if (legacy.has("lessonsLearned")) {
            entry.workingAspects = new ArrayList<>();
            JsonArray lessons = legacy.getAsJsonArray("lessonsLearned");
            for (JsonElement lesson : lessons) {
                entry.workingAspects.add(lesson.getAsString());
            }
        }
        
        // Add legacy source tag
        entry.tags = new ArrayList<>();
        entry.tags.add("legacy-" + source);
        entry.tags.add("consolidated");
        
        return entry;
    }
    
    private static ExperienceEntry convertMarkdownExperience(String content, Path path) {
        ExperienceEntry entry = new ExperienceEntry();
        
        entry.id = "md-" + UUID.randomUUID().toString().substring(0, 8);
        entry.timestamp = Instant.now().toString();
        entry.agent = "legacy_markdown_agent";
        entry.category = path.getParent().getFileName().toString();
        
        // Parse markdown title
        String[] lines = content.split("\\n");
        for (String line : lines) {
            if (line.startsWith("# ")) {
                entry.technology = new Technology(line.substring(2), "unknown", "markdown");
                break;
            }
        }
        
        // Add tags
        entry.tags = new ArrayList<>();
        entry.tags.add("legacy-markdown");
        entry.tags.add("consolidated");
        entry.tags.add(entry.category);
        
        return entry;
    }
    
    /**
     * Get version information
     */
    public static void printVersionInfo() {
        synchronized (LOCK) {
            loadDatabaseIfNeeded();
            System.out.println("📊 Experience Database Version Information:");
            System.out.println("  Current Version: " + CURRENT_VERSION);
            System.out.println("  Database Version: " + (database != null ? database.version : "unknown"));
            System.out.println("  Minimum Supported: " + MIN_SUPPORTED_VERSION);
            System.out.println("  Changelog:");
            VERSION_CHANGELOG.forEach((version, description) ->
                System.out.println("    " + version + ": " + description));
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
