package com.evolver.injection;

/**
 * Factory for creating learning docks
 */
public class LearningDockFactory {

    public LearningDock createDock(String dockName, String projectRoot, ProjectType projectType, FrameworkType frameworkType) {
        return new LearningDock(dockName, projectRoot, projectType, frameworkType);
    }
}