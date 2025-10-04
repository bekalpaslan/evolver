package com.evolver.context;

import java.time.Instant;
import java.util.*;

/**
 * Represents a piece of context collected from a source
 */
public class ContextFragment {
    private final String id;
    private final String source;
    private final ContextType type;
    private final String content;
    private final Map<String, Object> metadata;
    private final Set<String> aspects;
    private final double relevanceScore;
    private final int estimatedTokens;
    private final Instant timestamp;
    private final List<String> dependencies;

    private ContextFragment(Builder builder) {
        this.id = builder.id;
        this.source = builder.source;
        this.type = builder.type;
        this.content = builder.content;
        this.metadata = builder.metadata;
        this.aspects = builder.aspects;
        this.relevanceScore = builder.relevanceScore;
        this.estimatedTokens = builder.estimatedTokens;
        this.timestamp = builder.timestamp;
        this.dependencies = builder.dependencies;
    }

    public String getId() { return id; }
    public String getSource() { return source; }
    public ContextType getType() { return type; }
    public String getContent() { return content; }
    public Map<String, Object> getMetadata() { return metadata; }
    public Set<String> getAspects() { return aspects; }
    public double getRelevanceScore() { return relevanceScore; }
    public int getEstimatedTokens() { return estimatedTokens; }
    public Instant getTimestamp() { return timestamp; }
    public List<String> getDependencies() { return dependencies; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String source;
        private ContextType type;
        private String content;
        private Map<String, Object> metadata = new HashMap<>();
        private Set<String> aspects = new HashSet<>();
        private double relevanceScore = 0.0;
        private int estimatedTokens = 0;
        private Instant timestamp = Instant.now();
        private List<String> dependencies = new ArrayList<>();

        public Builder id(String id) { this.id = id; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder type(ContextType type) { this.type = type; return this; }
        public Builder content(String content) {
            this.content = content;
            this.estimatedTokens = estimateTokens(content);
            return this;
        }
        public Builder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public Builder addMetadata(String key, Object value) { this.metadata.put(key, value); return this; }
        public Builder aspects(Set<String> aspects) { this.aspects = aspects; return this; }
        public Builder addAspect(String aspect) { this.aspects.add(aspect); return this; }
        public Builder relevanceScore(double score) { this.relevanceScore = score; return this; }
        public Builder estimatedTokens(int tokens) { this.estimatedTokens = tokens; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public Builder addDependency(String dependency) { this.dependencies.add(dependency); return this; }

        public ContextFragment build() {
            Objects.requireNonNull(source, "Source must not be null");
            Objects.requireNonNull(type, "Type must not be null");
            Objects.requireNonNull(content, "Content must not be null");
            return new ContextFragment(this);
        }

        private int estimateTokens(String text) {
            // Rough estimation: ~4 characters per token
            return text.length() / 4;
        }
    }
}
