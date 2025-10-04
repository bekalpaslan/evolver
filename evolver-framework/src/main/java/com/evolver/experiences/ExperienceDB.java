package com.evolver.experiences;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple Experience Database for AI Agents
 * 
 * Easy commands for agents to read, write, and find experiences.
 * No complex APIs - just simple methods that work like a map.
 */
public class ExperienceDB {
    
    private static ExperienceDB instance;
    private final Map<String, Experience> experiences = new ConcurrentHashMap<>();
    private final Map<ExperienceCategory, Set<String>> categoryIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> tagIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> characteristicIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> projectIndex = new ConcurrentHashMap<>();
    private boolean loaded = false;
    
    private ExperienceDB() {
        // Initialize indexes
        for (ExperienceCategory category : ExperienceCategory.values()) {
            categoryIndex.put(category, ConcurrentHashMap.newKeySet());
        }
    }
    
    public static ExperienceDB getInstance() {
        if (instance == null) {
            synchronized (ExperienceDB.class) {
                if (instance == null) {
                    instance = new ExperienceDB();
                    instance.autoLoad();
                }
            }
        }
        return instance;
    }
    
    /**
     * Auto-load experiences on first access
     */
    private void autoLoad() {
        if (!loaded) {
            try {
                ExperienceDBLoader loader = new ExperienceDBLoader();
                loader.loadAll();
                loaded = true;
            } catch (Exception e) {
                System.err.println("⚠️ Could not auto-load experiences: " + e.getMessage());
                loaded = true; // Don't keep trying
            }
        }
    }
    
    // ============ SIMPLE COMMANDS FOR AGENTS ============
    
    /**
     * Save experience - like putting something in a map
     */
    public boolean save(Experience experience) {
        if (experience == null || experience.getId() == null) {
            return false;
        }
        
        experiences.put(experience.getId(), experience);
        updateIndexes(experience);
        
        System.out.println("✅ Saved: " + experience.getTitle());
        return true;
    }
    
    /**
     * Get experience by ID - like getting from a map
     */
    public Experience get(String id) {
        return experiences.get(id);
    }
    
    /**
     * Find experiences by category - simple lookup
     */
    public List<Experience> findByCategory(ExperienceCategory category) {
        Set<String> ids = categoryIndex.getOrDefault(category, Collections.emptySet());
        return ids.stream()
                  .map(experiences::get)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
    }
    
    /**
     * Find experiences by agent characteristic - simple lookup
     */
    public List<Experience> findByCharacteristic(String characteristic) {
        Set<String> ids = characteristicIndex.getOrDefault(characteristic, Collections.emptySet());
        return ids.stream()
                  .map(experiences::get)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
    }
    
    /**
     * Find experiences by project type - simple lookup
     */
    public List<Experience> findByProject(String projectType) {
        Set<String> ids = projectIndex.getOrDefault(projectType, Collections.emptySet());
        return ids.stream()
                  .map(experiences::get)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
    }
    
    /**
     * Find experiences by tag - simple lookup
     */
    public List<Experience> findByTag(String tag) {
        Set<String> ids = tagIndex.getOrDefault(tag.toLowerCase(), Collections.emptySet());
        return ids.stream()
                  .map(experiences::get)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
    }
    
    /**
     * Search experiences by text - simple contains check
     */
    public List<Experience> search(String text) {
        String searchText = text.toLowerCase();
        return experiences.values().stream()
                         .filter(exp -> matchesText(exp, searchText))
                         .collect(Collectors.toList());
    }
    
    /**
     * Get all experiences - like getting all values from a map
     */
    public List<Experience> getAll() {
        return new ArrayList<>(experiences.values());
    }
    
    /**
     * Check if similar experience exists - simple check
     */
    public boolean existsSimilar(String title, ExperienceCategory category) {
        String cleanTitle = title.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        
        return findByCategory(category).stream()
               .anyMatch(exp -> {
                   String expTitle = exp.getTitle().toLowerCase().replaceAll("[^a-z0-9\\s]", "");
                   return expTitle.equals(cleanTitle) || 
                          expTitle.contains(cleanTitle) || 
                          cleanTitle.contains(expTitle);
               });
    }
    
    /**
     * Count experiences - simple size check
     */
    public int count() {
        return experiences.size();
    }
    
    /**
     * Count by category - simple category count
     */
    public int countByCategory(ExperienceCategory category) {
        return categoryIndex.getOrDefault(category, Collections.emptySet()).size();
    }
    
    /**
     * List all categories with counts - simple stats
     */
    public Map<ExperienceCategory, Integer> getCategoryCounts() {
        Map<ExperienceCategory, Integer> counts = new HashMap<>();
        for (ExperienceCategory category : ExperienceCategory.values()) {
            counts.put(category, countByCategory(category));
        }
        return counts;
    }
    
    /**
     * Get recent experiences - simple time-based filter
     */
    public List<Experience> getRecent(int limit) {
        return experiences.values().stream()
                         .sorted((e1, e2) -> e2.getTimestamp().compareTo(e1.getTimestamp()))
                         .limit(limit)
                         .collect(Collectors.toList());
    }
    
    /**
     * Get recommended experiences - simple filter
     */
    public List<Experience> getRecommended() {
        return experiences.values().stream()
                         .filter(Experience::isRecommended)
                         .collect(Collectors.toList());
    }
    
    /**
     * Mark as recommended - simple update
     */
    public boolean recommend(String id) {
        Experience exp = experiences.get(id);
        if (exp != null) {
            exp.setRecommended(true);
            return true;
        }
        return false;
    }
    
    /**
     * Remove experience - simple removal
     */
    public boolean remove(String id) {
        Experience exp = experiences.remove(id);
        if (exp != null) {
            removeFromIndexes(exp);
            System.out.println("🗑️ Removed: " + exp.getTitle());
            return true;
        }
        return false;
    }
    
    /**
     * Clear all experiences - simple clear
     */
    public void clear() {
        experiences.clear();
        categoryIndex.values().forEach(Set::clear);
        tagIndex.clear();
        characteristicIndex.clear();
        projectIndex.clear();
        System.out.println("🧹 Cleared all experiences");
    }
    
    // ============ HELPER METHODS ============
    
    private void updateIndexes(Experience experience) {
        String id = experience.getId();
        
        // Category index
        if (experience.getCategory() != null) {
            categoryIndex.get(experience.getCategory()).add(id);
        }
        
        // Characteristic index
        if (experience.getAgentCharacteristic() != null) {
            characteristicIndex.computeIfAbsent(experience.getAgentCharacteristic(), 
                                              k -> ConcurrentHashMap.newKeySet()).add(id);
        }
        
        // Project index
        if (experience.getProjectType() != null) {
            projectIndex.computeIfAbsent(experience.getProjectType(), 
                                       k -> ConcurrentHashMap.newKeySet()).add(id);
        }
        
        // Tag index
        if (experience.getTags() != null) {
            for (String tag : experience.getTags()) {
                tagIndex.computeIfAbsent(tag.toLowerCase(), 
                                       k -> ConcurrentHashMap.newKeySet()).add(id);
            }
        }
    }
    
    private void removeFromIndexes(Experience experience) {
        String id = experience.getId();
        
        // Remove from all indexes
        categoryIndex.values().forEach(set -> set.remove(id));
        characteristicIndex.values().forEach(set -> set.remove(id));
        projectIndex.values().forEach(set -> set.remove(id));
        tagIndex.values().forEach(set -> set.remove(id));
    }
    
    private boolean matchesText(Experience exp, String searchText) {
        return (exp.getTitle() != null && exp.getTitle().toLowerCase().contains(searchText)) ||
               (exp.getDescription() != null && exp.getDescription().toLowerCase().contains(searchText)) ||
               (exp.getSituation() != null && exp.getSituation().toLowerCase().contains(searchText)) ||
               (exp.getApproach() != null && exp.getApproach().toLowerCase().contains(searchText)) ||
               (exp.getOutcome() != null && exp.getOutcome().toLowerCase().contains(searchText));
    }
    
    // ============ CONVENIENCE METHODS FOR AGENTS ============
    
    /**
     * Quick save with minimal info - agents can use this for simple saves
     */
    public boolean quickSave(String agentId, String characteristic, ExperienceCategory category, 
                           String title, String description) {
        Experience exp = new Experience(agentId, characteristic, category, title, description);
        return save(exp);
    }
    
    /**
     * Find experiences by category - for enum compatibility
     */
    public List<Experience> find(ExperienceCategory category) {
        return findByCategory(category);
    }
    
    /**
     * What I need method with string parameters for agent commands
     */
    public List<Experience> whatINeed(String myCharacteristic, String categoryName, String projectType) {
        try {
            ExperienceCategory category = ExperienceCategory.valueOf(categoryName.toUpperCase());
            return findWhatINeed(myCharacteristic, category, projectType);
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Unknown category: " + categoryName);
            return Collections.emptyList();
        }
    }
    
    /**
     * Check if experience exists by title and category string
     */
    public boolean exists(String title, String categoryName) {
        try {
            ExperienceCategory category = ExperienceCategory.valueOf(categoryName.toUpperCase());
            return existsSimilar(title, category);
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Unknown category: " + categoryName);
            return false;
        }
    }

    /**
     * Find what I need - smart search for agents
     */
    public List<Experience> findWhatINeed(String myCharacteristic, ExperienceCategory category, String projectType) {
        List<Experience> results = new ArrayList<>();
        
        // First: Same characteristic + category
        results.addAll(findByCharacteristic(myCharacteristic).stream()
                      .filter(exp -> exp.getCategory() == category)
                      .collect(Collectors.toList()));
        
        // Second: Same project type + category
        if (projectType != null) {
            results.addAll(findByProject(projectType).stream()
                          .filter(exp -> exp.getCategory() == category)
                          .filter(exp -> !results.contains(exp))
                          .collect(Collectors.toList()));
        }
        
        // Third: Just category
        results.addAll(findByCategory(category).stream()
                      .filter(exp -> !results.contains(exp))
                      .limit(5) // Don't overwhelm with too many
                      .collect(Collectors.toList()));
        
        return results;
    }
    
    /**
     * Show stats - simple overview for agents
     */
    public void showStats() {
        System.out.println("📊 Experience Database Stats:");
        System.out.println("  Total experiences: " + count());
        System.out.println("  Recommended: " + getRecommended().size());
        
        System.out.println("  By category:");
        getCategoryCounts().forEach((category, count) -> {
            if (count > 0) {
                System.out.println("    " + category.getDisplayName() + ": " + count);
            }
        });
    }
}
