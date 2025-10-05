package com.evolver.agent;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Generate agent accountability reports showing contribution quality
 */
public class AgentAccountabilityReport {
    
    private static final String EXPERIENCES_FILE = "experiences.json";
    private static final Gson gson = new GsonBuilder().create();
    
    public static void main(String[] args) {
        try {
            new AgentAccountabilityReport().generateReport();
        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
    
    public void generateReport() throws IOException {
        System.out.println("🤖 AGENT ACCOUNTABILITY REPORT");
        System.out.println("=".repeat(40));
        
        if (!Files.exists(Paths.get(EXPERIENCES_FILE))) {
            System.out.println("No experience database found");
            return;
        }
        
        JsonObject database = gson.fromJson(Files.readString(Paths.get(EXPERIENCES_FILE)), JsonObject.class);
        JsonArray experiences = database.getAsJsonArray("experiences");
        
        if (experiences == null || experiences.size() == 0) {
            System.out.println("No experiences in database");
            return;
        }
        
        Map<String, AgentStats> agentStats = new HashMap<>();
        
        for (JsonElement element : experiences) {
            JsonObject exp = element.getAsJsonObject();
            String agentId = getStringValue(exp, "agentId");
            String model = getStringValue(exp, "model");
            
            final String finalAgentId = agentId != null ? agentId : "unknown";
            final String finalModel = model != null ? model : "unknown";
            
            AgentStats stats = agentStats.computeIfAbsent(finalAgentId, k -> new AgentStats(finalAgentId, finalModel));
            stats.experienceCount++;
            
            // Calculate quality score
            ExperienceValidator.ValidationResult validation = ExperienceValidator.validateExperience(exp);
            stats.totalQualityScore += validation.qualityScore;
            if (validation.isValid()) {
                stats.validExperiences++;
            }
        }
        
        System.out.println("Agent Contributions:");
        agentStats.values().stream()
            .sorted((a, b) -> Integer.compare(b.experienceCount, a.experienceCount))
            .forEach(stats -> {
                double avgQuality = stats.totalQualityScore / stats.experienceCount;
                double validPercent = 100.0 * stats.validExperiences / stats.experienceCount;
                System.out.printf("🤖 %s (%s):%n", stats.agentId, stats.model);
                System.out.printf("   Experiences: %d%n", stats.experienceCount);
                System.out.printf("   Avg Quality: %.1f/10.0%n", avgQuality);
                System.out.printf("   Valid Rate: %.1f%%%n", validPercent);
                System.out.println();
            });
    }
    
    private String getStringValue(JsonObject obj, String key) {
        JsonElement element = obj.get(key);
        return element != null && !element.isJsonNull() && element.isJsonPrimitive() 
            ? element.getAsString() : null;
    }
    
    private static class AgentStats {
        final String agentId;
        final String model;
        int experienceCount;
        int validExperiences;
        double totalQualityScore;
        
        AgentStats(String agentId, String model) {
            this.agentId = agentId;
            this.model = model;
        }
    }
}