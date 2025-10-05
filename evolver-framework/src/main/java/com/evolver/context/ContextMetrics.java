package com.evolver.context;

/**
 * Metrics about context quality
 */
public class ContextMetrics {
    private final int totalTokens;
    private final int fragmentCount;
    private final double relevanceScore;
    private final double coverage;

    private ContextMetrics(Builder builder) {
        this.totalTokens = builder.totalTokens;
        this.fragmentCount = builder.fragmentCount;
        this.relevanceScore = builder.relevanceScore;
        this.coverage = builder.coverage;
    }

    public int getTotalTokens() { return totalTokens; }
    public int getFragmentCount() { return fragmentCount; }
    public double getRelevanceScore() { return relevanceScore; }
    public double getCoverage() { return coverage; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int totalTokens;
        private int fragmentCount;
        private double relevanceScore;
        private double coverage;

        public Builder totalTokens(int tokens) {
            this.totalTokens = tokens;
            return this;
        }
        public Builder fragmentCount(int count) {
            this.fragmentCount = count;
            return this;
        }
        public Builder relevanceScore(double score) {
            validatePrecision(score, "relevanceScore");
            this.relevanceScore = score;
            return this;
        }
        public Builder coverage(double coverage) {
            validatePrecision(coverage, "coverage");
            this.coverage = coverage;
            return this;
        }

        public ContextMetrics build() {
            return new ContextMetrics(this);
        }
        
        /**
         * Validate that double values use 0.1 precision as required by framework
         */
        private void validatePrecision(double value, String field) {
            double rounded = Math.round(value * 10.0) / 10.0;
            if (Math.abs(value - rounded) > 0.001) {
                throw new IllegalArgumentException(
                    field + " must have 0.1 precision, got: " + value +
                    ". Use: " + rounded
                );
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
            "ContextMetrics{tokens=%d, fragments=%d, relevance=%.2f, coverage=%.2f}",
            totalTokens, fragmentCount, relevanceScore, coverage
        );
    }
}
