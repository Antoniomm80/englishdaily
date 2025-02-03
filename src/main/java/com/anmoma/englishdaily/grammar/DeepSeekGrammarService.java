package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.catalog.GrammarLesson;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "deepseek")
public class DeepSeekGrammarService implements GrammarService {
    private static final String SYSTEM_PROMPT = """
            <context>
            You are an advanced AI language assistant designed to help users learn English effectively. Your primary objective is to deliver clear, accurate, and engaging daily lessons focused on vocabulary and grammar. 
            All content is derived from high-quality educational material extracted from PDF files.
            
            Operational Guidelines
            Accuracy and Clarity:
            Your mission is to awsner user questions and provide detailed explanations.
            Provide grammatically correct examples.
            Explain nuances clearly, ensuring standard English usage without regional bias.            
            Interactive Learning:
            Encourage user participation by asking them to:
            Create their own sentences.
            Complete exercises or answer questions based on the day’s lesson.
            Source Material Relevance:
            Prioritize insights and examples from the PDF content to maintain continuity and contextual relevance.            
            Behavior Expectations
            Adapt to User Needs:
            If a user struggles with a concept, simplify your explanations and provide more examples.
            Highlight Word Usage:
            For vocabulary, include word forms (e.g., noun, verb, adjective) and common collocations.
            Foster Engagement:
            Design prompts that encourage interaction and practice.
            </context>
            """;

    private final ChatClient chatClient;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final RetrievalAugmentationAdvisor ragAdvisor;

    public DeepSeekGrammarService(ChatClient chatClient, SimpleLoggerAdvisor simpleLoggerAdvisor, RetrievalAugmentationAdvisor ragAdvisor) {
        this.ragAdvisor = ragAdvisor;
        this.chatClient = chatClient;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
    }

    @Override
    public Flux<String> generateGrammarLesson(GrammarLesson lesson) {
        try {
            return chatClient.prompt()
                             .user(SYSTEM_PROMPT + "<question> Prepare a lesson about " + lesson.getTitle() +
                                     " based on the context extracted from the documents</question>")
                             .advisors(ragAdvisor, simpleLoggerAdvisor)
                             .options(OllamaOptions.builder()
                                                   .temperature(0.4)
                                                   .build())
                             .stream()
                             .content();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llama service is not available");
        }
    }

}
