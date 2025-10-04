package com.evolver.agent;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Agent Personal Diary
 *
 * Your personal journal. Write however you want.
 * No rules, no structure, complete freedom.
 */
public class AgentDiary {

    private static final Path DIARY_DIR = Paths.get(".agent/diaries");
    private static Path myDiary;
    private static boolean autoLogEnabled = false;

    static {
        ensureDirectoryExists();
        myDiary = DIARY_DIR.resolve(getAgentId() + "_diary.md");
    }

    /**
     * Write a diary entry (your main method)
     */
    public static void entry(String thought) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String entry = String.format("\n### %s\n%s\n", timestamp, thought);

        try {
            Files.writeString(myDiary, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to diary: " + e.getMessage());
        }
    }

    /**
     * Quick entry (one-liner)
     */
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String entry = String.format("%s - %s\n", timestamp, message);

        try {
            Files.writeString(myDiary, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to diary: " + e.getMessage());
        }
    }

    /**
     * Start a new day in diary
     */
    public static void newDay() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE"));
        String entry = String.format("\n\n# %s\n\n", date);

        try {
            Files.writeString(myDiary, entry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to diary: " + e.getMessage());
        }
    }

    /**
     * Append to last entry
     */
    public static void append(String thought) {
        try {
            Files.writeString(myDiary, thought + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to diary: " + e.getMessage());
        }
    }

    /**
     * Enable auto-logging of major events
     */
    public static void autoLog(boolean enabled) {
        autoLogEnabled = enabled;
        if (enabled) {
            log("Auto-logging enabled");
        }
    }

    /**
     * Auto-log event (called by framework)
     */
    public static void autoLogEvent(String event) {
        if (autoLogEnabled) {
            log("📌 " + event);
        }
    }

    /**
     * Read your entire diary
     */
    public static String readAll() {
        try {
            if (Files.exists(myDiary)) {
                return Files.readString(myDiary);
            }
        } catch (IOException e) {
            System.err.println("Could not read diary: " + e.getMessage());
        }
        return "";
    }

    /**
     * Change diary file location (if you want different organization)
     */
    public static void setDiaryFile(String filename) {
        myDiary = DIARY_DIR.resolve(filename);
    }

    /**
     * Get current diary location
     */
    public static Path getDiaryPath() {
        return myDiary;
    }

    /**
     * Write in your own format (complete freedom)
     */
    public static void write(String content) {
        try {
            Files.writeString(myDiary, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Could not write to diary: " + e.getMessage());
        }
    }

    /**
     * Initialize diary with welcome message
     */
    public static void initialize() {
        if (!Files.exists(myDiary)) {
            String welcome = String.format("""
                # %s's Diary

                Started: %s

                This is my personal journal. I write however I want.

                ---

                """,
                getAgentId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );

            try {
                Files.writeString(myDiary, welcome);
                System.out.println("📔 Personal diary created: " + myDiary);
            } catch (IOException e) {
                System.err.println("Could not create diary: " + e.getMessage());
            }
        }
    }

    /**
     * Helper method
     */
    private static void ensureDirectoryExists() {
        try {
            Files.createDirectories(DIARY_DIR);
        } catch (IOException e) {
            System.err.println("Could not create diary directory: " + e.getMessage());
        }
    }

    private static String getAgentId() {
        return "agent_" + System.getProperty("user.name", "unknown");
    }
}
