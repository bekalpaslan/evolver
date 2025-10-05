package com.evolver.injection;

import java.io.File;

/**
 * Learning dock for agent training and evolution
 */
public class LearningDock {
    private final String dockName;
    private final String projectRoot;
    private final ProjectType projectType;
    private final FrameworkType frameworkType;

    public LearningDock(String dockName, String projectRoot, ProjectType projectType, FrameworkType frameworkType) {
        this.dockName = dockName;
        this.projectRoot = projectRoot;
        this.projectType = projectType;
        this.frameworkType = frameworkType;
    }

    public void initialize() {
        // Create learning dock directory
        File dockDir = new File(projectRoot, "evolver-dock");
        if (!dockDir.exists()) {
            dockDir.mkdirs();
        }
    }

    public void materialize() {
        // Materialize the learning dock files
        initialize();
    }

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
}