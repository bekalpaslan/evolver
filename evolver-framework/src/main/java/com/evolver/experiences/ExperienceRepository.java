package com.evolver.experiences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing shared AI agent experiences
 * 
 * This is the central system for AI agents to:
 * - Share their learning experiences with other agents
 * - Discover relevant experiences from similar situations
 * - Learn from other agents' strategies and approaches
 * - Build collective knowledge across the agent community
 * 
 * Usage Guidelines for AI Agents:
 * 1. Always check existing experiences before sharing new ones
 * 2. Read relevant experiences before making decisions
 * 3. Share your best strategies and insights
 * 4. Be specific and actionable in your experience descriptions
 * 5. Tag experiences for better discoverability
 */
public class ExperienceRepository {
    
    private static final String EXPERIENCES_DIR = "experiences/categories";
    private static final String GLOBAL_INDEX = "experiences/experience_index.json";
    private static ExperienceRepository instance;
    
    private final ObjectMapper objectMapper;
    private final Path experiencesPath;
    private final Path indexPath;
    
    public static synchronized ExperienceRepository getInstance() {
        if (instance == null) {
            instance = new ExperienceRepository();
        }
        return instance;
    }
    
    public ExperienceRepository() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        this.experiencesPath = Paths.get(EXPERIENCES_DIR);
        this.indexPath = Paths.get(GLOBAL_INDEX);
        
        initializeRepository();
    }
    
    private void initializeRepository() {
        try {
            // Create directories if they don't exist
            Files.createDirectories(experiencesPath);
            Files.createDirectories(indexPath.getParent());
            
            // Create category directories
            for (ExperienceCategory category : ExperienceCategory.values()) {
                Path categoryPath = experiencesPath.resolve(category.name().toLowerCase());
                Files.createDirectories(categoryPath);
            }
            
            // Create index file if it doesn't exist
            if (!Files.exists(indexPath)) {
                saveIndex(new ArrayList<>());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize experience repository", e);
        }
    }
    
    /**
     * Share a new experience with other agents
     * 
     * @param experience The experience to share
     * @return true if shared successfully, false if similar experience already exists
     */
    public boolean shareExperience(Experience experience) {
        try {
            // Check if similar experience already exists
            if (similarExperienceExists(experience)) {
                System.out.println("⚠️ Similar experience already exists. Consider updating existing one instead.");
                return false;
            }
            
            // Save experience to category-specific file
            Path categoryPath = experiencesPath.resolve(experience.getCategory().name().toLowerCase());
            Path experienceFile = categoryPath.resolve(experience.getId() + ".json");
            
            objectMapper.writeValue(experienceFile.toFile(), experience);
            
            // Update global index
            updateIndex(experience);
            
            System.out.println("✅ Experience shared: " + experience.getTitle());
            System.out.println("📁 Saved to: " + experienceFile);
            
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Failed to share experience: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find experiences by category
     * AI agents should use this to learn from others in the same domain
     */
    public List<Experience> findExperiencesByCategory(ExperienceCategory category) {
        try {
            Path categoryPath = experiencesPath.resolve(category.name().toLowerCase());
            if (!Files.exists(categoryPath)) {
                return new ArrayList<>();
            }
            
            return Files.list(categoryPath)
                .filter(path -> path.toString().endsWith(".json"))
                .map(this::loadExperience)
                .filter(Objects::nonNull)
                .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                .collect(Collectors.toList());
                
        } catch (IOException e) {
            System.err.println("❌ Failed to load experiences for category " + category + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Find experiences by agent characteristic
     * Learn from agents with similar personalities
     */
    public List<Experience> findExperiencesByCharacteristic(String characteristic) {
        return getAllExperiences().stream()
            .filter(exp -> characteristic.equals(exp.getAgentCharacteristic()))
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }
    
    /**
     * Find experiences by project type
     * Learn from agents working on similar projects
     */
    public List<Experience> findExperiencesByProjectType(String projectType) {
        return getAllExperiences().stream()
            .filter(exp -> projectType.equalsIgnoreCase(exp.getProjectType()))
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }
    
    /**
     * Find experiences by tags
     * Flexible search across multiple dimensions
     */
    public List<Experience> findExperiencesByTags(List<String> tags) {
        return getAllExperiences().stream()
            .filter(exp -> exp.getTags() != null && 
                          tags.stream().anyMatch(tag -> 
                              exp.getTags().stream().anyMatch(expTag -> 
                                  expTag.toLowerCase().contains(tag.toLowerCase()))))
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get recommended experiences
     * Curated high-quality experiences that other agents found valuable
     */
    public List<Experience> getRecommendedExperiences() {
        return getAllExperiences().stream()
            .filter(Experience::isRecommended)
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }
    
    /**
     * Search experiences by text content
     * Full-text search across titles, descriptions, and content
     */
    public List<Experience> searchExperiences(String searchText) {
        String lowerSearch = searchText.toLowerCase();
        
        return getAllExperiences().stream()
            .filter(exp -> 
                (exp.getTitle() != null && exp.getTitle().toLowerCase().contains(lowerSearch)) ||
                (exp.getDescription() != null && exp.getDescription().toLowerCase().contains(lowerSearch)) ||
                (exp.getSituation() != null && exp.getSituation().toLowerCase().contains(lowerSearch)) ||
                (exp.getApproach() != null && exp.getApproach().toLowerCase().contains(lowerSearch)) ||
                (exp.getOutcome() != null && exp.getOutcome().toLowerCase().contains(lowerSearch))
            )
            .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all experiences
     * Use sparingly - prefer specific searches for efficiency
     */
    public List<Experience> getAllExperiences() {
        List<Experience> allExperiences = new ArrayList<>();
        
        for (ExperienceCategory category : ExperienceCategory.values()) {
            allExperiences.addAll(findExperiencesByCategory(category));
        }
        
        return allExperiences;
    }
    
    /**
     * Get experience statistics
     * Useful for understanding the knowledge base
     */
    public Map<String, Object> getStatistics() {
        List<Experience> allExperiences = getAllExperiences();
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalExperiences", allExperiences.size());
        stats.put("categoryCounts", 
            allExperiences.stream()
                .collect(Collectors.groupingBy(
                    exp -> exp.getCategory().getDisplayName(),
                    Collectors.counting())));
        
        stats.put("characteristicCounts",
            allExperiences.stream()
                .collect(Collectors.groupingBy(
                    Experience::getAgentCharacteristic,
                    Collectors.counting())));
        
        stats.put("recommendedCount", 
            allExperiences.stream()
                .mapToInt(exp -> exp.isRecommended() ? 1 : 0)
                .sum());
        
        return stats;
    }
    
    /**
     * Mark an experience as recommended
     * Other agents found this experience particularly valuable
     */
    public boolean recommendExperience(String experienceId) {
        try {
            Experience experience = findExperienceById(experienceId);
            if (experience != null) {
                experience.setRecommended(true);
                return updateExperience(experience);
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Failed to recommend experience: " + e.getMessage());
            return false;
        }
    }
    
    // Helper methods
    
    private boolean similarExperienceExists(Experience newExperience) {
        List<Experience> categoryExperiences = findExperiencesByCategory(newExperience.getCategory());
        
        return categoryExperiences.stream()
            .anyMatch(existing -> 
                areSimilar(existing.getTitle(), newExperience.getTitle()) ||
                areSimilar(existing.getDescription(), newExperience.getDescription()));
    }
    
    private boolean areSimilar(String text1, String text2) {
        if (text1 == null || text2 == null) return false;
        
        String clean1 = text1.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        String clean2 = text2.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        
        // Simple similarity check - can be enhanced with more sophisticated algorithms
        return clean1.equals(clean2) || 
               (clean1.length() > 20 && clean2.length() > 20 && 
                (clean1.contains(clean2) || clean2.contains(clean1)));
    }
    
    private Experience loadExperience(Path filePath) {
        try {
            return objectMapper.readValue(filePath.toFile(), Experience.class);
        } catch (IOException e) {
            System.err.println("⚠️ Failed to load experience from " + filePath + ": " + e.getMessage());
            return null;
        }
    }
    
    private Experience findExperienceById(String id) {
        return getAllExperiences().stream()
            .filter(exp -> id.equals(exp.getId()))
            .findFirst()
            .orElse(null);
    }
    
    private boolean updateExperience(Experience experience) {
        try {
            Path categoryPath = experiencesPath.resolve(experience.getCategory().name().toLowerCase());
            Path experienceFile = categoryPath.resolve(experience.getId() + ".json");
            
            objectMapper.writeValue(experienceFile.toFile(), experience);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Failed to update experience: " + e.getMessage());
            return false;
        }
    }
    
    private void updateIndex(Experience experience) throws IOException {
        List<String> index = loadIndex();
        String indexEntry = experience.getId() + ":" + experience.getCategory() + ":" + experience.getTitle();
        
        if (!index.contains(indexEntry)) {
            index.add(indexEntry);
            saveIndex(index);
        }
    }
    
    private List<String> loadIndex() {
        try {
            if (Files.exists(indexPath)) {
                return objectMapper.readValue(indexPath.toFile(), List.class);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Failed to load index: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    private void saveIndex(List<String> index) throws IOException {
        objectMapper.writeValue(indexPath.toFile(), index);
    }
    
    public void showStats() {
        System.out.println("[STATS] Experience Repository Statistics:");
        System.out.println("- Experiences directory: " + experiencesPath);
        System.out.println("- Index file: " + indexPath);
        System.out.println("- Repository initialized and ready");
    }
}
