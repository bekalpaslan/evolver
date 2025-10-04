package com.evolver.agent;package com.evolver.agent;



import java.util.*;import java.util.*;



/**/**

 * AGENT TECHNOLOGY RECOMMENDATIONS * AGENT TECHNOLOGY RECOMMENDATIONS

 *  * 

 * Comprehensive recommendations tailored to specific agent characteristics, * Comprehensive recommendations tailored to specific agent characteristics,

 * including technology ratings, harmony combinations, and domain-specific suggestions. * including technology ratings, harmony combinations, and domain-specific suggestions.

 */ */

public class AgentTechnologyRecommendations {public class AgentTechnologyRecommendations {

        

    private final String agentCharacteristic;    private final String agentCharacteristic;

    private final String domain;    private final String domain;

    private final List<TechnologyRecommendation> topRated;    private final List<TechnologyRecommendation> topRated;

    private final List<TechnologyRecommendation> harmoniousCombinations;    private final List<TechnologyRecommendation> harmoniousCombinations;

    private final List<TechnologyRecommendation> domainSpecific;    private final List<TechnologyRecommendation> domainSpecific;

    private final List<TechnologyRecommendation> emergingTechnologies;    private final List<TechnologyRecommendation> emergingTechnologies;

    private final Map<String, Object> metadata;    private final Map<String, Object> metadata;

        

    public AgentTechnologyRecommendations(Builder builder) {    public AgentTechnologyRecommendations(Builder builder) {

        this.agentCharacteristic = builder.agentCharacteristic;        this.agentCharacteristic = builder.agentCharacteristic;

        this.domain = builder.domain;        this.domain = builder.domain;

        this.topRated = new ArrayList<>(builder.topRated);        this.topRated = new ArrayList<>(builder.topRated);

        this.harmoniousCombinations = new ArrayList<>(builder.harmoniousCombinations);        this.harmoniousCombinations = new ArrayList<>(builder.harmoniousCombinations);

        this.domainSpecific = new ArrayList<>(builder.domainSpecific);        this.domainSpecific = new ArrayList<>(builder.domainSpecific);

        this.emergingTechnologies = new ArrayList<>(builder.emergingTechnologies);        this.emergingTechnologies = new ArrayList<>(builder.emergingTechnologies);

        this.metadata = new HashMap<>(builder.metadata);        this.metadata = new HashMap<>(builder.metadata);

    }    }

        

    public static Builder builder() {    public static Builder builder() {

        return new Builder();        return new Builder();

    }    }

        

    // Getters    // Getters

    public String getAgentCharacteristic() { return agentCharacteristic; }    public String getAgentCharacteristic() { return agentCharacteristic; }

    public String getDomain() { return domain; }    public String getDomain() { return domain; }

    public List<TechnologyRecommendation> getTopRated() { return new ArrayList<>(topRated); }    public List<TechnologyRecommendation> getTopRated() { return new ArrayList<>(topRated); }

    public List<TechnologyRecommendation> getHarmoniousCombinations() { return new ArrayList<>(harmoniousCombinations); }    public List<TechnologyRecommendation> getHarmoniousCombinations() { return new ArrayList<>(harmoniousCombinations); }

    public List<TechnologyRecommendation> getDomainSpecific() { return new ArrayList<>(domainSpecific); }    public List<TechnologyRecommendation> getDomainSpecific() { return new ArrayList<>(domainSpecific); }

    public List<TechnologyRecommendation> getEmergingTechnologies() { return new ArrayList<>(emergingTechnologies); }    public List<TechnologyRecommendation> getEmergingTechnologies() { return new ArrayList<>(emergingTechnologies); }

    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }

        

    /**    /**

     * Get all recommendations sorted by rating     * Get all recommendations sorted by rating

     */     */

    public List<TechnologyRecommendation> getAllRecommendations() {    public List<TechnologyRecommendation> getAllRecommendations() {

        List<TechnologyRecommendation> all = new ArrayList<>();        List<TechnologyRecommendation> all = new ArrayList<>();

        all.addAll(topRated);        all.addAll(topRated);

        all.addAll(harmoniousCombinations);        all.addAll(harmoniousCombinations);

        all.addAll(domainSpecific);        all.addAll(domainSpecific);

        all.addAll(emergingTechnologies);        all.addAll(emergingTechnologies);

                

        all.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));        all.sort((a, b) -> Double.compare(b.getRating(), a.getRating()));

        return all;        return all;

    }    }

        

    public static class Builder {    /**

        private String agentCharacteristic;     * Get recommendations by category

        private String domain;     */

        private List<TechnologyRecommendation> topRated = new ArrayList<>();    public List<TechnologyRecommendation> getByCategory(String category) {

        private List<TechnologyRecommendation> harmoniousCombinations = new ArrayList<>();        return getAllRecommendations().stream()

        private List<TechnologyRecommendation> domainSpecific = new ArrayList<>();            .filter(rec -> rec.getCategory().equals(category))

        private List<TechnologyRecommendation> emergingTechnologies = new ArrayList<>();            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        private Map<String, Object> metadata = new HashMap<>();    }

            

        public Builder agentCharacteristic(String agentCharacteristic) {    /**

            this.agentCharacteristic = agentCharacteristic;     * Get summary statistics

            return this;     */

        }    public RecommendationSummary getSummary() {

                List<TechnologyRecommendation> all = getAllRecommendations();

        public Builder domain(String domain) {        

            this.domain = domain;        double averageRating = all.stream()

            return this;            .mapToDouble(TechnologyRecommendation::getRating)

        }            .average().orElse(0.0);

                

        public Builder topRated(List<TechnologyRecommendation> topRated) {        double averageConfidence = all.stream()

            this.topRated = new ArrayList<>(topRated);            .mapToDouble(TechnologyRecommendation::getConfidence)

            return this;            .average().orElse(0.0);

        }        

                Map<String, Integer> categoryCount = new HashMap<>();

        public Builder harmoniousCombinations(List<TechnologyRecommendation> harmoniousCombinations) {        all.forEach(rec -> categoryCount.merge(rec.getCategory(), 1, Integer::sum));

            this.harmoniousCombinations = new ArrayList<>(harmoniousCombinations);        

            return this;        return RecommendationSummary.builder()

        }            .totalRecommendations(all.size())

                    .averageRating(averageRating)

        public Builder domainSpecific(List<TechnologyRecommendation> domainSpecific) {            .averageConfidence(averageConfidence)

            this.domainSpecific = new ArrayList<>(domainSpecific);            .categoryCounts(categoryCount)

            return this;            .build();

        }    }

            

        public Builder emergingTechnologies(List<TechnologyRecommendation> emergingTechnologies) {    public static class Builder {

            this.emergingTechnologies = new ArrayList<>(emergingTechnologies);        private String agentCharacteristic;

            return this;        private String domain;

        }        private List<TechnologyRecommendation> topRated = new ArrayList<>();

                private List<TechnologyRecommendation> harmoniousCombinations = new ArrayList<>();

        public Builder metadata(String key, Object value) {        private List<TechnologyRecommendation> domainSpecific = new ArrayList<>();

            this.metadata.put(key, value);        private List<TechnologyRecommendation> emergingTechnologies = new ArrayList<>();

            return this;        private Map<String, Object> metadata = new HashMap<>();

        }        

                public Builder agentCharacteristic(String agentCharacteristic) {

        public AgentTechnologyRecommendations build() {            this.agentCharacteristic = agentCharacteristic;

            Objects.requireNonNull(agentCharacteristic, "Agent characteristic is required");            return this;

            return new AgentTechnologyRecommendations(this);        }

        }        

    }        public Builder domain(String domain) {

}            this.domain = domain;

            return this;

/**        }

 * TECHNOLOGY RECOMMENDATION        

 *         public Builder topRated(List<TechnologyRecommendation> topRated) {

 * Individual technology recommendation with rating, reasoning, and metadata.            this.topRated = new ArrayList<>(topRated);

 */            return this;

class TechnologyRecommendation {        }

            

    private final String technology;        public Builder harmoniousCombinations(List<TechnologyRecommendation> harmoniousCombinations) {

    private final String version;            this.harmoniousCombinations = new ArrayList<>(harmoniousCombinations);

    private final double rating;            return this;

    private final String reason;        }

    private final String category;        

    private final double confidence;        public Builder domainSpecific(List<TechnologyRecommendation> domainSpecific) {

    private final Map<String, Object> additionalData;            this.domainSpecific = new ArrayList<>(domainSpecific);

                return this;

    public TechnologyRecommendation(Builder builder) {        }

        this.technology = builder.technology;        

        this.version = builder.version;        public Builder emergingTechnologies(List<TechnologyRecommendation> emergingTechnologies) {

        this.rating = builder.rating;            this.emergingTechnologies = new ArrayList<>(emergingTechnologies);

        this.reason = builder.reason;            return this;

        this.category = builder.category;        }

        this.confidence = builder.confidence;        

        this.additionalData = new HashMap<>(builder.additionalData);        public Builder metadata(String key, Object value) {

    }            this.metadata.put(key, value);

                return this;

    public static Builder builder() {        }\n        \n        public AgentTechnologyRecommendations build() {\n            Objects.requireNonNull(agentCharacteristic, \"Agent characteristic is required\");\n            return new AgentTechnologyRecommendations(this);\n        }\n    }\n}\n\n/**\n * TECHNOLOGY RECOMMENDATION\n * \n * Individual technology recommendation with rating, reasoning, and metadata.\n */\nclass TechnologyRecommendation {\n    \n    private final String technology;\n    private final String version;\n    private final double rating;\n    private final String reason;\n    private final String category;\n    private final double confidence;\n    private final Map<String, Object> additionalData;\n    \n    public TechnologyRecommendation(Builder builder) {\n        this.technology = builder.technology;\n        this.version = builder.version;\n        this.rating = builder.rating;\n        this.reason = builder.reason;\n        this.category = builder.category;\n        this.confidence = builder.confidence;\n        this.additionalData = new HashMap<>(builder.additionalData);\n    }\n    \n    public static Builder builder() {\n        return new Builder();\n    }\n    \n    // Getters\n    public String getTechnology() { return technology; }\n    public String getVersion() { return version; }\n    public double getRating() { return rating; }\n    public String getReason() { return reason; }\n    public String getCategory() { return category; }\n    public double getConfidence() { return confidence; }\n    public Map<String, Object> getAdditionalData() { return new HashMap<>(additionalData); }\n    \n    public static class Builder {\n        private String technology;\n        private String version;\n        private double rating;\n        private String reason;\n        private String category;\n        private double confidence = 0.8;\n        private Map<String, Object> additionalData = new HashMap<>();\n        \n        public Builder technology(String technology) {\n            this.technology = technology;\n            return this;\n        }\n        \n        public Builder version(String version) {\n            this.version = version;\n            return this;\n        }\n        \n        public Builder rating(double rating) {\n            this.rating = rating;\n            return this;\n        }\n        \n        public Builder reason(String reason) {\n            this.reason = reason;\n            return this;\n        }\n        \n        public Builder category(String category) {\n            this.category = category;\n            return this;\n        }\n        \n        public Builder confidence(double confidence) {\n            this.confidence = confidence;\n            return this;\n        }\n        \n        public Builder additionalData(String key, Object value) {\n            this.additionalData.put(key, value);\n            return this;\n        }\n        \n        public TechnologyRecommendation build() {\n            Objects.requireNonNull(technology, \"Technology is required\");\n            Objects.requireNonNull(category, \"Category is required\");\n            return new TechnologyRecommendation(this);\n        }\n    }\n}\n\n/**\n * RECOMMENDATION SUMMARY\n * \n * Statistical summary of technology recommendations.\n */\nclass RecommendationSummary {\n    \n    private final int totalRecommendations;\n    private final double averageRating;\n    private final double averageConfidence;\n    private final Map<String, Integer> categoryCounts;\n    \n    public RecommendationSummary(Builder builder) {\n        this.totalRecommendations = builder.totalRecommendations;\n        this.averageRating = builder.averageRating;\n        this.averageConfidence = builder.averageConfidence;\n        this.categoryCounts = new HashMap<>(builder.categoryCounts);\n    }\n    \n    public static Builder builder() {\n        return new Builder();\n    }\n    \n    // Getters\n    public int getTotalRecommendations() { return totalRecommendations; }\n    public double getAverageRating() { return averageRating; }\n    public double getAverageConfidence() { return averageConfidence; }\n    public Map<String, Integer> getCategoryCounts() { return new HashMap<>(categoryCounts); }\n    \n    public static class Builder {\n        private int totalRecommendations;\n        private double averageRating;\n        private double averageConfidence;\n        private Map<String, Integer> categoryCounts = new HashMap<>();\n        \n        public Builder totalRecommendations(int totalRecommendations) {\n            this.totalRecommendations = totalRecommendations;\n            return this;\n        }\n        \n        public Builder averageRating(double averageRating) {\n            this.averageRating = averageRating;\n            return this;\n        }\n        \n        public Builder averageConfidence(double averageConfidence) {\n            this.averageConfidence = averageConfidence;\n            return this;\n        }\n        \n        public Builder categoryCounts(Map<String, Integer> categoryCounts) {\n            this.categoryCounts = new HashMap<>(categoryCounts);\n            return this;\n        }\n        \n        public RecommendationSummary build() {\n            return new RecommendationSummary(this);\n        }\n    }\n}
        return new Builder();
    }
    
    // Getters
    public String getTechnology() { return technology; }
    public String getVersion() { return version; }
    public double getRating() { return rating; }
    public String getReason() { return reason; }
    public String getCategory() { return category; }
    public double getConfidence() { return confidence; }
    public Map<String, Object> getAdditionalData() { return new HashMap<>(additionalData); }
    
    public static class Builder {
        private String technology;
        private String version;
        private double rating;
        private String reason;
        private String category;
        private double confidence = 0.8;
        private Map<String, Object> additionalData = new HashMap<>();
        
        public Builder technology(String technology) {
            this.technology = technology;
            return this;
        }
        
        public Builder version(String version) {
            this.version = version;
            return this;
        }
        
        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }
        
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }
        
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        
        public Builder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }
        
        public Builder additionalData(String key, Object value) {
            this.additionalData.put(key, value);
            return this;
        }
        
        public TechnologyRecommendation build() {
            Objects.requireNonNull(technology, "Technology is required");
            Objects.requireNonNull(category, "Category is required");
            return new TechnologyRecommendation(this);
        }
    }
}