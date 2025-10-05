package com.evolver.cli;

import com.evolver.privacy.PrivacyControls;
import java.nio.file.Paths;

/**
 * Command Line Interface for Evolver Framework
 * 
 * Provides access to:
 * - Privacy controls
 * - Configuration management
 * - Data export/import
 * - Agent interaction
 */
public class EvolverCLI {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        String command = args[0];
        
        try {
            switch (command) {
                case "config":
                    handleConfigCommand(args);
                    break;
                case "export":
                    handleExportCommand(args);
                    break;
                case "purge":
                    handlePurgeCommand(args);
                    break;
                case "status":
                    handleStatusCommand();
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error executing command: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void handleConfigCommand(String[] args) {
        if (args.length < 2) {
            System.err.println("Config command requires an option");
            printConfigUsage();
            return;
        }
        
        String option = args[1];
        
        switch (option) {
            case "--disable-technology-intelligence":
                PrivacyControls.disableTelemetry();
                break;
            case "--enable-technology-intelligence":
                PrivacyControls.enableTelemetry();
                break;
            case "--technology-intelligence-local-only":
                PrivacyControls.setLocalOnlyMode();
                break;
            case "--show":
                PrivacyControls.showPrivacySettings();
                break;
            default:
                System.err.println("Unknown config option: " + option);
                printConfigUsage();
        }
    }
    
    private static void handleExportCommand(String[] args) {
        if (args.length < 2) {
            System.err.println("Export command requires an option");
            printExportUsage();
            return;
        }
        
        String option = args[1];
        
        if ("--technology-experiences".equals(option)) {
            String destination = args.length > 2 ? args[2] : "./my-experiences.json";
            PrivacyControls.exportExperiences(Paths.get(destination));
        } else {
            System.err.println("Unknown export option: " + option);
            printExportUsage();
        }
    }
    
    private static void handlePurgeCommand(String[] args) {
        boolean hasConfirm = false;
        boolean hasTechExperiences = false;
        
        for (String arg : args) {
            if ("--confirm".equals(arg)) hasConfirm = true;
            if ("--technology-experiences".equals(arg)) hasTechExperiences = true;
        }
        
        if (hasTechExperiences) {
            if (hasConfirm) {
                // Skip interactive confirmation since --confirm was provided
                System.out.println("⚠️  Purging technology experiences (--confirm provided)");
                try {
                    java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("experiences.json"));
                    System.out.println("✅ Technology experiences purged");
                } catch (Exception e) {
                    System.err.println("❌ Failed to purge: " + e.getMessage());
                }
            } else {
                PrivacyControls.purgeExperiences();
            }
        } else {
            System.err.println("Purge command requires --technology-experiences option");
            printPurgeUsage();
        }
    }
    
    private static void handleStatusCommand() {
        PrivacyControls.showPrivacySettings();
    }
    
    private static void printUsage() {
        System.out.println("Evolver Framework CLI");
        System.out.println("Usage: evolver <command> [options]");
        System.out.println("");
        System.out.println("Commands:");
        System.out.println("  config    Configure framework settings");
        System.out.println("  export    Export data");
        System.out.println("  purge     Delete data");
        System.out.println("  status    Show current status");
        System.out.println("");
        System.out.println("For help with a specific command:");
        System.out.println("  evolver <command> --help");
    }
    
    private static void printConfigUsage() {
        System.out.println("Config options:");
        System.out.println("  --disable-technology-intelligence    Disable all data collection");
        System.out.println("  --enable-technology-intelligence     Enable data collection");
        System.out.println("  --technology-intelligence-local-only Local data only, no sharing");
        System.out.println("  --show                               Show current settings");
    }
    
    private static void printExportUsage() {
        System.out.println("Export options:");
        System.out.println("  --technology-experiences [file]     Export experiences to file");
        System.out.println("                                       Default: ./my-experiences.json");
    }
    
    private static void printPurgeUsage() {
        System.out.println("Purge options:");
        System.out.println("  --technology-experiences --confirm  Delete all experience data");
        System.out.println("                                       --confirm skips interactive prompt");
    }
}