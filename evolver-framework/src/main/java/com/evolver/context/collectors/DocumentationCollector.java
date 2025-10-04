package com.evolver.context.collectors;

import com.evolver.context.*;

/**
 * Collects existing documentation and comments
 */
public class DocumentationCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getTaskType() == TaskType.DOCUMENTATION ||
               request.getTaskType() == TaskType.EXPLANATION ||
               request.getTaskType() == TaskType.CODE_GENERATION;
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String projectPath = (String) request.getParameter("project_path");
        if (projectPath == null) {
            return null;
        }

        String documentation = collectDocumentation(projectPath);

        return ContextFragment.builder()
            .source("DocumentationCollector")
            .type(ContextType.PROJECT_DOCUMENTATION)
            .content(documentation)
            .addAspect("documentation")
            .addAspect("comments")
            .addAspect("readme")
            .relevanceScore(0.6)
            .addMetadata("source_path", projectPath)
            .build();
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "DocumentationCollector",
            "Collects existing documentation, README files, and code comments",
            "1.0.0",
            CollectorMetadata.CollectorType.STATIC
        );
    }

    private String collectDocumentation(String projectPath) {
        // Placeholder - would scan for README, docs, javadoc comments
        return "// Documentation from: " + projectPath + "\n" +
               "// README, API docs, and comments would be aggregated here";
    }
}
