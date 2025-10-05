package com.evolver.agent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Multi-Agent Experiment System
 * 
 * Enables multiple agents to:
 * - Collaborate on complex experiments
 * - Share experimental resources
 * - Validate results across agents
 * - Build collective intelligence
 */
public class MultiAgentExperiment {
    
    private static final Map<String, CollaborativeExperiment> activeExperiments = new ConcurrentHashMap<>();
    private static final List<String> collaborationLog = new CopyOnWriteArrayList<>();
    
    /**
     * Create a new multi-agent collaborative experiment
     * 
     * @param hypothesis The shared hypothesis to test
     * @param description Description of the collaborative experiment
     * @return A collaborative experiment builder
     */
    public static CollaborativeExperiment create(String hypothesis, String description) {
        String experimentId = "collab-" + UUID.randomUUID().toString().substring(0, 8);
        CollaborativeExperiment experiment = new CollaborativeExperiment(experimentId, hypothesis, description);
        activeExperiments.put(experimentId, experiment);
        
        System.out.println("🤝 Multi-agent experiment created: " + experimentId);
        System.out.println("   Hypothesis: " + hypothesis);
        System.out.println("   Description: " + description);
        
        log("Created collaborative experiment: " + experimentId);
        return experiment;
    }
    
    /**
     * Get an active collaborative experiment by ID
     * 
     * @param experimentId The experiment identifier
     * @return The collaborative experiment or null if not found
     */
    public static CollaborativeExperiment getExperiment(String experimentId) {
        return activeExperiments.get(experimentId);
    }
    
    /**
     * Get all active collaborative experiments
     * 
     * @return List of active experiments
     */
    public static List<CollaborativeExperiment> getActiveExperiments() {
        return new ArrayList<>(activeExperiments.values());
    }
    
    /**
     * Get collaboration history log
     * 
     * @return List of collaboration events
     */
    public static List<String> getCollaborationLog() {
        return new ArrayList<>(collaborationLog);
    }
    
    private static void log(String event) {
        collaborationLog.add(new Date() + ": " + event);
    }
    
    /**
     * A collaborative experiment involving multiple agents
     */
    public static class CollaborativeExperiment {
        private final String experimentId;
        private final String hypothesis;
        private final String description;
        private final long startTime;
        private final List<String> participatingAgents = new CopyOnWriteArrayList<>();
        private final Map<String, AgentContribution> contributions = new ConcurrentHashMap<>();
        private final List<String> sharedObservations = new CopyOnWriteArrayList<>();
        private ExperimentStatus status = ExperimentStatus.RECRUITING;
        
        public CollaborativeExperiment(String experimentId, String hypothesis, String description) {
            this.experimentId = experimentId;
            this.hypothesis = hypothesis;
            this.description = description;
            this.startTime = System.currentTimeMillis();
        }
        
        /**
         * Add an agent to this collaborative experiment
         * 
         * @param agentId The agent identifier
         * @param expertise The agent's relevant expertise/characteristic
         * @return This experiment for fluent chaining
         */
        public CollaborativeExperiment addAgent(String agentId, String expertise) {
            if (!participatingAgents.contains(agentId)) {
                participatingAgents.add(agentId);
                contributions.put(agentId, new AgentContribution(agentId, expertise));
                
                System.out.println("👋 Agent joined experiment " + experimentId + ": " + agentId);
                System.out.println("   Expertise: " + expertise);
                
                log("Agent " + agentId + " joined experiment " + experimentId);
            }
            return this;
        }
        
        /**
         * Record an observation from an agent
         * 
         * @param agentId The observing agent
         * @param observation The observation made
         * @param evidence Supporting evidence
         * @return This experiment for fluent chaining
         */
        public CollaborativeExperiment recordObservation(String agentId, String observation, String evidence) {
            if (!participatingAgents.contains(agentId)) {
                throw new IllegalArgumentException("Agent not participating in experiment: " + agentId);
            }
            
            String entry = agentId + ": " + observation + " (Evidence: " + evidence + ")";
            sharedObservations.add(entry);
            
            AgentContribution contribution = contributions.get(agentId);
            contribution.addObservation(observation, evidence);
            
            System.out.println("🔬 Observation recorded in " + experimentId);
            System.out.println("   Agent: " + agentId);
            System.out.println("   Observation: " + observation);
            
            log("Observation: " + agentId + " observed: " + observation);
            return this;
        }
        
        /**
         * Run the collaborative experiment with all participating agents
         * 
         * @return The collaborative result
         */
        public CollaborativeResult runCollaborative() {
            if (participatingAgents.size() < 2) {
                throw new IllegalStateException("Collaborative experiment requires at least 2 agents, got: " + 
                                              participatingAgents.size());
            }
            
            status = ExperimentStatus.RUNNING;
            System.out.println("🚀 Running collaborative experiment: " + experimentId);
            System.out.println("   Participating agents: " + participatingAgents.size());
            System.out.println("   Hypothesis: " + hypothesis);
            
            // Simulate collaborative experiment execution
            Map<String, Double> agentResults = new HashMap<>();
            double totalConfidence = 0.0;
            
            for (String agentId : participatingAgents) {
                // Each agent contributes their perspective
                double agentResult = simulateAgentExperiment(agentId);
                agentResults.put(agentId, agentResult);
                totalConfidence += agentResult;
                
                System.out.println("   " + agentId + " result: " + String.format("%.1f", agentResult));
            }
            
            double averageResult = totalConfidence / participatingAgents.size();
            double consensusLevel = calculateConsensus(agentResults);
            
            boolean hypothesisConfirmed = averageResult > 7.0 && consensusLevel > 0.7;
            
            status = ExperimentStatus.COMPLETED;
            
            CollaborativeResult result = new CollaborativeResult(
                experimentId, hypothesis, participatingAgents.size(),
                agentResults, averageResult, consensusLevel, hypothesisConfirmed
            );
            
            System.out.println("✅ Collaborative experiment completed!");
            System.out.println("   Average result: " + String.format("%.1f", averageResult));
            System.out.println("   Consensus level: " + String.format("%.1f", consensusLevel));
            System.out.println("   Hypothesis " + (hypothesisConfirmed ? "CONFIRMED" : "REJECTED"));
            
            // Record to experience database
            recordCollaborativeResult(result);
            
            activeExperiments.remove(experimentId);
            log("Experiment completed: " + experimentId + " - " + (hypothesisConfirmed ? "CONFIRMED" : "REJECTED"));
            
            return result;
        }
        
        /**
         * Simulate an individual agent's contribution to the experiment
         */
        private double simulateAgentExperiment(String agentId) {
            // In a real system, this would run the agent's specific experimental approach
            // For now, simulate based on agent characteristics and contributions
            AgentContribution contribution = contributions.get(agentId);
            
            // Base result with some randomness
            double baseResult = 5.0 + (Math.random() * 4.0); // 5.0 to 9.0
            
            // Bonus for agents with observations
            if (contribution.observations.size() > 0) {
                baseResult += 0.5;
            }
            
            // Round to 0.1 precision (framework requirement)
            return Math.round(baseResult * 10.0) / 10.0;
        }
        
        /**
         * Calculate consensus level among agent results
         */
        private double calculateConsensus(Map<String, Double> results) {
            if (results.size() < 2) return 1.0;
            
            double mean = results.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double variance = results.values().stream()
                .mapToDouble(r -> Math.pow(r - mean, 2))
                .average().orElse(0.0);
            
            // Consensus is higher when variance is lower
            // Scale so that variance of 0 = consensus 1.0, variance of 4 = consensus 0.0
            double consensus = Math.max(0.0, 1.0 - (variance / 4.0));
            return Math.round(consensus * 10.0) / 10.0;
        }
        
        /**
         * Record the collaborative result to the experience database
         */
        private void recordCollaborativeResult(CollaborativeResult result) {
            try {
                ExperienceRepository.record()
                    .category("collaboration")
                    .technology("Multi-Agent Experiment", "1.0", "framework-experiment")
                    .rating("consensus", result.consensusLevel * 10.0) // Scale to 0-10
                    .rating("confidence", result.averageResult)
                    .evidence("hypothesis", result.hypothesis)
                    .evidence("agents", String.valueOf(result.agentCount))
                    .evidence("results", result.agentResults.toString())
                    .workingAspect("Collaborative experiment with " + result.agentCount + " agents")
                    .recommendation(result.hypothesisConfirmed ? 
                                  "Adopt approach based on multi-agent validation" :
                                  "Reject approach based on multi-agent analysis")
                    .tag("multi-agent")
                    .tag("collaboration")
                    .save();
            } catch (Exception e) {
                System.err.println("⚠️  Failed to record collaborative result: " + e.getMessage());
            }
        }
        
        // Getters
        public String getExperimentId() { return experimentId; }
        public String getHypothesis() { return hypothesis; }
        public String getDescription() { return description; }
        public List<String> getParticipatingAgents() { return new ArrayList<>(participatingAgents); }
        public List<String> getSharedObservations() { return new ArrayList<>(sharedObservations); }
        public ExperimentStatus getStatus() { return status; }
    }
    
    /**
     * Status of a collaborative experiment
     */
    public enum ExperimentStatus {
        RECRUITING,  // Looking for participating agents
        RUNNING,     // Currently executing
        COMPLETED    // Finished
    }
    
    /**
     * An agent's contribution to a collaborative experiment
     */
    private static class AgentContribution {
        final String agentId;
        final String expertise;
        final List<String> observations = new CopyOnWriteArrayList<>();
        final List<String> evidence = new CopyOnWriteArrayList<>();
        
        public AgentContribution(String agentId, String expertise) {
            this.agentId = agentId;
            this.expertise = expertise;
        }
        
        public void addObservation(String observation, String evidenceItem) {
            observations.add(observation);
            evidence.add(evidenceItem);
        }
    }
    
    /**
     * Result of a collaborative experiment
     */
    public static class CollaborativeResult {
        public final String experimentId;
        public final String hypothesis;
        public final int agentCount;
        public final Map<String, Double> agentResults;
        public final double averageResult;
        public final double consensusLevel;
        public final boolean hypothesisConfirmed;
        
        public CollaborativeResult(String experimentId, String hypothesis, int agentCount,
                                 Map<String, Double> agentResults, double averageResult,
                                 double consensusLevel, boolean hypothesisConfirmed) {
            this.experimentId = experimentId;
            this.hypothesis = hypothesis;
            this.agentCount = agentCount;
            this.agentResults = new HashMap<>(agentResults);
            this.averageResult = averageResult;
            this.consensusLevel = consensusLevel;
            this.hypothesisConfirmed = hypothesisConfirmed;
        }
        
        /**
         * Publish the collaborative learning to other agents
         */
        public void publishCollaborativeLearning() {
            System.out.println("\n📢 PUBLISHING COLLABORATIVE LEARNING");
            System.out.println("  Experiment: " + experimentId);
            System.out.println("  Hypothesis: " + hypothesis);
            System.out.println("  Agents involved: " + agentCount);
            System.out.println("  Average result: " + String.format("%.1f", averageResult));
            System.out.println("  Consensus level: " + String.format("%.1f", consensusLevel));
            System.out.println("  Result: " + (hypothesisConfirmed ? "CONFIRMED" : "REJECTED"));
            System.out.println("  Shared with: All agents in framework");
            
            log("Published collaborative learning: " + experimentId);
        }
    }
}