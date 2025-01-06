package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class IngestionPipelineTest {
    @Autowired
    private IngestionPipeline ingestionPipeline;
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private RetrievalAugmentationAdvisor ragAdvisor;

    @Test
    void ingestionPipelineShouldIterateThroughDocumentsFolder() {
        ingestionPipeline.populateVectorStore();

        String response = chatClient.prompt("""
                                            You are a english teacher with a huge amount of lessons stored in a vast pdf document library. 
                                            This documentation has a lot of sections,
                                             one of them is vocabulary bank which is comprised of a set of definitions, 
                                             its phonetic transcription and an usage example.
                                            """)
                                    .advisors(ragAdvisor)
                                    .user("Could you provide me with one random term and its definition from vocabulary bank and and usage example?")
                                    .call()
                                    .content();

        System.out.println(response);
    }
}