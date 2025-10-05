package com.evolver.privacy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Privacy Controls for Evolver Framework
 * 
 * Provides user control over:
 * - Technology intelligence collection
 * - Experience data export
 * - Data purging and deletion
 * - Privacy settings configuration
 */
public class PrivacyControls {
    
    private static final Path PRIVACY_CONFIG = Paths.get(".evolver-privacy");
    private static final Path EXPERIENCES_FILE = Paths.get("experiences.json");
    
    /**
     * Disable technology intelligence collection entirely
     */
    public static void disableTelemetry() {
        try {
            Files.writeString(PRIVACY_CONFIG, "telemetry=disabled\n");
            System.out.println("✅ Technology intelligence disabled");
            System.out.println("   No experience data will be collected");
            System.out.println("   Framework will operate in local-only mode");
            System.out.println("   To re-enable: evolver config --enable-technology-intelligence");
        } catch (IOException e) {
            System.err.println("❌ Failed to disable telemetry: " + e.getMessage());
        }
    }
    
    /**
     * Enable technology intelligence collection
     */
    public static void enableTelemetry() {
        try {
            Files.writeString(PRIVACY_CONFIG, "telemetry=enabled\n");
            System.out.println("✅ Technology intelligence enabled");
            System.out.println("   Framework will collect anonymous technology experiences");
            System.out.println("   Data helps improve technology recommendations");
            System.out.println("   To disable: evolver config --disable-technology-intelligence");
        } catch (IOException e) {
            System.err.println("❌ Failed to enable telemetry: " + e.getMessage());
        }
    }
    
    /**
     * Export technology experiences to specified location
     * 
     * @param destination Where to export the data
     */
    public static void exportExperiences(Path destination) {
        try {
            if (!Files.exists(EXPERIENCES_FILE)) {
                System.out.println("📄 No experiences data found");
                System.out.println("   Experience collection may be disabled");
                System.out.println("   Or no technologies have been evaluated yet");
                return;
            }
            
            Files.copy(EXPERIENCES_FILE, destination);
            
            System.out.println("✅ Exported technology experiences");
            System.out.println("   Source: " + EXPERIENCES_FILE.toAbsolutePath());
            System.out.println("   Destination: " + destination.toAbsolutePath());
            System.out.println("   Format: JSON");
            
            // Show data summary
            showDataSummary();
            
        } catch (IOException e) {
            System.err.println("❌ Failed to export experiences: " + e.getMessage());
            System.err.println("   Check file permissions and destination path");
        }
    }
    
    /**
     * Purge all technology experiences after confirmation
     */
    public static void purgeExperiences() {
        System.out.println("⚠️  WARNING: This will permanently delete ALL technology experiences");
        System.out.println("   This action cannot be undone");
        System.out.println("   Your learning data will be lost");
        System.out.println("");
        System.out.print("Type 'CONFIRM' to proceed with deletion: ");
        
        try (Scanner scanner = new Scanner(System.in)) {
            String confirmation = scanner.nextLine().trim();
            
            if ("CONFIRM".equals(confirmation)) {
                try {
                    if (Files.exists(EXPERIENCES_FILE)) {
                        Files.delete(EXPERIENCES_FILE);
                        System.out.println("✅ Technology experiences purged");
                        System.out.println("   All experience data has been deleted");
                        System.out.println("   Framework will start with clean slate");
                    } else {
                        System.out.println("📄 No experiences data found to purge");
                    }
                    
                    // Also clean up any backup files
                    cleanupBackups();
                    
                } catch (IOException e) {
                    System.err.println("❌ Failed to purge experiences: " + e.getMessage());
                }
            } else {
                System.out.println("❌ Purge cancelled");
                System.out.println("   Your data is safe");
            }
        }
    }
    
    /**
     * Set local-only mode (no sharing with community)
     */
    public static void setLocalOnlyMode() {
        try {
            String config = "telemetry=enabled\nsharing=disabled\n";
            Files.writeString(PRIVACY_CONFIG, config);
            
            System.out.println("🏠 Local-only mode enabled");
            System.out.println("   Technology experiences will be collected");
            System.out.println("   Data will NOT be shared with community");
            System.out.println("   All data stays on your machine");
            
        } catch (IOException e) {
            System.err.println("❌ Failed to set local-only mode: " + e.getMessage());
        }
    }
    
    /**
     * Show current privacy settings
     */
    public static void showPrivacySettings() {
        System.out.println("🔒 Current Privacy Settings");
        System.out.println("========================");
        
        try {
            if (Files.exists(PRIVACY_CONFIG)) {
                String config = Files.readString(PRIVACY_CONFIG);
                
                if (config.contains("telemetry=disabled")) {
                    System.out.println("📊 Technology Intelligence: DISABLED");
                    System.out.println("   No experience data collected");
                } else {
                    System.out.println("📊 Technology Intelligence: ENABLED");
                    System.out.println("   Anonymous technology experiences collected");
                }
                
                if (config.contains("sharing=disabled")) {
                    System.out.println("🌐 Community Sharing: DISABLED");
                    System.out.println("   Data stays local only");
                } else {
                    System.out.println("🌐 Community Sharing: ENABLED");
                    System.out.println("   Data may be shared for community benefit");
                }
            } else {
                System.out.println("📊 Technology Intelligence: ENABLED (default)");
                System.out.println("🌐 Community Sharing: ENABLED (default)");
            }
            
            // Show data summary
            showDataSummary();
            
        } catch (IOException e) {
            System.err.println("❌ Failed to read privacy settings: " + e.getMessage());
        }
        
        System.out.println("");
        System.out.println("Available commands:");
        System.out.println("  evolver config --disable-technology-intelligence");
        System.out.println("  evolver config --technology-intelligence-local-only");
        System.out.println("  evolver export --technology-experiences ./my-data.json");
        System.out.println("  evolver purge --technology-experiences --confirm");
    }
    
    /**
     * Show summary of collected data
     */
    private static void showDataSummary() {
        try {
            if (Files.exists(EXPERIENCES_FILE)) {
                String content = Files.readString(EXPERIENCES_FILE);
                long experienceCount = content.split("\"technology\"").length - 1;
                long fileSize = Files.size(EXPERIENCES_FILE);
                
                System.out.println("");
                System.out.println("📈 Data Summary:");
                System.out.println("   Experiences recorded: " + experienceCount);
                System.out.println("   Database size: " + formatFileSize(fileSize));
                System.out.println("   Location: " + EXPERIENCES_FILE.toAbsolutePath());
            } else {
                System.out.println("");
                System.out.println("📈 Data Summary:");
                System.out.println("   No experience data collected yet");
            }
        } catch (IOException e) {
            System.err.println("⚠️  Failed to read data summary: " + e.getMessage());
        }
    }
    
    /**
     * Clean up backup and temporary files
     */
    private static void cleanupBackups() {
        try {
            Path backupFile = Paths.get("experiences.backup.json");
            if (Files.exists(backupFile)) {
                Files.delete(backupFile);
                System.out.println("   Backup files cleaned up");
            }
        } catch (IOException e) {
            System.err.println("⚠️  Failed to clean backup files: " + e.getMessage());
        }
    }
    
    /**
     * Format file size for human readability
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    /**
     * Check if telemetry is enabled
     * 
     * @return true if technology intelligence collection is enabled
     */
    public static boolean isTelemetryEnabled() {
        try {
            if (Files.exists(PRIVACY_CONFIG)) {
                String config = Files.readString(PRIVACY_CONFIG);
                return !config.contains("telemetry=disabled");
            }
            return true; // Default is enabled
        } catch (IOException e) {
            return true; // Default to enabled if can't read config
        }
    }
    
    /**
     * Check if community sharing is enabled
     * 
     * @return true if community sharing is enabled
     */
    public static boolean isSharingEnabled() {
        try {
            if (Files.exists(PRIVACY_CONFIG)) {
                String config = Files.readString(PRIVACY_CONFIG);
                return !config.contains("sharing=disabled");
            }
            return true; // Default is enabled
        } catch (IOException e) {
            return true; // Default to enabled if can't read config
        }
    }
}