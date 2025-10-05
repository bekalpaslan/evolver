package com.evolver.injection;

/**
 * Merges the Evolver framework with existing project frameworks
 */
public class FrameworkMerger {

    public MergeResult merge(ProjectType projectType, FrameworkType existingFramework, String projectRoot) {
        // Simple merge logic - in real implementation this would be more complex
        boolean success = true;
        String message = "Framework merged successfully";

        if (existingFramework != FrameworkType.NONE) {
            message += " with existing " + existingFramework;
        }

        return new MergeResult(success, message);
    }
}