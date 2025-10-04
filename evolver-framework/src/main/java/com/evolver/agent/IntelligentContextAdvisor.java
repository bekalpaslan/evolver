package com.evolver.agent;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * INTELLIGENT CONTEXT ADVISOR
 * 
 * Analyzes ongoing tasks and suggests when engineers should provide
 * more context to maximize collaboration efficiency and outcome quality.
 * 
 * The system tracks context gaps, task complexity, time investment,
 * and provides intelligent recommendations for context enhancement.
 */
public class IntelligentContextAdvisor {
    
    private final Map<String, TaskContext> activeTasks;
    private final Map<String, ContextGapAnalysis> gapHistory;
    private final AgentCharacteristic agentCharacteristic;
    private final ContextEfficiencyMetrics metrics;
    
    public IntelligentContextAdvisor(AgentCharacteristic agentCharacteristic) {
        this.agentCharacteristic = agentCharacteristic;
        this.activeTasks = new HashMap<>();
        this.gapHistory = new HashMap<>();
        this.metrics = new ContextEfficiencyMetrics();
    }
    
    /**
     * Analyze current task and suggest context improvements
     */
    public ContextSuggestion analyzeAndSuggest(String taskId, TaskAnalysisInput input) {
        TaskContext task = activeTasks.computeIfAbsent(taskId, k -> new TaskContext(taskId));
        task.updateProgress(input);
        
        // Analyze multiple dimensions
        ContextGapAnalysis gaps = analyzeContextGaps(task, input);
        TaskComplexityAnalysis complexity = analyzeTaskComplexity(task, input);
        TimeInvestmentAnalysis timeAnalysis = analyzeTimeInvestment(task);
        EfficiencyPrediction efficiency = predictEfficiency(task, gaps, complexity);
        
        // Generate intelligent suggestions
        List<ContextSuggestionItem> suggestions = generateSuggestions(
            task, gaps, complexity, timeAnalysis, efficiency);
        
        // Calculate urgency and priority
        SuggestionPriority priority = calculatePriority(gaps, complexity, timeAnalysis);
        
        return ContextSuggestion.builder()
            .taskId(taskId)
            .agentCharacteristic(agentCharacteristic.getName())
            .analysisTimestamp(LocalDateTime.now())
            .contextGaps(gaps)
            .taskComplexity(complexity)
            .timeAnalysis(timeAnalysis)
            .efficiencyPrediction(efficiency)
            .suggestions(suggestions)
            .priority(priority)
            .overallRecommendation(generateOverallRecommendation(suggestions, priority))
            .estimatedTimeSavings(calculateTimeSavings(suggestions))
            .build();
    }
    
    /**
     * Analyze context gaps in current task
     */
    private ContextGapAnalysis analyzeContextGaps(TaskContext task, TaskAnalysisInput input) {
        List<ContextGap> gaps = new ArrayList<>();
        
        // Functional context gaps
        if (isFunctionalContextLacking(input)) {
            gaps.add(ContextGap.builder()
                .type(ContextGapType.FUNCTIONAL)
                .severity(calculateFunctionalGapSeverity(input))
                .description("Functional requirements appear incomplete or ambiguous")
                .suggestedInformation(Arrays.asList(
                    "Detailed user stories with acceptance criteria",
                    "Business rules and constraints", 
                    "Error handling requirements",
                    "Performance expectations"
                ))
                .estimatedImpact(ContextImpact.HIGH)
                .build());
        }
        
        // Technical context gaps
        if (isTechnicalContextLacking(input)) {
            gaps.add(ContextGap.builder()
                .type(ContextGapType.TECHNICAL)
                .severity(calculateTechnicalGapSeverity(input))
                .description("Technical context insufficient for optimal implementation")
                .suggestedInformation(Arrays.asList(
                    "Architecture diagrams or system overview",
                    "Existing codebase patterns and conventions",
                    "Integration points and dependencies",
                    "Deployment and environment details"
                ))
                .estimatedImpact(ContextImpact.MEDIUM)
                .build());
        }
        
        // Domain context gaps
        if (isDomainContextLacking(input)) {
            gaps.add(ContextGap.builder()
                .type(ContextGapType.DOMAIN)
                .severity(calculateDomainGapSeverity(input))
                .description("Domain knowledge gaps may lead to suboptimal solutions")
                .suggestedInformation(Arrays.asList(
                    "Industry-specific requirements",
                    "Regulatory or compliance constraints",
                    "User workflow and business processes",
                    "Domain terminology and definitions"
                ))
                .estimatedImpact(ContextImpact.MEDIUM)
                .build());
        }
        
        // Quality context gaps  
        if (isQualityContextLacking(input)) {
            gaps.add(ContextGap.builder()
                .type(ContextGapType.QUALITY)
                .severity(calculateQualityGapSeverity(input))
                .description("Quality requirements need clarification")
                .suggestedInformation(Arrays.asList(
                    "Testing strategy and coverage expectations",
                    "Code quality standards and review process",
                    "Security requirements and considerations",
                    "Performance benchmarks and monitoring"
                ))
                .estimatedImpact(ContextImpact.HIGH)
                .build());
        }
        
        return ContextGapAnalysis.builder()
            .gaps(gaps)
            .overallGapScore(calculateOverallGapScore(gaps))
            .criticalGapCount(gaps.stream().mapToInt(g -> g.getSeverity() >= 7 ? 1 : 0).sum())
            .build();
    }
    
    /**
     * Analyze task complexity indicators
     */
    private TaskComplexityAnalysis analyzeTaskComplexity(TaskContext task, TaskAnalysisInput input) {
        double complexity = 0.0;
        List<String> complexityFactors = new ArrayList<>();
        
        // Analyze code complexity
        if (input.getEstimatedLinesOfCode() > 500) {
            complexity += 0.3;
            complexityFactors.add("Large codebase scope (" + input.getEstimatedLinesOfCode() + " LOC)");
        }
        
        // Analyze integration complexity
        if (input.getIntegrationPoints() > 3) {
            complexity += 0.2;
            complexityFactors.add("Multiple integration points (" + input.getIntegrationPoints() + ")");
        }
        
        // Analyze technology complexity
        if (input.getNewTechnologies() > 1) {
            complexity += 0.2;
            complexityFactors.add("Multiple new technologies (" + input.getNewTechnologies() + ")");
        }
        
        // Analyze domain complexity
        if (input.getDomainNovelty() > 0.7) {
            complexity += 0.2;
            complexityFactors.add("High domain novelty");
        }
        
        // Analyze time pressure
        if (input.getTimeConstraintSeverity() > 0.6) {
            complexity += 0.1;
            complexityFactors.add("Significant time constraints");
        }
        
        return TaskComplexityAnalysis.builder()
            .overallComplexity(Math.min(1.0, complexity))
            .complexityFactors(complexityFactors)
            .riskLevel(calculateRiskLevel(complexity))
            .recommendedApproach(getRecommendedApproach(complexity, agentCharacteristic))
            .build();
    }
    
    /**
     * Analyze time investment vs. expected return
     */
    private TimeInvestmentAnalysis analyzeTimeInvestment(TaskContext task) {
        Duration timeSpent = Duration.between(task.getStartTime(), LocalDateTime.now());
        double hoursSpent = timeSpent.toMinutes() / 60.0;
        
        // Estimate remaining time based on complexity and progress
        double estimatedTotal = task.getEstimatedHours();
        double progressPercent = task.getProgressPercent();
        double projectedTotal = progressPercent > 0 ? hoursSpent / (progressPercent / 100.0) : estimatedTotal;
        
        boolean isLongRunning = projectedTotal > 4.0; // More than 4 hours
        boolean isHighValue = task.getBusinessValue() > 0.7;
        boolean needsMoreContext = hoursSpent > 2.0 && progressPercent < 30;
        
        return TimeInvestmentAnalysis.builder()
            .hoursSpent(hoursSpent)
            .estimatedTotalHours(estimatedTotal)
            .projectedTotalHours(projectedTotal)
            .progressPercent(progressPercent)
            .isLongRunning(isLongRunning)
            .isHighValue(isHighValue)
            .needsMoreContext(needsMoreContext)
            .efficiency(calculateEfficiency(hoursSpent, progressPercent))
            .build();
    }
    
    /**
     * Generate intelligent context suggestions
     */
    private List<ContextSuggestionItem> generateSuggestions(
            TaskContext task, 
            ContextGapAnalysis gaps, 
            TaskComplexityAnalysis complexity,
            TimeInvestmentAnalysis timeAnalysis,
            EfficiencyPrediction efficiency) {
        
        List<ContextSuggestionItem> suggestions = new ArrayList<>();
        
        // Time-based suggestions
        if (timeAnalysis.isLongRunning() && timeAnalysis.needsMoreContext()) {
            suggestions.add(ContextSuggestionItem.builder()
                .category("Time Investment")
                .priority(SuggestionPriority.HIGH)
                .title("Significant Time Investment Detected - Context Review Recommended")
                .description("You've invested " + String.format("%.1f", timeAnalysis.getHoursSpent()) + 
                    " hours with " + String.format("%.0f", timeAnalysis.getProgressPercent()) + 
                    "% progress. Additional context could accelerate completion.")
                .actionItems(Arrays.asList(
                    "Review and clarify requirements with stakeholders",
                    "Gather additional technical documentation",
                    "Discuss implementation approach with team lead",
                    "Validate assumptions with domain experts"
                ))
                .estimatedTimeSavings(30) // minutes
                .reasoning("Time investment indicates task complexity requires better context foundation")
                .build());
        }
        
        // Complexity-based suggestions
        if (complexity.getOverallComplexity() > 0.6) {
            suggestions.add(ContextSuggestionItem.builder()
                .category("Task Complexity")
                .priority(SuggestionPriority.MEDIUM)
                .title("High Complexity Task - Enhanced Context Recommended")
                .description("Task complexity factors: " + String.join(", ", complexity.getComplexityFactors()))
                .actionItems(Arrays.asList(
                    "Break down task into smaller, well-defined subtasks",
                    "Gather architectural guidance for complex integrations",
                    "Establish testing strategy before implementation",
                    "Document assumptions and design decisions"
                ))
                .estimatedTimeSavings(45)
                .reasoning("Complex tasks benefit significantly from upfront context investment")
                .build());
        }
        
        // Gap-specific suggestions
        for (ContextGap gap : gaps.getGaps()) {
            if (gap.getSeverity() >= 6) {
                suggestions.add(ContextSuggestionItem.builder()
                    .category(gap.getType().toString())
                    .priority(gap.getSeverity() >= 8 ? SuggestionPriority.HIGH : SuggestionPriority.MEDIUM)
                    .title("Address " + gap.getType() + " Context Gap")
                    .description(gap.getDescription())
                    .actionItems(gap.getSuggestedInformation())
                    .estimatedTimeSavings(calculateGapTimeSavings(gap))
                    .reasoning("Addressing this gap early prevents rework and improves solution quality")
                    .build());
            }
        }
        
        // Agent-specific suggestions
        addAgentSpecificSuggestions(suggestions, task, complexity, agentCharacteristic);
        
        // Efficiency-based suggestions
        if (efficiency.getPredictedEfficiency() < 0.6) {
            suggestions.add(ContextSuggestionItem.builder()
                .category("Efficiency")
                .priority(SuggestionPriority.HIGH)
                .title("Low Efficiency Predicted - Context Enhancement Critical")
                .description("Current approach may lead to extended development time and potential rework")
                .actionItems(Arrays.asList(
                    "Pause development and review requirements",
                    "Consult with technical lead or architect",
                    "Research similar implementations and best practices",
                    "Validate approach with a quick prototype"
                ))
                .estimatedTimeSavings(120) // 2 hours
                .reasoning("Early course correction prevents significant time loss")
                .build());
        }
        
        return suggestions.stream()
            .sorted((a, b) -> b.getPriority().getValue() - a.getPriority().getValue())
            .collect(Collectors.toList());
    }
    
    /**
     * Add agent characteristic-specific suggestions
     */
    private void addAgentSpecificSuggestions(List<ContextSuggestionItem> suggestions, 
                                           TaskContext task, 
                                           TaskComplexityAnalysis complexity,
                                           AgentCharacteristic characteristic) {
        
        String agentName = characteristic.getName();
        
        switch (agentName) {
            case "DocBot":
                if (task.getDocumentationScore() < 0.7) {
                    suggestions.add(ContextSuggestionItem.builder()
                        .category("Documentation")
                        .priority(SuggestionPriority.MEDIUM)
                        .title("Documentation Standards Review Needed")
                        .description("As a documentation-focused agent, I need clearer documentation context")
                        .actionItems(Arrays.asList(
                            "Provide examples of expected documentation format",
                            "Clarify documentation standards and requirements",
                            "Share existing documentation templates"
                        ))
                        .estimatedTimeSavings(25)
                        .reasoning("Clear documentation context enables better implementation planning")
                        .build());
                }
                break;
                
            case "CleanFreak":
                if (task.getCodeQualityContext() < 0.6) {
                    suggestions.add(ContextSuggestionItem.builder()
                        .category("Code Quality")
                        .priority(SuggestionPriority.MEDIUM)
                        .title("Code Quality Standards Clarification Needed")
                        .description("Clean code implementation requires clearer quality context")
                        .actionItems(Arrays.asList(
                            "Share coding standards and style guides",
                            "Provide examples of preferred patterns",
                            "Clarify refactoring boundaries and constraints"
                        ))
                        .estimatedTimeSavings(35)
                        .reasoning("Quality standards context prevents rework and review cycles")
                        .build());
                }
                break;
                
            case "SpeedDemon":
                if (complexity.getOverallComplexity() > 0.5) {
                    suggestions.add(ContextSuggestionItem.builder()
                        .category("Performance")
                        .priority(SuggestionPriority.HIGH)
                        .title("Performance Context Critical for Complex Task")
                        .description("Performance-focused implementation needs clearer optimization context")
                        .actionItems(Arrays.asList(
                            "Define performance benchmarks and targets",
                            "Identify critical performance paths",
                            "Provide profiling and monitoring requirements"
                        ))
                        .estimatedTimeSavings(40)
                        .reasoning("Performance context prevents premature optimization and guides focus")
                        .build());
                }
                break;
                
            case "ChaosMonkey":
                suggestions.add(ContextSuggestionItem.builder()
                    .category("Edge Cases")
                    .priority(SuggestionPriority.MEDIUM)
                    .title("Edge Case and Error Handling Context Needed")
                    .description("Chaos testing requires comprehensive failure scenario context")
                    .actionItems(Arrays.asList(
                        "Document known failure modes and edge cases",
                        "Provide error handling strategy and requirements",
                        "Share resilience and recovery expectations"
                    ))
                    .estimatedTimeSavings(30)
                    .reasoning("Edge case context enables comprehensive resilience design")
                    .build());
                break;
        }
    }
    
    // Helper methods for gap analysis
    private boolean isFunctionalContextLacking(TaskAnalysisInput input) {
        return input.getFunctionalClarityScore() < 0.6 || 
               input.getRequirementCompletenessScore() < 0.7;
    }
    
    private boolean isTechnicalContextLacking(TaskAnalysisInput input) {
        return input.getTechnicalContextScore() < 0.6 ||
               input.getArchitecturalGuidanceScore() < 0.5;
    }
    
    private boolean isDomainContextLacking(TaskAnalysisInput input) {
        return input.getDomainKnowledgeScore() < 0.6 ||
               input.getDomainNovelty() > 0.7;
    }
    
    private boolean isQualityContextLacking(TaskAnalysisInput input) {
        return input.getQualityRequirementsScore() < 0.6 ||
               input.getTestingContextScore() < 0.5;
    }
    
    private EfficiencyPrediction predictEfficiency(TaskContext task, 
                                                 ContextGapAnalysis gaps, 
                                                 TaskComplexityAnalysis complexity) {
        // Predict efficiency based on current context quality
        double baseEfficiency = 0.8;
        double gapPenalty = gaps.getOverallGapScore() * 0.3;
        double complexityPenalty = complexity.getOverallComplexity() * 0.2;
        
        double predicted = Math.max(0.1, baseEfficiency - gapPenalty - complexityPenalty);
        
        return EfficiencyPrediction.builder()
            .predictedEfficiency(predicted)
            .confidenceLevel(0.75)
            .factorsConsidered(Arrays.asList(
                "Context gap analysis",
                "Task complexity assessment", 
                "Historical patterns"
            ))
            .build();
    }
    
    private double calculateTimeSavings(List<ContextSuggestionItem> suggestions) {
        return suggestions.stream()
            .mapToDouble(ContextSuggestionItem::getEstimatedTimeSavings)
            .sum();
    }
    
    private String generateOverallRecommendation(List<ContextSuggestionItem> suggestions, 
                                               SuggestionPriority priority) {
        if (suggestions.isEmpty()) {
            return "Current context appears sufficient. Continue with implementation.";
        }
        
        long highPriority = suggestions.stream()
            .mapToLong(s -> s.getPriority() == SuggestionPriority.HIGH ? 1 : 0)
            .sum();
            
        if (highPriority > 0) {
            return String.format("RECOMMENDED: Address %d high-priority context gaps before proceeding. " +
                "Estimated time savings: %.0f minutes.", highPriority, calculateTimeSavings(suggestions));
        } else {
            return String.format("SUGGESTED: Consider addressing %d context improvements for better efficiency. " +
                "Estimated time savings: %.0f minutes.", suggestions.size(), calculateTimeSavings(suggestions));
        }
    }
    
    // Additional helper methods...
    private double calculateOverallGapScore(List<ContextGap> gaps) {
        return gaps.stream().mapToDouble(ContextGap::getSeverity).average().orElse(0.0) / 10.0;
    }
    
    private int calculateGapTimeSavings(ContextGap gap) {
        return switch (gap.getEstimatedImpact()) {
            case HIGH -> 60;
            case MEDIUM -> 30;
            case LOW -> 15;
        };
    }
    
    private double calculateEfficiency(double hoursSpent, double progressPercent) {
        if (hoursSpent == 0) return 1.0;
        return Math.min(1.0, (progressPercent / 100.0) / (hoursSpent / 8.0)); // 8 hours = 100% efficiency baseline
    }
    
    private SuggestionPriority calculatePriority(ContextGapAnalysis gaps, 
                                                TaskComplexityAnalysis complexity, 
                                                TimeInvestmentAnalysis timeAnalysis) {
        if (gaps.getCriticalGapCount() > 0 || 
            (timeAnalysis.isLongRunning() && timeAnalysis.needsMoreContext())) {
            return SuggestionPriority.HIGH;
        } else if (complexity.getOverallComplexity() > 0.6 || gaps.getOverallGapScore() > 0.5) {
            return SuggestionPriority.MEDIUM;
        } else {
            return SuggestionPriority.LOW;
        }
    }
    
    private String getRecommendedApproach(double complexity, AgentCharacteristic characteristic) {
        if (complexity > 0.7) {
            return "Incremental development with frequent validation checkpoints";
        } else if (complexity > 0.4) {
            return "Structured approach with clear milestones";
        } else {
            return "Standard development process";
        }
    }
    
    private String calculateRiskLevel(double complexity) {
        if (complexity > 0.7) return "HIGH";
        else if (complexity > 0.4) return "MEDIUM";
        else return "LOW";
    }
    
    private double calculateFunctionalGapSeverity(TaskAnalysisInput input) {
        return (1.0 - input.getFunctionalClarityScore()) * 10.0;
    }
    
    private double calculateTechnicalGapSeverity(TaskAnalysisInput input) {
        return (1.0 - input.getTechnicalContextScore()) * 10.0;
    }
    
    private double calculateDomainGapSeverity(TaskAnalysisInput input) {
        return (1.0 - input.getDomainKnowledgeScore()) * 10.0;
    }
    
    private double calculateQualityGapSeverity(TaskAnalysisInput input) {
        return (1.0 - input.getQualityRequirementsScore()) * 10.0;
    }
}