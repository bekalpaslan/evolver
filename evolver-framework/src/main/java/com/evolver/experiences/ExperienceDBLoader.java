package com.evolver.experiences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Database Loader - Automatically loads experiences from JSON files
 * 
 * Simple loader that reads experiences from the file system
 * and populates the in-memory database for fast access.
 */
public class ExperienceDBLoader {
    
    private static final String EXPERIENCES_DIR = "experiences/categories";
    private final ObjectMapper objectMapper;
    private final ExperienceDB db;
    
    public ExperienceDBLoader() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
        this.db = ExperienceDB.getInstance();
    }
    
    /**
     * Load all experiences from file system into memory database
     */
    public void loadAll() {
        try {
            Path experiencesPath = Paths.get(EXPERIENCES_DIR);
            if (!Files.exists(experiencesPath)) {
                System.out.println("📂 No experiences directory found at: " + EXPERIENCES_DIR);
                return;
            }
            
            int loaded = 0;
            
            // Walk through all category directories
            try (Stream<Path> categoryDirs = Files.list(experiencesPath)) {
                for (Path categoryDir : categoryDirs.filter(Files::isDirectory).toArray(Path[]::new)) {
                    loaded += loadFromCategory(categoryDir);
                }
            }
            
            System.out.println("✅ Loaded " + loaded + " experiences into database");
            
        } catch (IOException e) {
            System.err.println("❌ Error loading experiences: " + e.getMessage());
        }
    }
    
    /**
     * Load experiences from a specific category directory
     */
    private int loadFromCategory(Path categoryDir) throws IOException {
        int count = 0;
        
        try (Stream<Path> files = Files.list(categoryDir)) {
            for (Path file : files.filter(p -> p.toString().endsWith(".json")).toArray(Path[]::new)) {
                try {
                    Experience experience = objectMapper.readValue(file.toFile(), Experience.class);
                    if (experience != null && experience.getId() != null) {
                        db.save(experience);
                        count++;
                    }
                } catch (IOException e) {
                    System.err.println("⚠️ Failed to load experience from " + file + ": " + e.getMessage());
                }
            }
        }
        
        return count;
    }
    
    /**
     * Save all experiences from database to file system
     */
    public void saveAll() {
        try {
            Path experiencesPath = Paths.get(EXPERIENCES_DIR);
            Files.createDirectories(experiencesPath);
            
            int saved = 0;
            
            // Save by category
            for (ExperienceCategory category : ExperienceCategory.values()) {
                Path categoryDir = experiencesPath.resolve(category.name().toLowerCase());
                Files.createDirectories(categoryDir);
                
                for (Experience experience : db.findByCategory(category)) {
                    Path file = categoryDir.resolve(experience.getId() + ".json");
                    objectMapper.writeValue(file.toFile(), experience);
                    saved++;
                }
            }
            
            System.out.println("✅ Saved " + saved + " experiences to file system");
            
        } catch (IOException e) {
            System.err.println("❌ Error saving experiences: " + e.getMessage());
        }
    }
    
    /**
     * Auto-initialize: Load experiences when database is first accessed
     */
    public static void autoLoad() {
        ExperienceDBLoader loader = new ExperienceDBLoader();
        loader.loadAll();
    }
}