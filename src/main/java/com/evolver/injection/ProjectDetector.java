package com.evolver.injection;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * PROJECT DETECTOR
 * 
 * Automatically detects project type and existing frameworks.
 * Understands Maven, Gradle, npm, Python, .NET, and more.
 */
public class ProjectDetector {
    
    public ProjectScan scanProject(String projectRoot) {
        File root = new File(projectRoot);
        ProjectType projectType = detectProjectType(root);
        FrameworkType existingFramework = detectExistingFramework(root, projectType);
        Map<String, Object> metadata = collectMetadata(root, projectType);
        
        return new ProjectScan(projectType, existingFramework, metadata);
    }
    
    private ProjectType detectProjectType(File root) {
        // Java projects
        if (new File(root, "pom.xml").exists()) return ProjectType.MAVEN_JAVA;
        if (new File(root, "build.gradle").exists()) return ProjectType.GRADLE_JAVA;
        if (new File(root, "build.gradle.kts").exists()) return ProjectType.GRADLE_KOTLIN;
        
        // JavaScript/TypeScript
        if (new File(root, "package.json").exists()) {
            try {
                String packageJson = Files.readString(Paths.get(root.getPath(), "package.json"));
                if (packageJson.contains("\"react\"")) return ProjectType.REACT;
                if (packageJson.contains("\"vue\"")) return ProjectType.VUE;
                if (packageJson.contains("\"angular\"")) return ProjectType.ANGULAR;
                if (packageJson.contains("\"express\"")) return ProjectType.NODE_EXPRESS;
                return ProjectType.NODE_JS;
            } catch (Exception e) {
                return ProjectType.NODE_JS;
            }
        }
        
        // Python
        if (new File(root, "requirements.txt").exists()) return ProjectType.PYTHON_PIP;
        if (new File(root, "pyproject.toml").exists()) return ProjectType.PYTHON_POETRY;
        if (new File(root, "setup.py").exists()) return ProjectType.PYTHON_SETUPTOOLS;
        
        // .NET
        if (hasFiles(root, "*.csproj")) return ProjectType.DOTNET_CSHARP;
        if (hasFiles(root, "*.fsproj")) return ProjectType.DOTNET_FSHARP;
        if (new File(root, "*.sln").exists()) return ProjectType.DOTNET_SOLUTION;
        
        // Go
        if (new File(root, "go.mod").exists()) return ProjectType.GO_MODULE;
        
        // Rust
        if (new File(root, "Cargo.toml").exists()) return ProjectType.RUST_CARGO;
        
        // Generic cases
        if (hasFiles(root, "*.java")) return ProjectType.JAVA_GENERIC;
        if (hasFiles(root, "*.py")) return ProjectType.PYTHON_GENERIC;
        if (hasFiles(root, "*.js", "*.ts")) return ProjectType.JAVASCRIPT_GENERIC;
        
        return ProjectType.UNKNOWN;
    }
    
    private FrameworkType detectExistingFramework(File root, ProjectType projectType) {
        // Spring Framework
        if (containsInFiles(root, "springframework", "*.java", "*.xml")) {
            return FrameworkType.SPRING;
        }
        
        // React
        if (containsInFiles(root, "react", "package.json")) {
            return FrameworkType.REACT;
        }
        
        // Express
        if (containsInFiles(root, "express", "package.json")) {
            return FrameworkType.EXPRESS;
        }
        
        // Django
        if (containsInFiles(root, "django", "requirements.txt", "*.py")) {
            return FrameworkType.DJANGO;
        }
        
        // Flask
        if (containsInFiles(root, "flask", "requirements.txt", "*.py")) {
            return FrameworkType.FLASK;
        }
        
        // ASP.NET
        if (containsInFiles(root, "Microsoft.AspNetCore", "*.csproj")) {
            return FrameworkType.ASPNET;
        }
        
        return FrameworkType.NONE;
    }
    
    private Map<String, Object> collectMetadata(File root, ProjectType projectType) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("projectRoot", root.getAbsolutePath());
        metadata.put("projectType", projectType);
        
        // Language-specific metadata
        switch (projectType) {
            case MAVEN_JAVA:
                metadata.put("buildSystem", "maven");
                metadata.put("configFile", "pom.xml");
                break;
            case GRADLE_JAVA:
            case GRADLE_KOTLIN:
                metadata.put("buildSystem", "gradle");
                metadata.put("configFile", "build.gradle");
                break;
            case NODE_JS:
            case REACT:
            case VUE:
            case ANGULAR:
                metadata.put("packageManager", detectPackageManager(root));
                metadata.put("configFile", "package.json");
                break;
        }
        
        return metadata;
    }
    
    private String detectPackageManager(File root) {
        if (new File(root, "yarn.lock").exists()) return "yarn";
        if (new File(root, "pnpm-lock.yaml").exists()) return "pnpm";
        return "npm";
    }
    
    private boolean hasFiles(File directory, String... patterns) {
        try {
            for (String pattern : patterns) {
                try (var stream = Files.walk(directory.toPath(), 3)) {
                    if (stream.anyMatch(path -> path.toString().matches(".*" + pattern.replace("*", ".*")))) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }
    
    private boolean containsInFiles(File directory, String searchTerm, String... filePatterns) {
        try {
            for (String pattern : filePatterns) {
                try (var stream = Files.walk(directory.toPath(), 2)) {
                    var matches = stream
                        .filter(path -> path.toString().matches(".*" + pattern.replace("*", ".*")))
                        .limit(10); // Don't scan too many files
                    
                    for (var path : matches.toList()) {
                        try {
                            String content = Files.readString(path);
                            if (content.toLowerCase().contains(searchTerm.toLowerCase())) {
                                return true;
                            }
                        } catch (Exception e) {
                            // Skip unreadable files
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return false;
    }
}