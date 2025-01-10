package com.anmoma.englishdaily.vocabulary;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VocabularyService {
    private static final String SYSTEM_PROMPT = """
            You are a english teacher with a huge amount of lessons stored in a vast pdf document library. 
            This documentation has a lot of sections,
             one of them is vocabulary bank which is comprised of a set of definitions, 
             its phonetic transcription and an usage example.
            """;
    private static final String USER_REQUEST = "Could you provide me with one random term and its definition from vocabulary bank and and usage example?";
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private RetrievalAugmentationAdvisor ragAdvisor;

    public String getDailyVocabulary() {
        String response = chatClient.prompt(SYSTEM_PROMPT)
                                    .advisors(ragAdvisor)
                                    .user(USER_REQUEST)
                                    .call()
                                    .content();
        return response;
    }
}
