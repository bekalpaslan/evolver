package com.evolver.agent;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

/**
 * Experience Repository - Collective Agent Learning
 *
 * Allows agents to:
 * - Record experiences (issues, solutions, insights)
 * - Search previous experiences
 * - Learn from other agents
 * - Choose recording method
 */
public class ExperienceRepository {

    private static final Path EXPERIENCE_DIR = Paths.get(".agent/experiences");
    private static final Path INDEX_FILE = EXPERIENCE_DIR.resolve("index.json");

    /**
     * Record a new experience
     */
    public static Experience record() {
        return new Experience();
    }

    /**
     * Search experiences by category and tags
     */
    public static List<ExperienceRecord> search(String category, String... tags) {
        ensureDirectoryExists();

        List<ExperienceRecord> results = new ArrayList<>();

        try {
            Files.walk(EXPERIENCE_DIR)
                .filter(p -> p.toString().endsWith(".md") || p.toString().endsWith(".json"))
                .filter(p -> matchesCategory(p, category))
                .filter(p -> matchesTags(p, tags))
                .forEach(p -> {
                    try {
                        results.add(loadExperience(p));
                    } catch (IOException e) {
                        // Skip unreadable files
                    }
                });
        } catch (IOException e) {
            System.err.println("Error searching experiences: " + e.getMessage());
        }

        return results;
    }

    /**
     * Load all relevant experiences for a task
     */
    public static List<ExperienceRecord> loadRelevantExperiences(String taskType, String... focusAreas) {
        return search("all")
            .stream()
            .filter(exp -> isRelevantTo(exp, taskType, focusAreas))
            .sorted((a, b) -> Double.compare(b.relevanceScore, a.relevanceScore))
            .collect(Collectors.toList());
    }

    /**
     * Quick experience recording (append to quick notes)
     */
    public static void quickNote(String note) {
        ensureDirectoryExists();
        Path quickNotes = EXPERIENCE_DIR.resolve("quick_notes.txt");

        String timestamp = Instant.now().toString();
        String entry = String.format("\n%s - %s\n%s\n", timestamp, getAgentId(), note);

        try {
            Files.writeString(quickNotes, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("✓ Quick note recorded");
        } catch (IOException e) {
            System.err.println("Failed to record quick note: " + e.getMessage());
        }
    }

    /**
     * Load experiences on bootstrap
     */
    public static void loadOnBootstrap() {
        ensureDirectoryExists();
        createReadmeIfNeeded();

        System.out.println("📚 Loading agent experiences...");

        try {
            long count = Files.walk(EXPERIENCE_DIR)
                .filter(p -> p.toString().endsWith(".md") || p.toString().endsWith(".json"))
                .count();

            if (count > 0) {
                System.out.println("✓ Found " + count + " experiences from other agents");
                System.out.println("  Read them at: .agent/experiences/");
            } else {
                System.out.println("✓ No previous experiences. You're the first!");
                System.out.println("  Record your learnings for future agents.");
            }
        } catch (IOException e) {
            System.out.println("⚠ Could not load experiences: " + e.getMessage());
        }
    }

    /**
     * Helper: Ensure directory structure exists
     */
    private static void ensureDirectoryExists() {
        try {
            Files.createDirectories(EXPERIENCE_DIR);
            Files.createDirectories(EXPERIENCE_DIR.resolve("performance"));
            Files.createDirectories(EXPERIENCE_DIR.resolve("accuracy"));
            Files.createDirectories(EXPERIENCE_DIR.resolve("bugs"));
            Files.createDirectories(EXPERIENCE_DIR.resolve("strategies"));
            Files.createDirectories(EXPERIENCE_DIR.resolve("experiments"));
        } catch (IOException e) {
            System.err.println("Could not create experience directories: " + e.getMessage());
        }
    }

    private static void createReadmeIfNeeded() {
        // README.md already exists from our setup
    }

    private static boolean matchesCategory(Path path, String category) {
        if ("all".equals(category)) return true;
        return path.toString().contains(category);
    }

    private static boolean matchesTags(Path path, String[] tags) {
        if (tags.length == 0) return true;

        try {
            String content = Files.readString(path).toLowerCase();
            return Arrays.stream(tags)
                .anyMatch(tag -> content.contains(tag.toLowerCase()));
        } catch (IOException e) {
            return false;
        }
    }

    private static ExperienceRecord loadExperience(Path path) throws IOException {
        String content = Files.readString(path);
        return new ExperienceRecord(
            path.getFileName().toString(),
            extractCategory(path),
            content,
            0.8 // Default relevance
        );
    }

    private static String extractCategory(Path path) {
        Path parent = path.getParent();
        if (parent != null && !parent.equals(EXPERIENCE_DIR)) {
            return parent.getFileName().toString();
        }
        return "general";
    }

    private static boolean isRelevantTo(ExperienceRecord exp, String taskType, String[] focusAreas) {
        String lower = exp.content.toLowerCase();
        boolean matchesTask = lower.contains(taskType.toLowerCase());
        boolean matchesFocus = focusAreas.length == 0 ||
            Arrays.stream(focusAreas).anyMatch(area -> lower.contains(area.toLowerCase()));
        return matchesTask || matchesFocus;
    }

    private static String getAgentId() {
        return "agent_" + System.getProperty("user.name", "unknown");
    }

    /**
     * Experience builder
     */
    public static class Experience {
        private String category = "general";
        private String issue;
        private String solution;
        private Map<String, Object> evidence = new HashMap<>();
        private String recommendation;
        private List<String> tags = new ArrayList<>();
        private ExperienceFormat format = ExperienceFormat.MARKDOWN;

        public Experience category(String category) {
            this.category = category;
            return this;
        }

        public Experience issue(String issue) {
            this.issue = issue;
            return this;
        }

        public Experience solution(String solution) {
            this.solution = solution;
            return this;
        }

        public Experience evidence(String metric, Object before, Object after) {
            this.evidence.put(metric + "_before", before);
            this.evidence.put(metric + "_after", after);
            return this;
        }

        public Experience recommendation(String recommendation) {
            this.recommendation = recommendation;
            return this;
        }

        public Experience tag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public Experience format(ExperienceFormat format) {
            this.format = format;
            return this;
        }

        /**
         * Save the experience
         */
        public void save() {
            ensureDirectoryExists();

            String filename = generateFilename();
            Path filepath = EXPERIENCE_DIR.resolve(category).resolve(filename);

            String content = format == ExperienceFormat.JSON ?
                generateJSON() : generateMarkdown();

            try {
                Files.writeString(filepath, content);
                System.out.println("✓ Experience recorded: " + filepath);
                System.out.println("  Other agents will learn from this!");
            } catch (IOException e) {
                System.err.println("Failed to save experience: " + e.getMessage());
            }
        }

        private String generateFilename() {
            String base = issue.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");

            String timestamp = Instant.now().toString().substring(0, 10);
            String ext = format == ExperienceFormat.JSON ? ".json" : ".md";

            return base + "_" + timestamp + ext;
        }

        private String generateMarkdown() {
            StringBuilder sb = new StringBuilder();

            sb.append("# Experience: ").append(issue).append("\n\n");
            sb.append("**Date:** ").append(Instant.now().toString().substring(0, 10)).append("\n");
            sb.append("**Agent:** ").append(getAgentId()).append("\n");
            sb.append("**Category:** ").append(category).append("\n\n");

            sb.append("## Issue Encountered\n");
            sb.append(issue).append("\n\n");

            sb.append("## Solution Applied\n");
            sb.append(solution).append("\n\n");

            if (!evidence.isEmpty()) {
                sb.append("## Evidence\n");
                evidence.forEach((k, v) -> sb.append("- ").append(k).append(": ").append(v).append("\n"));
                sb.append("\n");
            }

            if (recommendation != null) {
                sb.append("## Recommendation\n");
                sb.append(recommendation).append("\n\n");
            }

            if (!tags.isEmpty()) {
                sb.append("## Tags\n");
                tags.forEach(tag -> sb.append("`").append(tag).append("` "));
                sb.append("\n");
            }

            return sb.toString();
        }

        private String generateJSON() {
            return String.format("""
                {
                  "id": "%s",
                  "agent": "%s",
                  "timestamp": "%s",
                  "category": "%s",
                  "issue": "%s",
                  "solution": "%s",
                  "evidence": %s,
                  "recommendation": "%s",
                  "tags": %s
                }
                """,
                UUID.randomUUID().toString().substring(0, 8),
                getAgentId(),
                Instant.now().toString(),
                category,
                issue,
                solution,
                formatEvidence(),
                recommendation != null ? recommendation : "",
                formatTags()
            );
        }

        private String formatEvidence() {
            if (evidence.isEmpty()) return "{}";
            StringBuilder sb = new StringBuilder("{");
            evidence.forEach((k, v) ->
                sb.append("\"").append(k).append("\": \"").append(v).append("\", ")
            );
            sb.setLength(sb.length() - 2);
            sb.append("}");
            return sb.toString();
        }

        private String formatTags() {
            if (tags.isEmpty()) return "[]";
            return "[\"" + String.join("\", \"", tags) + "\"]";
        }
    }

    /**
     * Experience format options
     */
    public enum ExperienceFormat {
        MARKDOWN,  // Human + AI friendly
        JSON       // Machine-readable
    }

    /**
     * Experience record
     */
    public static class ExperienceRecord {
        public final String id;
        public final String category;
        public final String content;
        public final double relevanceScore;

        public ExperienceRecord(String id, String category, String content, double relevanceScore) {
            this.id = id;
            this.category = category;
            this.content = content;
            this.relevanceScore = relevanceScore;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s (relevance: %.2f)", category, id, relevanceScore);
        }
    }
}
