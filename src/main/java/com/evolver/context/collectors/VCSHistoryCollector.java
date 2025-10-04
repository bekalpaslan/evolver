package com.evolver.context.collectors;

import com.evolver.context.*;

/**
 * Collects version control history information
 */
public class VCSHistoryCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getScope().ordinal() >= ContextScope.MODULE.ordinal() &&
               (request.getTaskType() == TaskType.CODE_REFACTORING ||
                request.getTaskType() == TaskType.CODE_REVIEW ||
                request.getTaskType() == TaskType.BUG_FIXING);
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String filePath = (String) request.getParameter("file_path");
        if (filePath == null) {
            return null;
        }

        String history = getGitHistory(filePath);

        return ContextFragment.builder()
            .source("VCSHistoryCollector")
            .type(ContextType.VCS_HISTORY)
            .content(history)
            .addAspect("history")
            .addAspect("git")
            .addAspect("blame")
            .relevanceScore(0.5)
            .addMetadata("file", filePath)
            .build();
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public int getEstimatedCost() {
        return 200; // VCS operations can be expensive
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "VCSHistoryCollector",
            "Collects Git history and blame information",
            "1.0.0",
            CollectorMetadata.CollectorType.EXTERNAL
        );
    }

    private String getGitHistory(String filePath) {
        // Placeholder - would execute git log, git blame
        return "// Git history for: " + filePath + "\n" +
               "// Recent commits and changes would be listed here";
    }
}
