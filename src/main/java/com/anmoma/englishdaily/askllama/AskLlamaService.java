package com.anmoma.englishdaily.askllama;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "llama")
public class AskLlamaService implements AskLlmService {

    private static final String SYSTEM_PROMPT = """
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
            """;

    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    public AskLlamaService(QuestionAwserAdvisorFactory questionAwserAdvisorFactory, ChatClient chatClient, SimpleLoggerAdvisor simpleLoggerAdvisor) {
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.chatClient = chatClient;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
    }

    @Override
    public Flux<String> ask(String userRequest) {
        try {
            return chatClient.prompt()
                             .system(SYSTEM_PROMPT)
                             .user(userRequest)
                             .advisors(questionAwserAdvisorFactory.createAdvisorWithQuery(userRequest), simpleLoggerAdvisor)
                             .stream()
                             .content();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llama service is not available");
        }
    }
}
