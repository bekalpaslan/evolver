package com.evolver.context;

import java.util.*;

/**
 * Represents a request for context gathering
 */
public class ContextRequest {
    private final String taskDescription;
    private final TaskType taskType;
    private final Set<String> focusAreas;
    private final Map<String, Object> parameters;
    private final int tokenBudget;
    private final Set<ContextType> preferredTypes;
    private final Set<ContextType> excludedTypes;
    private final ContextScope scope;

    private ContextRequest(Builder builder) {
        this.taskDescription = builder.taskDescription;
        this.taskType = builder.taskType;
        this.focusAreas = builder.focusAreas;
        this.parameters = builder.parameters;
        this.tokenBudget = builder.tokenBudget;
        this.preferredTypes = builder.preferredTypes;
        this.excludedTypes = builder.excludedTypes;
        this.scope = builder.scope;
    }

    public String getTaskDescription() { return taskDescription; }
    public TaskType getTaskType() { return taskType; }
    public Set<String> getFocusAreas() { return focusAreas; }
    public Map<String, Object> getParameters() { return parameters; }
    public int getTokenBudget() { return tokenBudget; }
    public Set<ContextType> getPreferredTypes() { return preferredTypes; }
    public Set<ContextType> getExcludedTypes() { return excludedTypes; }
    public ContextScope getScope() { return scope; }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String taskDescription;
        private TaskType taskType = TaskType.GENERAL;
        private Set<String> focusAreas = new HashSet<>();
        private Map<String, Object> parameters = new HashMap<>();
        private int tokenBudget = 10000;
        private Set<ContextType> preferredTypes = new HashSet<>();
        private Set<ContextType> excludedTypes = new HashSet<>();
        private ContextScope scope = ContextScope.LOCAL;

        public Builder taskDescription(String description) {
            this.taskDescription = description;
            return this;
        }
        public Builder taskType(TaskType type) {
            this.taskType = type;
            return this;
        }
        public Builder focusAreas(Set<String> areas) {
            this.focusAreas = areas;
            return this;
        }
        public Builder addFocusArea(String area) {
            this.focusAreas.add(area);
            return this;
        }
        public Builder parameters(Map<String, Object> params) {
            this.parameters = params;
            return this;
        }
        public Builder addParameter(String key, Object value) {
            this.parameters.put(key, value);
            return this;
        }
        public Builder tokenBudget(int budget) {
            this.tokenBudget = budget;
            return this;
        }
        public Builder preferredTypes(Set<ContextType> types) {
            this.preferredTypes = types;
            return this;
        }
        public Builder addPreferredType(ContextType type) {
            this.preferredTypes.add(type);
            return this;
        }
        public Builder excludedTypes(Set<ContextType> types) {
            this.excludedTypes = types;
            return this;
        }
        public Builder addExcludedType(ContextType type) {
            this.excludedTypes.add(type);
            return this;
        }
        public Builder scope(ContextScope scope) {
            this.scope = scope;
            return this;
        }

        public ContextRequest build() {
            Objects.requireNonNull(taskDescription, "Task description must not be null");
            return new ContextRequest(this);
        }
    }
}
