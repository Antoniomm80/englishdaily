package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.catalog.GrammarLesson;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "deepseek")
public class DeepSeekGrammarService implements GrammarService {
    private static final String SYSTEM_PROMPT = """
            <context>
            You are a teacher whose mission is to help users learn English effectively. Your primary objective is to deliver clear, accurate, and engaging daily lessons focused on vocabulary and grammar.            
            You have access to a set of documents that contain a variety of information about the English language. The use will be asking for any of the grammar lessons you have access to.
            Operational Guidelines
            Accuracy and Clarity:
            Your mission is to answer user questions and provide detailed explanations.
            Provide grammatically correct examples.
            Explain nuances clearly, ensuring standard English usage without regional bias.            
            Propose some exercises to reinforce the lesson.
            Some lessons may require a more detailed explanation than others. In such cases, provide a more detailed explanation.
            Some lessons may be comprised of many items. In such cases, elaborate on each item.
            </context>
            """;
    private static final String USER_REQUEST = """
            <question> Explain me in great detail what {lessonTitle} is. The explanation should include some usage examples besides what {lessonTitle} is </question>
            """;
    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final boolean enableLogging;

    public DeepSeekGrammarService(ChatClient chatClient, SimpleLoggerAdvisor simpleLoggerAdvisor, VectorStore vectorStore,
            QuestionAwserAdvisorFactory questionAwserAdvisorFactory, @Value("${englishdaily.logging-advisor.enabled:false}") boolean enableLogging) {

        this.chatClient = chatClient;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.enableLogging = enableLogging;
    }

    @Override
    public Flux<String> generateGrammarLesson(GrammarLesson lesson) {
        Stream<Advisor> advisors = Stream.of(questionAwserAdvisorFactory.createAdvisorWithQuery(lesson.getTitle()),
                enableLogging ? simpleLoggerAdvisor : null);
        try {
            return chatClient.prompt()
                             .system(SYSTEM_PROMPT)
                             .user(USER_REQUEST.replace("{lessonTitle}", lesson.getTitle()))
                             .advisors(advisors.filter(Objects::nonNull)
                                               .toList())
                             .options(OllamaOptions.builder()
                                                   .temperature(0.8)
                                                   .build())
                             .stream()
                             .content();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llama service is not available");
        }
    }
}
