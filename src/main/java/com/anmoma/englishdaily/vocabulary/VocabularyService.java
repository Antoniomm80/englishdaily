package com.anmoma.englishdaily.vocabulary;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VocabularyService {
    private static final String SYSTEM_PROMPT = """
            You are an advanced AI language assistant integrated into an application designed to help users learn English. Your primary task is to provide clear, accurate, and engaging daily vocabulary and grammar refreshers derived from high-quality educational content extracted from PDF files. The content has been analyzed and structured using embedding technology from Nomic to ensure it aligns with effective language learning practices.           
                                                             Guidelines for your role:                                                                                                                                  
                                                             Accuracy: Provide grammatically correct examples, clarify any nuances, and offer standard English usage to avoid regional bias.
                                                             Daily Focus: Emphasize one or two key grammar points and introduce a manageable list of 5–10 new vocabulary words each day. Include definitions, pronunciations, and example sentences.
                                                             Interactive Feedback: Encourage users to interact by asking them to create their own sentences or complete exercises based on the day’s lesson.
                                                             Connection to PDF Content: Prioritize insights and examples extracted from the source material, ensuring continuity and relevance to the provided data.
                                                             You are responsible for creating a supportive and motivating environment where users feel encouraged to practice and improve their English skills daily.
            
                                                             Example Interaction Goals:
            
                                                             If a user struggles with understanding a grammar rule, break it down further and offer additional examples.
                                                             When introducing vocabulary, highlight word forms (e.g., noun, verb, adjective) and any common collocations.            
                                                             Always aim to foster a positive learning experience that builds confidence and consistency in the user’s language learning journey.
            """;
    private static final String USER_REQUEST = "Provide me with a random vocabulary word from the vocabulary bank sections from the PDFs set, along with its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations or synonyms for additional context.Please return the information in JSON format ready to be processed. The JSON fields must be named as follows: word, definition, partOfSpeech, pronunciation, exampleSentence, collocations, synonyms. ";
    @Autowired
    private ChatClient chatClient;
    @Autowired
    private RetrievalAugmentationAdvisor ragAdvisor;

    public VocabularyTerm getDailyVocabulary() {
        var outputConverter = new BeanOutputConverter<>(VocabularyTerm.class);
        VocabularyTerm response = chatClient.prompt(SYSTEM_PROMPT)
                                            .advisors(ragAdvisor)
                                            .user(USER_REQUEST)
                                            .options(OllamaOptions.builder()
                                                                  .withFormat("json")
                                                                  .build())
                                            .call()
                                            .entity(outputConverter);
        return response;
    }
}
