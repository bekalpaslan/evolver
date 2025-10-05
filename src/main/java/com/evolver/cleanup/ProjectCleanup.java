package com.evolver.cleanup;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Cleanup utility to remove OLD files and backups from the project
 */
public class ProjectCleanup {

    public static void main(String[] args) {
        System.out.println("🧹 Project Cleanup - Removing OLD and backup files");
        System.out.println("=".repeat(60));
        
        String projectRoot = System.getProperty("user.dir");
        
        List<String> filesToRemove = new ArrayList<>();
        
        // OLD files to remove
        filesToRemove.add("IMPLEMENTATION_COMPLETE_OLD.md");
        filesToRemove.add("SECURITY_FIXES_OLD.md");
        filesToRemove.add("docs/reference/GRADLE_TASKS_OLD.md");
        filesToRemove.add("docs/guides/QUICK_START_OLD.md");
        filesToRemove.add("docs/development/FRAMEWORK_IMPROVEMENTS_OLD.md");
        
        // Backup files to remove
        filesToRemove.add("experiences.backup.json");
        filesToRemove.add("experiences.json.backup.1759639362659");
        
        int removedCount = 0;
        long totalSize = 0;
        
        for (String filePath : filesToRemove) {
            try {
                Path path = Paths.get(projectRoot, filePath);
                File file = path.toFile();
                
                if (file.exists()) {
                    long fileSize = file.length();
                    
                    if (file.delete()) {
                        System.out.println("🗑️ Removed: " + filePath + " (" + formatSize(fileSize) + ")");
                        removedCount++;
                        totalSize += fileSize;
                    } else {
                        System.err.println("❌ Failed to remove: " + filePath);
                    }
                } else {
                    System.out.println("⚠️ Not found: " + filePath);
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error removing " + filePath + ": " + e.getMessage());
            }
        }
        
        System.out.println();
        System.out.println("✅ Cleanup complete!");
        System.out.println("📊 Removed " + removedCount + " files");
        System.out.println("💾 Freed " + formatSize(totalSize) + " of disk space");
        
        if (removedCount > 0) {
            System.out.println();
            System.out.println("🎯 Next steps:");
            System.out.println("  • Run 'git add .' to stage deletions");
            System.out.println("  • Run 'git commit -m \"🧹 Clean up OLD files and backups\"'");
            System.out.println("  • The project is now cleaner and more organized!");
        }
    }
    
    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " bytes";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
}