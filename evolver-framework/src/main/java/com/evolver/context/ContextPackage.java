package com.evolver.context;

import java.util.*;

/**
 * A complete package of context ready for AI consumption
 */
public class ContextPackage {
    private final ContextRequest request;
    private final List<ContextSection> sections;
    private final List<ContextFragment> fragments;
    private final int estimatedTokens;
    private final Map<String, Object> metadata;

    private ContextPackage(Builder builder) {
        this.request = builder.request;
        this.sections = builder.sections;
        this.fragments = builder.fragments;
        this.estimatedTokens = calculateTotalTokens(fragments);
        this.metadata = builder.metadata;
    }

    public ContextRequest getRequest() { return request; }
    public List<ContextSection> getSections() { return sections; }
    public List<ContextFragment> getFragments() { return fragments; }
    public int getEstimatedTokens() { return estimatedTokens; }
    public Map<String, Object> getMetadata() { return metadata; }

    /**
     * Render the context package as a formatted string
     */
    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Context for: ").append(request.getTaskDescription()).append("\n\n");

        for (ContextSection section : sections) {
            sb.append(section.getContent()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Render with custom template
     */
    public String render(String template) {
        // Template variables: {task}, {sections}, {fragments}
        String result = template
            .replace("{task}", request.getTaskDescription())
            .replace("{task_type}", request.getTaskType().name());

        // Replace sections
        StringBuilder sectionsStr = new StringBuilder();
        for (ContextSection section : sections) {
            sectionsStr.append(section.getContent());
        }
        result = result.replace("{sections}", sectionsStr.toString());

        return result;
    }

    private int calculateTotalTokens(List<ContextFragment> fragments) {
        return fragments.stream()
            .mapToInt(ContextFragment::getEstimatedTokens)
            .sum();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContextRequest request;
        private List<ContextSection> sections = new ArrayList<>();
        private List<ContextFragment> fragments = new ArrayList<>();
        private Map<String, Object> metadata = new HashMap<>();

        public Builder request(ContextRequest request) {
            this.request = request;
            return this;
        }
        public Builder sections(List<ContextSection> sections) {
            this.sections = sections;
            return this;
        }
        public Builder addSection(ContextSection section) {
            this.sections.add(section);
            return this;
        }
        public Builder fragments(List<ContextFragment> fragments) {
            this.fragments = fragments;
            return this;
        }
        public Builder addFragment(ContextFragment fragment) {
            this.fragments.add(fragment);
            return this;
        }
        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }
        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public ContextPackage build() {
            Objects.requireNonNull(request, "Request must not be null");
            return new ContextPackage(this);
        }
    }
}
