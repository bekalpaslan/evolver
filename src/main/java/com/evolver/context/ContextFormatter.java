package com.evolver.context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Formats context fragments into a coherent package for AI consumption
 */
public class ContextFormatter {
    private final ContextConfig config;

    public ContextFormatter(ContextConfig config) {
        this.config = config;
    }

    /**
     * Format fragments into a context package
     */
    public ContextPackage format(List<ContextFragment> fragments, ContextRequest request) {
        // Group fragments by type
        Map<ContextType, List<ContextFragment>> groupedByType = fragments.stream()
            .collect(Collectors.groupingBy(ContextFragment::getType));

        // Sort groups by importance for the task type
        List<ContextType> typeOrder = getTypeOrderForTask(request.getTaskType());

        // Build sections
        List<ContextSection> sections = new ArrayList<>();
        for (ContextType type : typeOrder) {
            List<ContextFragment> typeFragments = groupedByType.get(type);
            if (typeFragments != null && !typeFragments.isEmpty()) {
                sections.add(buildSection(type, typeFragments));
            }
        }

        // Add remaining types not in preferred order
        for (Map.Entry<ContextType, List<ContextFragment>> entry : groupedByType.entrySet()) {
            if (!typeOrder.contains(entry.getKey())) {
                sections.add(buildSection(entry.getKey(), entry.getValue()));
            }
        }

        return ContextPackage.builder()
            .request(request)
            .sections(sections)
            .fragments(fragments)
            .build();
    }

    /**
     * Get the preferred order of context types for a task type
     */
    private List<ContextType> getTypeOrderForTask(TaskType taskType) {
        switch (taskType) {
            case CODE_GENERATION:
            case CODE_COMPLETION:
                return Arrays.asList(
                    ContextType.TASK_DESCRIPTION,
                    ContextType.CODE_STRUCTURE,
                    ContextType.DOMAIN_PATTERNS,
                    ContextType.CODE_DEPENDENCIES,
                    ContextType.DOMAIN_EXAMPLES,
                    ContextType.PROJECT_CONFIGURATION
                );
            case CODE_REFACTORING:
                return Arrays.asList(
                    ContextType.CODE_IMPLEMENTATION,
                    ContextType.CODE_STRUCTURE,
                    ContextType.DOMAIN_BEST_PRACTICES,
                    ContextType.CODE_DEPENDENCIES,
                    ContextType.VCS_HISTORY
                );
            case BUG_FIXING:
            case ERROR_DIAGNOSIS:
                return Arrays.asList(
                    ContextType.RUNTIME_ERRORS,
                    ContextType.CODE_IMPLEMENTATION,
                    ContextType.RUNTIME_LOGS,
                    ContextType.CODE_DEPENDENCIES,
                    ContextType.RUNTIME_STATE
                );
            case CODE_REVIEW:
            case SECURITY_ANALYSIS:
                return Arrays.asList(
                    ContextType.CODE_IMPLEMENTATION,
                    ContextType.DOMAIN_BEST_PRACTICES,
                    ContextType.CODE_STRUCTURE,
                    ContextType.CODE_DEPENDENCIES,
                    ContextType.VCS_DIFF
                );
            case TEST_GENERATION:
                return Arrays.asList(
                    ContextType.CODE_IMPLEMENTATION,
                    ContextType.CODE_STRUCTURE,
                    ContextType.DOMAIN_EXAMPLES,
                    ContextType.DOMAIN_BEST_PRACTICES
                );
            case DOCUMENTATION:
            case EXPLANATION:
                return Arrays.asList(
                    ContextType.CODE_STRUCTURE,
                    ContextType.CODE_IMPLEMENTATION,
                    ContextType.CODE_COMMENTS,
                    ContextType.PROJECT_DOCUMENTATION
                );
            default:
                return Arrays.asList(
                    ContextType.TASK_DESCRIPTION,
                    ContextType.CODE_STRUCTURE,
                    ContextType.CODE_IMPLEMENTATION
                );
        }
    }

    private ContextSection buildSection(ContextType type, List<ContextFragment> fragments) {
        StringBuilder content = new StringBuilder();
        content.append("### ").append(formatTypeName(type)).append("\n\n");

        for (ContextFragment fragment : fragments) {
            content.append("#### ").append(fragment.getSource()).append("\n");
            if (!fragment.getMetadata().isEmpty()) {
                content.append("*Metadata: ")
                    .append(formatMetadata(fragment.getMetadata()))
                    .append("*\n");
            }
            content.append(fragment.getContent()).append("\n\n");
        }

        return new ContextSection(type, content.toString(), fragments);
    }

    private String formatTypeName(ContextType type) {
        return Arrays.stream(type.name().split("_"))
            .map(word -> word.charAt(0) + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }

    private String formatMetadata(Map<String, Object> metadata) {
        return metadata.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(", "));
    }
}
