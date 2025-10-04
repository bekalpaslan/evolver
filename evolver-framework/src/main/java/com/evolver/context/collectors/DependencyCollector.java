package com.evolver.context.collectors;

import com.evolver.context.*;

/**
 * Collects code dependency information
 */
public class DependencyCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getScope().ordinal() >= ContextScope.LOCAL.ordinal();
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String filePath = (String) request.getParameter("file_path");
        if (filePath == null) {
            return null;
        }

        String dependencies = analyzeDependencies(filePath);

        return ContextFragment.builder()
            .source("DependencyCollector")
            .type(ContextType.CODE_DEPENDENCIES)
            .content(dependencies)
            .addAspect("dependencies")
            .addAspect("imports")
            .relevanceScore(0.7)
            .addMetadata("file", filePath)
            .build();
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "DependencyCollector",
            "Analyzes code dependencies and imports",
            "1.0.0",
            CollectorMetadata.CollectorType.STATIC
        );
    }

    private String analyzeDependencies(String filePath) {
        // Placeholder - would analyze actual imports and dependencies
        return "// Dependencies for: " + filePath + "\n" +
               "// Import statements and dependency graph would be listed here";
    }
}
