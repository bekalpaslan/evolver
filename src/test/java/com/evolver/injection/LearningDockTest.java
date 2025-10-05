package com.evolver.injection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for LearningDock functionality
 */
public class LearningDockTest {

    @TempDir
    Path tempDir;

    @Test
    public void testLearningDockCreation() {
        String projectRoot = tempDir.toString();
        
        LearningDock dock = new LearningDock(
            "test-dock", 
            projectRoot, 
            ProjectType.GRADLE_JAVA, 
            FrameworkType.SPRING_BOOT
        );

        assertEquals("test-dock", dock.getDockName());
        assertEquals(projectRoot, dock.getProjectRoot());
        assertEquals(ProjectType.GRADLE_JAVA, dock.getProjectType());
        assertEquals(FrameworkType.SPRING_BOOT, dock.getFrameworkType());
        assertFalse(dock.isInitialized());
    }

    @Test
    public void testLearningDockInitialization() {
        String projectRoot = tempDir.toString();
        
        LearningDock dock = new LearningDock(
            "test-dock", 
            projectRoot, 
            ProjectType.GRADLE_JAVA, 
            FrameworkType.SPRING_BOOT
        );

        dock.initialize();
        assertTrue(dock.isInitialized());
        
        // Check that evolver-dock directory was created
        File dockDir = new File(projectRoot, "evolver-dock");
        assertTrue(dockDir.exists());
        assertTrue(dockDir.isDirectory());
    }

    @Test
    public void testLearningDockMaterialization() {
        String projectRoot = tempDir.toString();
        
        // Create some project structure
        new File(projectRoot, "src/main/java").mkdirs();
        new File(projectRoot, "src/test/java").mkdirs();
        
        LearningDock dock = new LearningDock(
            "test-dock", 
            projectRoot, 
            ProjectType.GRADLE_JAVA, 
            FrameworkType.SPRING_BOOT
        );

        dock.materialize();
        
        // Check that all expected files were created
        File dockDir = new File(projectRoot, "evolver-dock");
        assertTrue(dockDir.exists());
        
        assertTrue(new File(dockDir, "agents").exists());
        assertTrue(new File(dockDir, "analysis").exists());
        assertTrue(new File(dockDir, "context").exists());
        assertTrue(new File(dockDir, "learning").exists());
        assertTrue(new File(dockDir, "evolution").exists());
        
        assertTrue(new File(dockDir, "dock-config.json").exists());
        assertTrue(new File(dockDir, "LEARNING_GUIDE.md").exists());
        assertTrue(new File(dockDir, "analysis/project-structure.md").exists());
        assertTrue(new File(dockDir, "entry-point-mappings.md").exists());
    }

    @Test
    public void testEntryPointsForJavaProject() {
        String projectRoot = tempDir.toString();
        
        LearningDock dock = new LearningDock(
            "java-dock", 
            projectRoot, 
            ProjectType.GRADLE_JAVA, 
            FrameworkType.SPRING_BOOT
        );

        assertTrue(dock.getEntryPoints().contains("src/main/java"));
        assertTrue(dock.getEntryPoints().contains("src/test/java"));
        assertTrue(dock.getEntryPoints().contains("build.gradle"));
        assertTrue(dock.getEntryPoints().contains("README.md"));
    }

    @Test
    public void testCustomEntryPoints() {
        String projectRoot = tempDir.toString();
        
        LearningDock dock = new LearningDock(
            "custom-dock", 
            projectRoot, 
            ProjectType.GRADLE_JAVA, 
            FrameworkType.SPRING_BOOT
        );

        dock.withEntryPoint("custom/path")
            .withEntryPoints("another/path", "third/path");

        assertTrue(dock.getEntryPoints().contains("custom/path"));
        assertTrue(dock.getEntryPoints().contains("another/path"));
        assertTrue(dock.getEntryPoints().contains("third/path"));
    }
}