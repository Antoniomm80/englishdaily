package com.anmoma.englishdaily.grammar;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.catalog.grammarlesson.GrammarLesson;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "none")
public class OpenAiGrammarService implements GrammarService {
    private static final String SYSTEM_PROMPT = """            
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
            """;
    private static final String USER_REQUEST = """
            Explain me in great detail what {lessonTitle} is. The explanation should include some usage examples besides what {lessonTitle} is
            """;
    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final boolean enableLogging;

    public OpenAiGrammarService(ChatClient chatClient, SimpleLoggerAdvisor simpleLoggerAdvisor, VectorStore vectorStore,
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
                             .user(up -> up.text(USER_REQUEST)
                                           .param("lessonTitle", lesson.getTitle()))
                             .advisors(advisors.filter(Objects::nonNull)
                                               .toList())
                             .options(OpenAiChatOptions.builder()
                                                       .temperature(0.3)
                                                       .build())
                             .stream()
                             .content();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llm service is not available");
        }
    }
}
