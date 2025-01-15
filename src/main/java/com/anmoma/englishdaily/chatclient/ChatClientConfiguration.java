package com.anmoma.englishdaily.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfiguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder, RetrievalAugmentationAdvisor ragAdvisor,
            VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor) {
        return builder.defaultAdvisors(ragAdvisor, vectorStoreChatMemoryAdvisor)
                      .build();
    }

    @Bean
    public DocumentRetriever documentRetriever(VectorStore vectorStore) {
        return VectorStoreDocumentRetriever.builder()
                                           .vectorStore(vectorStore)
                                           .similarityThreshold(0.50)
                                           .build();
    }

   /* @Bean
    public QuestionAnswerAdvisor qaAdvisor(VectorStore vectorStore) {
        return new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()
                                                                   .withSimilarityThreshold(0.50)
                                                                   .withTopK(10));
    }*/

    @Bean
    public VectorStoreChatMemoryAdvisor vectorStoreChatMemoryAdvisor(VectorStore vectorStore) {
        return new VectorStoreChatMemoryAdvisor(vectorStore);
    }

    @Bean
    public RetrievalAugmentationAdvisor ragAdvisor(DocumentRetriever documentRetriever) {
        return RetrievalAugmentationAdvisor.builder()
                                           .documentRetriever(documentRetriever)

                                           .build();
    }

}
