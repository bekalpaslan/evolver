package com.evolver.agent.experience;

import com.evolver.agent.experience.ExperienceRepository;

/**
 * Experience Database Management Utility
 * 
 * Provides command-line tools for managing the experience database,
 * including version upgrades, format consolidation, and validation.
 */
public class ExperienceDbManager {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String command = args[0].toLowerCase();
        
        switch (command) {
            case "migrate":
                runMigration();
                break;
            case "consolidate":
                runConsolidation();
                break;
            case "validate":
                runValidation();
                break;
            case "version":
                showVersionInfo();
                break;
            case "stats":
                showStatistics();
                break;
            default:
                System.err.println("Unknown command: " + command);
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("Experience Database Manager");
        System.out.println("==========================");
        System.out.println();
        System.out.println("Usage: java com.evolver.agent.ExperienceDbManager <command>");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  migrate      - Run database migration to latest version");
        System.out.println("  consolidate  - Consolidate legacy formats into centralized database");
        System.out.println("  validate     - Validate database schema and integrity");
        System.out.println("  version      - Show version information");
        System.out.println("  stats        - Show database statistics");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java com.evolver.agent.ExperienceDbManager consolidate");
        System.out.println("  java com.evolver.agent.ExperienceDbManager stats");
    }
    
    private static void runMigration() {
        System.out.println("🔄 Running database migration...");
        try {
            ExperienceRepository.loadOnBootstrap();
            System.out.println("✅ Migration completed successfully");
        } catch (Exception e) {
            System.err.println("❌ Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runConsolidation() {
        System.out.println("🔄 Running format consolidation...");
        try {
            ExperienceRepository.consolidateLegacyFormats();
            ExperienceRepository.loadOnBootstrap(); // Reload to save consolidated data
            System.out.println("✅ Consolidation completed successfully");
        } catch (Exception e) {
            System.err.println("❌ Consolidation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runValidation() {
        System.out.println("🔍 Running database validation...");
        try {
            ExperienceRepository.loadOnBootstrap();
            ExperienceRepository.printStats();
            System.out.println("✅ Database validation completed successfully");
        } catch (Exception e) {
            System.err.println("❌ Validation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showVersionInfo() {
        ExperienceRepository.printVersionInfo();
    }
    
    private static void showStatistics() {
        ExperienceRepository.printStats();
    }
}