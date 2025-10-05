package com.evolver.context;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Formats context fragments into a coherent package for AI consumption
 */
public class ContextFormatter {
    private static final Logger logger = Logger.getLogger(ContextFormatter.class.getName());

    @SuppressWarnings("unused")
    private final ContextConfig config;

    public ContextFormatter(ContextConfig config) {
        this.config = Objects.requireNonNull(config, "ContextConfig cannot be null");
    }

    /**
     * Format fragments into a context package
     */
    public ContextPackage format(List<ContextFragment> fragments, ContextRequest request) {
        if (fragments == null) {
            logger.warning("Received null fragments list, creating empty package");
            return createEmptyPackage(request);
        }

        if (request == null) {
            logger.warning("Received null request, creating empty package");
            return createEmptyPackage(null);
        }

        try {
            // Group fragments by type
            Map<ContextType, List<ContextFragment>> groupedByType = new HashMap<>();
            for (ContextFragment fragment : fragments) {
                if (fragment != null && fragment.getType() != null) {
                    try {
                        groupedByType.computeIfAbsent(fragment.getType(), k -> new ArrayList<>())
                                   .add(fragment);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error grouping fragment by type", e);
                    }
                }
            }

            // Sort groups by importance for the task type
            List<ContextType> typeOrder = getTypeOrderForTask(request.getTaskType());

            // Build sections
            List<ContextSection> sections = new ArrayList<>();
            for (ContextType type : typeOrder) {
                try {
                    List<ContextFragment> typeFragments = groupedByType.get(type);
                    if (typeFragments != null && !typeFragments.isEmpty()) {
                        ContextSection section = buildSection(type, typeFragments);
                        if (section != null) {
                            sections.add(section);
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error building section for type: " + type, e);
                }
            }

            // Add remaining types not in preferred order
            for (Map.Entry<ContextType, List<ContextFragment>> entry : groupedByType.entrySet()) {
                if (entry.getKey() != null && !typeOrder.contains(entry.getKey())) {
                    try {
                        ContextSection section = buildSection(entry.getKey(), entry.getValue());
                        if (section != null) {
                            sections.add(section);
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Error building section for remaining type: " + entry.getKey(), e);
                    }
                }
            }

            ContextPackage contextPackage = ContextPackage.builder()
                .request(request)
                .sections(sections)
                .fragments(fragments.stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .build();

            logger.fine("Formatted " + fragments.size() + " fragments into " + sections.size() + " sections");

            return contextPackage;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Critical error during context formatting", e);
            return createErrorPackage(request, "Formatting failed: " + e.getMessage());
        }
    }

    private ContextPackage createEmptyPackage(ContextRequest request) {
        return ContextPackage.builder()
            .request(request)
            .sections(Collections.emptyList())
            .fragments(Collections.emptyList())
            .build();
    }

    private ContextPackage createErrorPackage(ContextRequest request, String errorMessage) {
        Map<String, Object> errorMetadata = new HashMap<>();
        errorMetadata.put("error", errorMessage);
        errorMetadata.put("timestamp", System.currentTimeMillis());

        return ContextPackage.builder()
            .request(request)
            .sections(Collections.emptyList())
            .fragments(Collections.emptyList())
            .metadata(errorMetadata)
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
