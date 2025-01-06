package com.anmoma.englishdaily.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ChatClientConfiguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public DocumentRetriever documentRetriever(VectorStore vectorStore) {
        return VectorStoreDocumentRetriever.builder()
                                           .vectorStore(vectorStore)
                                           .similarityThreshold(0.50)
                                           .build();
    }

    @Bean
    public QueryExpander queryExpander(ChatClient.Builder builder) {
        return MultiQueryExpander.builder()
                                 .chatClientBuilder(builder.build()
                                                           .mutate())
                                 .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor ragAdvisor(DocumentRetriever documentRetriever, QueryExpander queryExpander,
            @Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor) {
        return RetrievalAugmentationAdvisor.builder()
                                           .documentRetriever(documentRetriever)
                                           .queryExpander(queryExpander)
                                           .taskExecutor(taskExecutor)
                                           .build();
    }

}
