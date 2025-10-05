package com.evolver.metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.evolver.context.*;
import com.evolver.agent.AgentProgress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Baseline Metrics System
 * 
 * Establishes and tracks baseline performance metrics for:
 * - Context relevance scores
 * - Token efficiency
 * - Coverage percentages
 * - Framework improvement measurement
 */
public class Baseline {
    
    private static final Path BASELINE_FILE = Paths.get(".agent/baseline.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    /**
     * Establish initial baseline metrics by running standard context requests
     */
    public static void establish() {
        System.out.println("📊 Establishing baseline metrics...");
        
        try {
            // Create a standard context request for baseline measurement
            ContextRequest standardRequest = createStandardRequest();
            
            // Create engine with default configuration
            ContextConfig config = ContextConfig.builder()
                .minRelevanceThreshold(0.3)
                .build();
            ContextEngine engine = new ContextEngine(config);
            
            // Gather context and analyze
            ContextPackage context = engine.gatherContext(standardRequest).join();
            ContextMetrics metrics = engine.analyzeContext(context);
            
            // Create baseline metrics
            BaselineMetrics baseline = new BaselineMetrics();
            baseline.relevance = Math.round(metrics.getRelevanceScore() * 10.0) / 10.0; // 0.1 precision
            baseline.tokenEfficiency = calculateTokenEfficiency(metrics);
            baseline.coverage = Math.round(metrics.getCoverage() * 10.0) / 10.0; // 0.1 precision
            baseline.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
            baseline.version = "1.0.0";
            
            // Save baseline
            save(baseline);
            
            System.out.println("📊 Baseline established:");
            System.out.println("  Relevance: " + baseline.relevance + "/10");
            System.out.println("  Token efficiency: " + baseline.tokenEfficiency + " fragments/token");
            System.out.println("  Coverage: " + baseline.coverage + "/1.0");
            System.out.println("  Timestamp: " + baseline.timestamp);
            System.out.println("  Saved to: " + BASELINE_FILE.toAbsolutePath());
            
        } catch (Exception e) {
            System.err.println("❌ Failed to establish baseline: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Measure current performance and compare to baseline
     * 
     * @return The percentage improvement from baseline
     */
    public static double measureImprovement() {
        BaselineMetrics baseline = load();
        if (baseline == null) {
            System.out.println("⚠️  No baseline found. Establishing one now...");
            establish();
            return 0.0; // No improvement yet
        }
        
        try {
            ContextMetrics current = measureCurrent();
            
            double currentRelevance = Math.round(current.getRelevanceScore() * 10.0) / 10.0;
            double improvement = ((currentRelevance - baseline.relevance) / baseline.relevance) * 100.0;
            
            improvement = Math.round(improvement * 10.0) / 10.0; // 0.1 precision
            
            System.out.println("📈 Performance comparison:");
            System.out.println("  Baseline relevance: " + baseline.relevance);
            System.out.println("  Current relevance: " + currentRelevance);
            System.out.println("  Improvement: " + improvement + "%");
            
            // If improvement is significant, record it
            if (improvement > 0.0) {
                AgentProgress progress = AgentProgress.load();
                progress.recordBaselineImprovement(improvement, "context relevance");
            }
            
            return improvement;
            
        } catch (Exception e) {
            System.err.println("❌ Failed to measure improvement: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Measure current performance using the same standard request as baseline
     */
    public static ContextMetrics measureCurrent() {
        ContextRequest standardRequest = createStandardRequest();
        
        ContextConfig config = ContextConfig.builder()
            .minRelevanceThreshold(0.3)
            .build();
        ContextEngine engine = new ContextEngine(config);
        
        ContextPackage context = engine.gatherContext(standardRequest).join();
        return engine.analyzeContext(context);
    }
    
    /**
     * Get the current baseline metrics
     * 
     * @return Current baseline or null if none exists
     */
    public static BaselineMetrics getBaseline() {
        return load();
    }
    
    /**
     * Create a standard context request for consistent measurement
     */
    private static ContextRequest createStandardRequest() {
        return ContextRequest.builder()
            .taskDescription("Analyze the framework's context collection capabilities")
            .taskType(TaskType.CODE_REVIEW)
            .addParameter("focus", "context collection")
            .addParameter("complexity", "medium")
            .tokenBudget(5000)
            .addFocusArea("relevance")
            .addFocusArea("efficiency")
            .build();
    }
    
    /**
     * Calculate token efficiency metric
     */
    private static double calculateTokenEfficiency(ContextMetrics metrics) {
        if (metrics.getTotalTokens() == 0) return 0.0;
        double efficiency = (double) metrics.getFragmentCount() / metrics.getTotalTokens() * 1000.0;
        return Math.round(efficiency * 10.0) / 10.0; // 0.1 precision
    }
    
    /**
     * Save baseline metrics to disk
     */
    private static void save(BaselineMetrics baseline) {
        try {
            Files.createDirectories(BASELINE_FILE.getParent());
            String json = gson.toJson(baseline);
            Files.writeString(BASELINE_FILE, json);
        } catch (IOException e) {
            System.err.println("❌ Failed to save baseline: " + e.getMessage());
        }
    }
    
    /**
     * Load baseline metrics from disk
     */
    private static BaselineMetrics load() {
        try {
            if (Files.exists(BASELINE_FILE)) {
                String json = Files.readString(BASELINE_FILE);
                return gson.fromJson(json, BaselineMetrics.class);
            }
        } catch (IOException e) {
            System.err.println("⚠️  Failed to load baseline: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Print detailed baseline analysis
     */
    public static void printAnalysis() {
        BaselineMetrics baseline = load();
        if (baseline == null) {
            System.out.println("📊 No baseline metrics available");
            System.out.println("   Run: Baseline.establish() to create initial baseline");
            return;
        }
        
        System.out.println("📊 Baseline Metrics Analysis");
        System.out.println("=============================");
        System.out.println("Established: " + baseline.timestamp);
        System.out.println("Version: " + baseline.version);
        System.out.println("");
        System.out.println("Performance Metrics:");
        System.out.println("  Relevance Score: " + baseline.relevance + "/10");
        System.out.println("  Token Efficiency: " + baseline.tokenEfficiency + " fragments/1k tokens");
        System.out.println("  Coverage: " + baseline.coverage + "/1.0");
        
        try {
            ContextMetrics current = measureCurrent();
            double currentRelevance = Math.round(current.getRelevanceScore() * 10.0) / 10.0;
            double currentEfficiency = calculateTokenEfficiency(current);
            double currentCoverage = Math.round(current.getCoverage() * 10.0) / 10.0;
            
            System.out.println("");
            System.out.println("Current Performance:");
            System.out.println("  Relevance Score: " + currentRelevance + "/10 (" + 
                             formatChange(currentRelevance - baseline.relevance) + ")");
            System.out.println("  Token Efficiency: " + currentEfficiency + " fragments/1k tokens (" + 
                             formatChange(currentEfficiency - baseline.tokenEfficiency) + ")");
            System.out.println("  Coverage: " + currentCoverage + "/1.0 (" + 
                             formatChange(currentCoverage - baseline.coverage) + ")");
            
            double overallImprovement = measureImprovement();
            System.out.println("");
            System.out.println("Overall Improvement: " + overallImprovement + "%");
            
            if (overallImprovement >= 20.0) {
                System.out.println("✅ Graduation requirement met! (≥20% improvement)");
            } else {
                System.out.println("⏳ Need " + (20.0 - overallImprovement) + "% more for graduation");
            }
            
        } catch (Exception e) {
            System.err.println("⚠️  Failed to measure current performance: " + e.getMessage());
        }
    }
    
    /**
     * Format change value with appropriate sign and color
     */
    private static String formatChange(double change) {
        if (change > 0) {
            return "+" + change;
        } else if (change < 0) {
            return String.valueOf(change);
        } else {
            return "±0.0";
        }
    }
    
    /**
     * Baseline metrics data structure
     */
    public static class BaselineMetrics {
        public double relevance;
        public double tokenEfficiency;
        public double coverage;
        public String timestamp;
        public String version;
        
        public BaselineMetrics() {}
        
        @Override
        public String toString() {
            return String.format("Baseline{relevance=%.1f, efficiency=%.1f, coverage=%.1f, time=%s}",
                relevance, tokenEfficiency, coverage, timestamp);
        }
    }
}