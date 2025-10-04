package com.evolver.agent;

import java.time.LocalDateTime;
import java.util.*;

/**
 * TASK CONTEXT
 * 
 * Tracks ongoing task state and progress for context analysis.
 */
public class TaskContext {
    
    private final String taskId;
    private final LocalDateTime startTime;
    private double progressPercent;
    private double estimatedHours;
    private double businessValue;
    private double documentationScore;
    private double codeQualityContext;
    private final List<String> milestones;
    private final Map<String, Object> metadata;
    
    public TaskContext(String taskId) {
        this.taskId = taskId;
        this.startTime = LocalDateTime.now();
        this.progressPercent = 0.0;
        this.estimatedHours = 8.0; // Default estimate
        this.businessValue = 0.5; // Default medium value
        this.documentationScore = 0.5;
        this.codeQualityContext = 0.5;
        this.milestones = new ArrayList<>();
        this.metadata = new HashMap<>();
    }
    
    public void updateProgress(TaskAnalysisInput input) {
        // Update progress based on input analysis
        // This would typically be calculated based on completed work
    }
    
    // Getters
    public String getTaskId() { return taskId; }
    public LocalDateTime getStartTime() { return startTime; }
    public double getProgressPercent() { return progressPercent; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getBusinessValue() { return businessValue; }
    public double getDocumentationScore() { return documentationScore; }
    public double getCodeQualityContext() { return codeQualityContext; }
    public List<String> getMilestones() { return new ArrayList<>(milestones); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    
    // Setters
    public void setProgressPercent(double progressPercent) { this.progressPercent = progressPercent; }
    public void setEstimatedHours(double estimatedHours) { this.estimatedHours = estimatedHours; }
    public void setBusinessValue(double businessValue) { this.businessValue = businessValue; }
    public void setDocumentationScore(double documentationScore) { this.documentationScore = documentationScore; }
    public void setCodeQualityContext(double codeQualityContext) { this.codeQualityContext = codeQualityContext; }
}