package com.evolver.context;

import java.time.Duration;
import java.util.*;

/**
 * Configuration for context engineering
 */
public class ContextConfig {
    private final double minRelevanceThreshold;
    private final Duration contextMaxAge;
    private final List<FilterRule> filterRules;
    private final Set<String> requiredAspects;
    private final double reservedBudgetRatio;
    private final Map<String, Object> customSettings;

    private ContextConfig(Builder builder) {
        this.minRelevanceThreshold = builder.minRelevanceThreshold;
        this.contextMaxAge = builder.contextMaxAge;
        this.filterRules = builder.filterRules;
        this.requiredAspects = builder.requiredAspects;
        this.reservedBudgetRatio = builder.reservedBudgetRatio;
        this.customSettings = builder.customSettings;
    }

    public double getMinRelevanceThreshold() { return minRelevanceThreshold; }
    public Duration getContextMaxAge() { return contextMaxAge; }
    public List<FilterRule> getFilterRules() { return filterRules; }
    public Set<String> getRequiredAspects() { return requiredAspects; }
    public double getReservedBudgetRatio() { return reservedBudgetRatio; }
    public Map<String, Object> getCustomSettings() { return customSettings; }

    public Object getCustomSetting(String key) {
        return customSettings.get(key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private double minRelevanceThreshold = 0.3;
        private Duration contextMaxAge = Duration.ofHours(24);
        private List<FilterRule> filterRules = new ArrayList<>();
        private Set<String> requiredAspects = new HashSet<>();
        private double reservedBudgetRatio = 0.1;
        private Map<String, Object> customSettings = new HashMap<>();

        public Builder minRelevanceThreshold(double threshold) {
            this.minRelevanceThreshold = threshold;
            return this;
        }
        public Builder contextMaxAge(Duration maxAge) {
            this.contextMaxAge = maxAge;
            return this;
        }
        public Builder filterRules(List<FilterRule> rules) {
            this.filterRules = rules;
            return this;
        }
        public Builder addFilterRule(FilterRule rule) {
            this.filterRules.add(rule);
            return this;
        }
        public Builder requiredAspects(Set<String> aspects) {
            this.requiredAspects = aspects;
            return this;
        }
        public Builder addRequiredAspect(String aspect) {
            this.requiredAspects.add(aspect);
            return this;
        }
        public Builder reservedBudgetRatio(double ratio) {
            this.reservedBudgetRatio = ratio;
            return this;
        }
        public Builder customSettings(Map<String, Object> settings) {
            this.customSettings = settings;
            return this;
        }
        public Builder addCustomSetting(String key, Object value) {
            this.customSettings.put(key, value);
            return this;
        }

        public ContextConfig build() {
            return new ContextConfig(this);
        }
    }
}
