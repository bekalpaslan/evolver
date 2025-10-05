package com.evolver.injection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Learning dock for agent training and evolution.
 * 
 * A LearningDock is a specialized environment where AI agents:
 * - Discover project structure and patterns
 * - Learn domain-specific conventions
 * - Build project-aware context collectors
 * - Evolve the framework to match the project
 * 
 * Entry points guide where agents start their discovery process.
 */
public class LearningDock {
    private final String dockName;
    private final String projectRoot;
    private final ProjectType projectType;
    private final FrameworkType frameworkType;
    private final List<String> entryPoints;
    private final List<String> learningPaths;
    private boolean initialized = false;

    public LearningDock(String dockName, String projectRoot, ProjectType projectType, FrameworkType frameworkType) {
        this.dockName = dockName;
        this.projectRoot = projectRoot;
        this.projectType = projectType;
        this.frameworkType = frameworkType;
        this.entryPoints = new ArrayList<>();
        this.learningPaths = new ArrayList<>();
        
        // Set default entry points based on project type
        setDefaultEntryPoints();
    }

    /**
     * Initialize the learning dock directory structure
     */
    public void initialize() {
        if (initialized) return;
        
        try {
            File dockDir = new File(projectRoot, "evolver-dock");
            if (!dockDir.exists()) {
                dockDir.mkdirs();
                System.out.println("🚢 Created learning dock directory: " + dockDir.getAbsolutePath());
            }
            
            // Create subdirectories for agent learning
            createDockStructure(dockDir);
            initialized = true;
            
        } catch (Exception e) {
            System.err.println("⚠️ Failed to initialize learning dock: " + e.getMessage());
        }
    }

    /**
     * Materialize the learning dock by creating all necessary files and configurations
     */
    public void materialize() {
        initialize();
        
        try {
            File dockDir = new File(projectRoot, "evolver-dock");
            
            // Create dock configuration
            createDockConfig(dockDir);
            
            // Create agent learning guides
            createLearningGuides(dockDir);
            
            // Create project analysis files
            createProjectAnalysis(dockDir);
            
            // Create entry point mappings
            createEntryPointMappings(dockDir);
            
            System.out.println("✅ Learning dock materialized successfully");
            System.out.println("📍 Entry point: " + getEntryPoint());
            System.out.println("🎯 Learning paths: " + learningPaths.size());
            
        } catch (Exception e) {
            System.err.println("⚠️ Failed to materialize learning dock: " + e.getMessage());
        }
    }

    /**
     * Add a custom entry point for agent discovery
     */
    public LearningDock withEntryPoint(String path) {
        if (path != null && !entryPoints.contains(path)) {
            entryPoints.add(path);
            System.out.println("📍 Added entry point: " + path);
        }
        return this;
    }

    /**
     * Add multiple entry points
     */
    public LearningDock withEntryPoints(String... paths) {
        for (String path : paths) {
            withEntryPoint(path);
        }
        return this;
    }

    /**
     * Start agent learning process
     */
    public void startLearning() {
        if (!initialized) {
            materialize();
        }
        
        System.out.println("🤖 Starting agent learning in dock: " + dockName);
        System.out.println("📁 Project type: " + projectType);
        System.out.println("🔧 Framework: " + frameworkType);
        System.out.println("🎯 Entry points:");
        
        for (String entryPoint : entryPoints) {
            File entryFile = new File(projectRoot, entryPoint);
            if (entryFile.exists()) {
                System.out.println("  ✅ " + entryPoint + " (exists)");
            } else {
                System.out.println("  ⚠️ " + entryPoint + " (not found)");
            }
        }
        
        // Analyze project structure
        analyzeProjectStructure();
    }

    private void setDefaultEntryPoints() {
        // Add default entry points based on project type
        switch (projectType) {
            case GRADLE_JAVA:
            case MAVEN_JAVA:
                entryPoints.addAll(Arrays.asList(
                    "src/main/java",
                    "src/test/java",
                    "build.gradle",
                    "pom.xml",
                    "README.md",
                    "docs/"
                ));
                learningPaths.addAll(Arrays.asList(
                    "Java source code patterns",
                    "Build configuration",
                    "Test patterns",
                    "Documentation structure"
                ));
                break;
                
            case NODE_JS:
            case REACT:
                entryPoints.addAll(Arrays.asList(
                    "src/",
                    "package.json",
                    "README.md",
                    "docs/",
                    "tests/"
                ));
                learningPaths.addAll(Arrays.asList(
                    "JavaScript/TypeScript patterns",
                    "Package dependencies",
                    "Component structure"
                ));
                break;
                
            case PYTHON_PIP:
            case PYTHON_POETRY:
                entryPoints.addAll(Arrays.asList(
                    "src/",
                    "requirements.txt",
                    "pyproject.toml",
                    "README.md",
                    "docs/",
                    "tests/"
                ));
                learningPaths.addAll(Arrays.asList(
                    "Python module structure",
                    "Package dependencies",
                    "Testing patterns"
                ));
                break;
                
            default:
                entryPoints.addAll(Arrays.asList(
                    "README.md",
                    "docs/",
                    "src/"
                ));
                learningPaths.add("Generic project patterns");
        }
    }

    private void createDockStructure(File dockDir) throws IOException {
        // Create agent workspace directories
        new File(dockDir, "agents").mkdirs();
        new File(dockDir, "analysis").mkdirs();
        new File(dockDir, "context").mkdirs();
        new File(dockDir, "learning").mkdirs();
        new File(dockDir, "evolution").mkdirs();
    }

    private void createDockConfig(File dockDir) throws IOException {
        File configFile = new File(dockDir, "dock-config.json");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("{\n");
            writer.write("  \"dockName\": \"" + dockName + "\",\n");
            writer.write("  \"projectType\": \"" + projectType + "\",\n");
            writer.write("  \"frameworkType\": \"" + frameworkType + "\",\n");
            writer.write("  \"entryPoints\": [\n");
            for (int i = 0; i < entryPoints.size(); i++) {
                writer.write("    \"" + entryPoints.get(i) + "\"");
                if (i < entryPoints.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("  ],\n");
            writer.write("  \"learningPaths\": [\n");
            for (int i = 0; i < learningPaths.size(); i++) {
                writer.write("    \"" + learningPaths.get(i) + "\"");
                if (i < learningPaths.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("  ],\n");
            writer.write("  \"initialized\": true,\n");
            writer.write("  \"version\": \"1.0.0\"\n");
            writer.write("}\n");
        }
    }

    private void createLearningGuides(File dockDir) throws IOException {
        File guideFile = new File(dockDir, "LEARNING_GUIDE.md");
        try (FileWriter writer = new FileWriter(guideFile)) {
            writer.write("# 🤖 Agent Learning Guide for " + dockName + "\n\n");
            writer.write("## Project Overview\n");
            writer.write("- **Type**: " + projectType + "\n");
            writer.write("- **Framework**: " + frameworkType + "\n");
            writer.write("- **Dock**: " + dockName + "\n\n");
            
            writer.write("## Entry Points for Discovery\n\n");
            for (String entryPoint : entryPoints) {
                writer.write("- `" + entryPoint + "` - Agent discovery starting point\n");
            }
            
            writer.write("\n## Learning Paths\n\n");
            for (String learningPath : learningPaths) {
                writer.write("- " + learningPath + "\n");
            }
            
            writer.write("\n## Agent Instructions\n\n");
            writer.write("1. **Discover**: Analyze entry points to understand project structure\n");
            writer.write("2. **Learn**: Identify patterns, conventions, and domain knowledge\n");
            writer.write("3. **Collect**: Build specialized context collectors for this project\n");
            writer.write("4. **Evolve**: Adapt framework to become project-specific expert\n\n");
            
            writer.write("## Evolution Goals\n\n");
            writer.write("- Create project-aware context generation\n");
            writer.write("- Build domain-specific knowledge base\n");
            writer.write("- Develop specialized code understanding\n");
            writer.write("- Optimize for project-specific workflows\n\n");
        }
    }

    private void createProjectAnalysis(File dockDir) throws IOException {
        File analysisDir = new File(dockDir, "analysis");
        
        // Create project structure analysis
        File structureFile = new File(analysisDir, "project-structure.md");
        try (FileWriter writer = new FileWriter(structureFile)) {
            writer.write("# Project Structure Analysis\n\n");
            writer.write("## Detected Structure\n\n");
            
            // Analyze actual project structure
            analyzeDirectoryStructure(writer, new File(projectRoot), "", 0, 3);
            
            writer.write("\n## Key Directories\n\n");
            for (String entryPoint : entryPoints) {
                File entryFile = new File(projectRoot, entryPoint);
                if (entryFile.exists()) {
                    writer.write("- **" + entryPoint + "**: ");
                    if (entryFile.isDirectory()) {
                        writer.write("Directory with " + countFiles(entryFile) + " items\n");
                    } else {
                        writer.write("File (" + entryFile.length() + " bytes)\n");
                    }
                }
            }
        }
    }

    private void createEntryPointMappings(File dockDir) throws IOException {
        File mappingFile = new File(dockDir, "entry-point-mappings.md");
        try (FileWriter writer = new FileWriter(mappingFile)) {
            writer.write("# Entry Point Mappings\n\n");
            writer.write("## Agent Discovery Routes\n\n");
            
            for (String entryPoint : entryPoints) {
                writer.write("### " + entryPoint + "\n\n");
                File entryFile = new File(projectRoot, entryPoint);
                
                if (entryFile.exists()) {
                    if (entryFile.isDirectory()) {
                        writer.write("**Type**: Directory\n");
                        writer.write("**Purpose**: " + getDirectoryPurpose(entryPoint) + "\n");
                        writer.write("**Agent Focus**: " + getAgentFocus(entryPoint) + "\n\n");
                    } else {
                        writer.write("**Type**: File\n");
                        writer.write("**Purpose**: " + getFilePurpose(entryPoint) + "\n");
                        writer.write("**Agent Focus**: " + getAgentFocus(entryPoint) + "\n\n");
                    }
                } else {
                    writer.write("**Status**: Not found\n");
                    writer.write("**Recommendation**: Create this entry point for better agent learning\n\n");
                }
            }
        }
    }

    private void analyzeProjectStructure() {
        System.out.println("🔍 Analyzing project structure...");
        
        int totalFiles = 0;
        int totalDirs = 0;
        
        for (String entryPoint : entryPoints) {
            File entryFile = new File(projectRoot, entryPoint);
            if (entryFile.exists()) {
                if (entryFile.isDirectory()) {
                    totalDirs++;
                    totalFiles += countFiles(entryFile);
                } else {
                    totalFiles++;
                }
            }
        }
        
        System.out.println("📊 Analysis complete:");
        System.out.println("  📁 Directories: " + totalDirs);
        System.out.println("  📄 Files: " + totalFiles);
        System.out.println("  🎯 Entry points: " + entryPoints.size());
    }

    private void analyzeDirectoryStructure(FileWriter writer, File dir, String prefix, int depth, int maxDepth) throws IOException {
        if (depth > maxDepth || !dir.isDirectory()) return;
        
        File[] files = dir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.getName().startsWith(".")) continue; // Skip hidden files
            
            writer.write(prefix + "- " + file.getName());
            if (file.isDirectory()) {
                writer.write("/\n");
                if (depth < maxDepth) {
                    analyzeDirectoryStructure(writer, file, prefix + "  ", depth + 1, maxDepth);
                }
            } else {
                writer.write("\n");
            }
        }
    }

    private int countFiles(File dir) {
        if (!dir.isDirectory()) return 1;
        
        File[] files = dir.listFiles();
        if (files == null) return 0;
        
        int count = 0;
        for (File file : files) {
            if (!file.getName().startsWith(".")) {
                count++;
            }
        }
        return count;
    }

    private String getDirectoryPurpose(String path) {
        if (path.contains("src")) return "Source code and implementation";
        if (path.contains("test")) return "Test code and validation";
        if (path.contains("docs")) return "Documentation and guides";
        if (path.contains("config")) return "Configuration files";
        return "Project component";
    }

    private String getFilePurpose(String path) {
        if (path.endsWith("README.md")) return "Project overview and getting started";
        if (path.endsWith("build.gradle")) return "Gradle build configuration";
        if (path.endsWith("pom.xml")) return "Maven build configuration";
        if (path.endsWith("package.json")) return "Node.js dependencies and scripts";
        return "Project configuration file";
    }

    private String getAgentFocus(String path) {
        if (path.contains("src/main")) return "Learn implementation patterns and architecture";
        if (path.contains("test")) return "Understand testing patterns and coverage";
        if (path.contains("docs")) return "Learn domain knowledge and conventions";
        if (path.endsWith("README.md")) return "Understand project purpose and setup";
        return "Analyze structure and patterns";
    }

    // Getters
    public String getEntryPoint() {
        return projectRoot + "/evolver-dock";
    }

    public String getDockName() {
        return dockName;
    }

    public String getProjectRoot() {
        return projectRoot;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public FrameworkType getFrameworkType() {
        return frameworkType;
    }

    public List<String> getEntryPoints() {
        return new ArrayList<>(entryPoints);
    }

    public List<String> getLearningPaths() {
        return new ArrayList<>(learningPaths);
    }

    public boolean isInitialized() {
        return initialized;
    }
}