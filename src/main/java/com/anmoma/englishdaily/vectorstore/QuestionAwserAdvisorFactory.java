package com.anmoma.englishdaily.vectorstore;

import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;

@Service
public class QuestionAwserAdvisorFactory {
    private final VectorStore vectorStore;

    public QuestionAwserAdvisorFactory(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public QuestionAnswerAdvisor createAdvisorWithQuery(String query) {
        return createAdvisorWithQuerySimilarityThresholdAndTopK(query, 0.7, 6);
    }

    public QuestionAnswerAdvisor createAdvisorWithQuerySimilarityThresholdAndTopK(String query, double threshold, int topK) {
        return new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder()
                                                                   .query(query)
                                                                   .similarityThreshold(threshold)
                                                                   .topK(topK)
                                                                   .build());
    }

    public QuestionAnswerAdvisor createAdvisorWithQuerySimilarityThresholdTopKAndFilterExpression(String query, double threshold, int topK,
            Filter.Expression filterExpression) {
        return new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder()
                                                                   .query(query)
                                                                   .similarityThreshold(threshold)
                                                                   .topK(topK)
                                                                   .filterExpression(filterExpression)
                                                                   .build());
    }
}
