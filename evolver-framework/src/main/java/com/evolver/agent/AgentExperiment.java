package com.evolver.agent;

import com.evolver.context.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Agent Experimentation Framework
 *
 * Allows agents to:
 * - Test new strategies
 * - Compare approaches
 * - Measure effectiveness
 * - Promote successful experiments
 */
public class AgentExperiment {

    private final String hypothesis;
    private final ContextCollector baseline;
    private final ContextCollector variant;
    private final List<String> metrics;
    private final int trials;
    private final String experimentId;

    private AgentExperiment(Builder builder) {
        this.hypothesis = builder.hypothesis;
        this.baseline = builder.baseline;
        this.variant = builder.variant;
        this.metrics = builder.metrics;
        this.trials = builder.trials;
        this.experimentId = UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Run the experiment and compare baseline vs variant
     */
    public ExperimentResult run() {
        System.out.println("🧪 EXPERIMENT: " + experimentId);
        System.out.println("Hypothesis: " + hypothesis);
        System.out.println("Trials: " + trials);
        System.out.println("Metrics: " + metrics);
        System.out.println("─".repeat(60) + "\n");

        Map<String, Double> baselineScores = new HashMap<>();
        Map<String, Double> variantScores = new HashMap<>();

        // Run trials
        for (int i = 0; i < trials; i++) {
            // Create test request
            ContextRequest request = createTestRequest(i);

            // Baseline measurement
            if (baseline != null) {
                Map<String, Double> bScores = measureCollector(baseline, request);
                bScores.forEach((metric, score) ->
                    baselineScores.merge(metric, score, Double::sum));
            }

            // Variant measurement
            if (variant != null) {
                Map<String, Double> vScores = measureCollector(variant, request);
                vScores.forEach((metric, score) ->
                    variantScores.merge(metric, score, Double::sum));
            }
        }

        // Calculate averages
        baselineScores.replaceAll((k, v) -> v / trials);
        variantScores.replaceAll((k, v) -> v / trials);

        // Analyze results
        return analyzeResults(baselineScores, variantScores);
    }

    /**
     * Measure collector performance
     */
    private Map<String, Double> measureCollector(ContextCollector collector, ContextRequest request) {
        Map<String, Double> scores = new HashMap<>();

        long startTime = System.nanoTime();

        if (collector.isApplicable(request)) {
            ContextFragment fragment = collector.collect(request);

            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1_000_000.0; // milliseconds

            if (fragment != null) {
                scores.put("relevance", fragment.getRelevanceScore());
                scores.put("tokens", (double) fragment.getEstimatedTokens());
                scores.put("speed", 1000.0 / duration); // ops per second
                scores.put("coverage", (double) fragment.getAspects().size());
            }
        }

        return scores;
    }

    /**
     * Create test request for trial
     */
    private ContextRequest createTestRequest(int trial) {
        return ContextRequest.builder()
            .taskDescription("Test request " + trial)
            .taskType(TaskType.CODE_GENERATION)
            .addParameter("trial", trial)
            .tokenBudget(5000)
            .build();
    }

    /**
     * Analyze experiment results
     */
    private ExperimentResult analyzeResults(
        Map<String, Double> baselineScores,
        Map<String, Double> variantScores
    ) {
        System.out.println("📊 RESULTS:");
        System.out.println("─".repeat(60));

        boolean variantBetter = true;
        double improvementSum = 0;
        int metricCount = 0;

        for (String metric : metrics) {
            Double baselineScore = baselineScores.getOrDefault(metric, 0.0);
            Double variantScore = variantScores.getOrDefault(metric, 0.0);

            double improvement = ((variantScore - baselineScore) / Math.max(baselineScore, 0.001)) * 100;
            improvementSum += improvement;
            metricCount++;

            String symbol = improvement > 0 ? "↑" : "↓";
            String color = improvement > 0 ? "✓" : "✗";

            System.out.printf("  %s %-15s: Baseline=%.2f  Variant=%.2f  %s %.1f%%\n",
                color, metric, baselineScore, variantScore, symbol, Math.abs(improvement));

            if (improvement <= 0) {
                variantBetter = false;
            }
        }

        double avgImprovement = metricCount > 0 ? improvementSum / metricCount : 0;
        boolean significant = Math.abs(avgImprovement) > 5.0; // 5% threshold

        System.out.println("\n📈 Summary:");
        System.out.printf("  Average Improvement: %.1f%%\n", avgImprovement);
        System.out.printf("  Statistical Significance: %s\n", significant ? "YES" : "NO");
        System.out.printf("  Recommendation: %s\n",
            (variantBetter && significant) ? "PROMOTE VARIANT" : "KEEP BASELINE");

        return new ExperimentResult(
            experimentId,
            hypothesis,
            baselineScores,
            variantScores,
            variantBetter,
            significant,
            avgImprovement
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String hypothesis;
        private ContextCollector baseline;
        private ContextCollector variant;
        private List<String> metrics = Arrays.asList("relevance", "speed", "coverage");
        private int trials = 10;

        public Builder hypothesis(String hypothesis) {
            this.hypothesis = hypothesis;
            return this;
        }

        public Builder baseline(ContextCollector baseline) {
            this.baseline = baseline;
            return this;
        }

        public Builder variant(ContextCollector variant) {
            this.variant = variant;
            return this;
        }

        public Builder metrics(List<String> metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder trials(int trials) {
            this.trials = trials;
            return this;
        }

        public AgentExperiment build() {
            Objects.requireNonNull(hypothesis, "Hypothesis required");
            return new AgentExperiment(this);
        }
    }

    /**
     * Experiment result
     */
    public static class ExperimentResult {
        private final String experimentId;
        private final String hypothesis;
        private final Map<String, Double> baselineScores;
        private final Map<String, Double> variantScores;
        private final boolean variantBetter;
        private final boolean significant;
        private final double improvement;

        public ExperimentResult(String experimentId, String hypothesis,
                              Map<String, Double> baselineScores,
                              Map<String, Double> variantScores,
                              boolean variantBetter, boolean significant,
                              double improvement) {
            this.experimentId = experimentId;
            this.hypothesis = hypothesis;
            this.baselineScores = baselineScores;
            this.variantScores = variantScores;
            this.variantBetter = variantBetter;
            this.significant = significant;
            this.improvement = improvement;
        }

        public boolean isSignificant() { return significant; }
        public boolean variantIsBetter() { return variantBetter; }
        public double getImprovement() { return improvement; }

        /**
         * Promote the variant to become the new baseline
         */
        public void promote() {
            System.out.println("\n[ROCKET] PROMOTING VARIANT TO PRODUCTION");
            System.out.println("  Experiment: " + experimentId);
            System.out.println("  Improvement: " + String.format("%.1f%%", improvement));
            System.out.println("  Status: ✅ Variant is now the default");

            // In a real system, this would update configuration
            AgentRuntime.registerExperimentSuccess(experimentId, this);
        }

        /**
         * Share learnings with other agents
         */
        public void publishLearning() {
            System.out.println("\n📢 PUBLISHING LEARNING");
            System.out.println("  Hypothesis: " + hypothesis);
            System.out.println("  Result: " + (variantBetter ? "CONFIRMED" : "REJECTED"));
            System.out.println("  Shared with: All agents");

            AgentRuntime.shareLearning(this);
        }
    }
}
