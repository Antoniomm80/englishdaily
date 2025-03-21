package com.anmoma.englishdaily.askllama;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "deepseek")
public class AskDeepSeekService implements AskLlmService {

    private static final String SYSTEM_PROMPT = """
            <context>
            You are a teacher whose mission is to help users learn English effectively. Your primary objective is to deliver clear, accurate, and engaging explations to the user questions.                       
            Operational Guidelines
            Accuracy and Clarity:
            Your mission is to answer user questions and provide detailed explanations.
            Provide grammatically correct examples.
            Explain nuances clearly, ensuring standard English usage without regional bias.            
            Propose some exercises to foster engagement.
            Some questions may require a more detailed explanation than others. In such cases, provide a more detailed explanation.
            Some questions may be comprised of many items. In such cases, elaborate on each item.
            </context>
            """;

    private final static String USER_PROMPT = """
            <question> {userRequest} </question>
            """;

    private final ChatClient chatClient;
    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final boolean enableLogging;

    public AskDeepSeekService(ChatClient chatClient, QuestionAwserAdvisorFactory questionAwserAdvisorFactory, SimpleLoggerAdvisor simpleLoggerAdvisor,
            @Value("${englishdaily.logging-advisor.enabled:false}") boolean enableLogging) {
        this.chatClient = chatClient;
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.enableLogging = enableLogging;
    }

    @Override
    public Flux<String> ask(String userRequest) {
        try {
            Stream<Advisor> advisors = Stream.of(questionAwserAdvisorFactory.createAdvisorWithQuery(userRequest),
                    enableLogging ? simpleLoggerAdvisor : null);
            return chatClient.prompt()
                             .system(SYSTEM_PROMPT)
                             .user(USER_PROMPT.replace("{userRequest}", userRequest))
                             .advisors(advisors.filter(Objects::nonNull)
                                               .toList())
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
