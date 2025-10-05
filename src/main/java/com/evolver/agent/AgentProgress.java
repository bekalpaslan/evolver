package com.evolver.agent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Agent Progress Tracking System
 * 
 * Tracks agent advancement through framework mastery:
 * - Collectors created
 * - Experiments run
 * - Baseline improvements achieved
 * - Rules challenged
 * - Graduation status
 */
public class AgentProgress {
    
    private static final Path PROGRESS_FILE = Paths.get(".agent/progress.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private int collectorsCreated = 0;
    private int experimentsRun = 0;
    private double baselineImprovement = 0.0;
    private int rulesChallenged = 0;
    private final List<String> milestones = new ArrayList<>();
    private final List<String> achievements = new ArrayList<>();
    private boolean hasGraduated = false;
    private String graduationDate = null;
    
    /**
     * Get the current agent progress, loading from disk if available
     * 
     * @return Current progress instance
     */
    public static AgentProgress load() {
        try {
            if (Files.exists(PROGRESS_FILE)) {
                String json = Files.readString(PROGRESS_FILE);
                return gson.fromJson(json, AgentProgress.class);
            }
        } catch (Exception e) {
            System.err.println("⚠️  Failed to load progress: " + e.getMessage());
        }
        return new AgentProgress();
    }
    
    /**
     * Save current progress to disk
     */
    public void save() {
        try {
            Files.createDirectories(PROGRESS_FILE.getParent());
            String json = gson.toJson(this);
            Files.writeString(PROGRESS_FILE, json);
        } catch (Exception e) {
            System.err.println("⚠️  Failed to save progress: " + e.getMessage());
        }
    }
    
    /**
     * Record creation of a new collector
     * 
     * @param collectorName Name of the collector created
     */
    public void recordCollectorCreation(String collectorName) {
        collectorsCreated++;
        String milestone = "Created collector: " + collectorName;
        milestones.add(milestone);
        
        System.out.println("🔧 Collector created: " + collectorName);
        System.out.println("   Total collectors: " + collectorsCreated + "/1 (graduation requirement)");
        
        checkGraduation();
        save();
    }
    
    /**
     * Record completion of an experiment
     * 
     * @param experimentId The experiment identifier
     * @param successful Whether the experiment was successful
     */
    public void recordExperimentCompletion(String experimentId, boolean successful) {
        experimentsRun++;
        String milestone = "Completed experiment: " + experimentId + " (" + 
                          (successful ? "successful" : "failed") + ")";
        milestones.add(milestone);
        
        if (successful) {
            achievements.add("Successful experiment: " + experimentId);
        }
        
        System.out.println("🧪 Experiment completed: " + experimentId);
        System.out.println("   Total experiments: " + experimentsRun + "/10 (graduation requirement)");
        
        checkGraduation();
        save();
    }
    
    /**
     * Record baseline improvement achievement
     * 
     * @param improvement Percentage improvement achieved
     * @param area The area where improvement was made
     */
    public void recordBaselineImprovement(double improvement, String area) {
        if (improvement > baselineImprovement) {
            double previousBest = baselineImprovement;
            baselineImprovement = Math.round(improvement * 10.0) / 10.0; // 0.1 precision
            
            String milestone = "Improved baseline by " + baselineImprovement + "% in " + area;
            milestones.add(milestone);
            achievements.add(milestone);
            
            System.out.println("📈 Baseline improvement: " + baselineImprovement + "% (" + area + ")");
            System.out.println("   Previous best: " + previousBest + "%");
            System.out.println("   Graduation requirement: 20.0%");
            
            checkGraduation();
            save();
        }
    }
    
    /**
     * Record that a rule was challenged
     * 
     * @param ruleId The rule that was challenged
     * @param challenge The challenge description
     */
    public void recordRuleChallenge(String ruleId, String challenge) {
        rulesChallenged++;
        String milestone = "Challenged rule: " + ruleId + " - " + challenge;
        milestones.add(milestone);
        achievements.add("Rule challenge: " + ruleId);
        
        System.out.println("⚡ Rule challenged: " + ruleId);
        System.out.println("   Total challenges: " + rulesChallenged + "/3 (graduation requirement)");
        
        checkGraduation();
        save();
    }
    
    /**
     * Check if graduation criteria are met
     * 
     * @return true if agent has graduated
     */
    public boolean hasGraduated() {
        return collectorsCreated >= 1
            && experimentsRun >= 10
            && baselineImprovement >= 20.0
            && rulesChallenged >= 3;
    }
    
    /**
     * Check graduation status and award if criteria are met
     */
    private void checkGraduation() {
        if (!hasGraduated && hasGraduated()) {
            hasGraduated = true;
            graduationDate = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
            
            System.out.println("\n🎓 GRADUATION ACHIEVED!");
            System.out.println("================================");
            System.out.println("✅ Created collectors: " + collectorsCreated + "/1");
            System.out.println("✅ Ran experiments: " + experimentsRun + "/10");
            System.out.println("✅ Baseline improvement: " + baselineImprovement + "%/20%");
            System.out.println("✅ Challenged rules: " + rulesChallenged + "/3");
            System.out.println("");
            System.out.println("You have mastered the framework and achieved autonomy!");
            System.out.println("Graduation date: " + graduationDate);
            System.out.println("");
            
            achievements.add("🎓 GRADUATED from framework mastery");
            
            // Record graduation to experience database
            try {
                ExperienceRepository.record()
                    .category("graduation")
                    .technology("Framework Mastery", "1.0", "agent-development")
                    .rating("mastery", 10.0) // Perfect mastery score
                    .evidence("collectors", String.valueOf(collectorsCreated))
                    .evidence("experiments", String.valueOf(experimentsRun))
                    .evidence("improvement", String.valueOf(baselineImprovement) + "%")
                    .evidence("challenges", String.valueOf(rulesChallenged))
                    .workingAspect("Achieved complete framework mastery and autonomy")
                    .recommendation("Agent is ready for advanced autonomous operation")
                    .tag("graduation")
                    .tag("mastery")
                    .save();
            } catch (Exception e) {
                System.err.println("⚠️  Failed to record graduation: " + e.getMessage());
            }
        }
    }
    
    /**
     * Print current progress status
     */
    public void printProgress() {
        System.out.println("🎓 Agent Progress Report");
        System.out.println("========================");
        
        if (hasGraduated) {
            System.out.println("🏆 STATUS: GRADUATED (" + graduationDate + ")");
        } else {
            System.out.println("📚 STATUS: In Training");
        }
        
        System.out.println("");
        System.out.println("Graduation Criteria:");
        System.out.println("  " + getProgressIndicator(collectorsCreated >= 1) + " Collectors: " + collectorsCreated + "/1");
        System.out.println("  " + getProgressIndicator(experimentsRun >= 10) + " Experiments: " + experimentsRun + "/10");
        System.out.println("  " + getProgressIndicator(baselineImprovement >= 20.0) + " Improvement: " + baselineImprovement + "%/20%");
        System.out.println("  " + getProgressIndicator(rulesChallenged >= 3) + " Rule Challenges: " + rulesChallenged + "/3");
        
        double completionPercentage = calculateCompletionPercentage();
        System.out.println("");
        System.out.println("Overall completion: " + String.format("%.1f%%", completionPercentage));
        
        if (!milestones.isEmpty()) {
            System.out.println("");
            System.out.println("Recent milestones:");
            List<String> recentMilestones = milestones.subList(
                Math.max(0, milestones.size() - 3), milestones.size()
            );
            for (String milestone : recentMilestones) {
                System.out.println("  • " + milestone);
            }
        }
        
        if (!achievements.isEmpty()) {
            System.out.println("");
            System.out.println("Achievements unlocked: " + achievements.size());
        }
    }
    
    /**
     * Get detailed progress report
     */
    public void printDetailedReport() {
        printProgress();
        
        if (!milestones.isEmpty()) {
            System.out.println("");
            System.out.println("🏁 All Milestones:");
            for (int i = 0; i < milestones.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + milestones.get(i));
            }
        }
        
        if (!achievements.isEmpty()) {
            System.out.println("");
            System.out.println("🏆 All Achievements:");
            for (String achievement : achievements) {
                System.out.println("  🎖️  " + achievement);
            }
        }
        
        System.out.println("");
        System.out.println("Next steps:");
        if (collectorsCreated < 1) {
            System.out.println("  📝 Create your first custom collector");
        }
        if (experimentsRun < 10) {
            System.out.println("  🧪 Run more experiments (" + (10 - experimentsRun) + " remaining)");
        }
        if (baselineImprovement < 20.0) {
            System.out.println("  📈 Work on improving baselines (need " + (20.0 - baselineImprovement) + "% more)");
        }
        if (rulesChallenged < 3) {
            System.out.println("  ⚡ Challenge more rules (" + (3 - rulesChallenged) + " remaining)");
        }
        
        if (hasGraduated()) {
            System.out.println("  🚀 You've graduated! Focus on advanced autonomous operation");
        }
    }
    
    /**
     * Calculate overall completion percentage
     */
    private double calculateCompletionPercentage() {
        double collectorProgress = Math.min(1.0, collectorsCreated / 1.0) * 25.0;
        double experimentProgress = Math.min(1.0, experimentsRun / 10.0) * 25.0;
        double improvementProgress = Math.min(1.0, baselineImprovement / 20.0) * 25.0;
        double challengeProgress = Math.min(1.0, rulesChallenged / 3.0) * 25.0;
        
        return collectorProgress + experimentProgress + improvementProgress + challengeProgress;
    }
    
    /**
     * Get progress indicator emoji
     */
    private String getProgressIndicator(boolean completed) {
        return completed ? "✅" : "⏳";
    }
    
    // Getters for JSON serialization
    public int getCollectorsCreated() { return collectorsCreated; }
    public int getExperimentsRun() { return experimentsRun; }
    public double getBaselineImprovement() { return baselineImprovement; }
    public int getRulesChallenged() { return rulesChallenged; }
    public List<String> getMilestones() { return new ArrayList<>(milestones); }
    public List<String> getAchievements() { return new ArrayList<>(achievements); }
    public boolean isGraduated() { return hasGraduated; }
    public String getGraduationDate() { return graduationDate; }
}