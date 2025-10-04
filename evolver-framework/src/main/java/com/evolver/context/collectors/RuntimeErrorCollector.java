package com.evolver.context.collectors;

import com.evolver.context.*;

/**
 * Collects runtime error information
 */
public class RuntimeErrorCollector implements ContextCollector {

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getTaskType() == TaskType.BUG_FIXING ||
               request.getTaskType() == TaskType.ERROR_DIAGNOSIS ||
               request.getTaskType() == TaskType.TEST_DEBUGGING;
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String errorLog = (String) request.getParameter("error_log");
        if (errorLog == null) {
            return null;
        }

        String parsedErrors = parseErrorLog(errorLog);

        return ContextFragment.builder()
            .source("RuntimeErrorCollector")
            .type(ContextType.RUNTIME_ERRORS)
            .content(parsedErrors)
            .addAspect("errors")
            .addAspect("exceptions")
            .addAspect("stack_trace")
            .relevanceScore(0.95) // Very high for debugging tasks
            .addMetadata("error_count", countErrors(errorLog))
            .build();
    }

    @Override
    public int getPriority() {
        return 95; // Highest priority for error-related tasks
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "RuntimeErrorCollector",
            "Collects and parses runtime errors and stack traces",
            "1.0.0",
            CollectorMetadata.CollectorType.DYNAMIC
        );
    }

    private String parseErrorLog(String errorLog) {
        // Placeholder - would parse and structure error information
        return "Parsed errors:\n" + errorLog;
    }

    private int countErrors(String errorLog) {
        // Placeholder - would count actual errors
        return errorLog.split("Error|Exception").length - 1;
    }
}
