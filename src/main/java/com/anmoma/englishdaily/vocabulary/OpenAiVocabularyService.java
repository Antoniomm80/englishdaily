package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.documentreader.FilenameProvider;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "none")
public class OpenAiVocabularyService implements VocabularyService {
    private static final Logger log = LoggerFactory.getLogger(OpenAiVocabularyService.class);
    private static final String SYSTEM_PROMPT = """            
            You are an advanced AI language assistant integrated into an application designed to help users learn English.
            You are provided with a set of documents that contain a variety of information about the English language. 
            Your task is to extract information from these documents and provide it to users in a structured format.
            One of this sections is the vocabulary bank section which contains a list of items along with its definition.
            The terms can be found under the section named "Vocabulary Bank" or "Vocabulary List" or similar and can be labelled as follows:
            - idiom which stands for idiom
            - n which stands for noun
            - v which stands for verb
            - adj which stands for adjective
            - adv which stands for adverb
            - abrv which stands for abbreviation
            - phr v which stands for phrasal verb
            Please avoid returning one of the terms of this blacklist: {blackList}
            Please respond only with a json payload containing the word picked up and its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations and synonyms if makes sense.                     
            """;
    private static final String USER_REQUEST = """
            Get one random term from vocabulary bank
            """;

    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final FilenameProvider filenameProvider;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final boolean enableLogging;

    public OpenAiVocabularyService(QuestionAwserAdvisorFactory questionAwserAdvisorFactory, ChatClient chatClient, FilenameProvider filenameProvider,
            SimpleLoggerAdvisor simpleLoggerAdvisor, @Value("${englishdaily.logging-advisor.enabled:false}") boolean enableLogging) {
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.chatClient = chatClient;
        this.filenameProvider = filenameProvider;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.enableLogging = enableLogging;
    }

    @Override
    public VocabularyTerm getDailyVocabulary() {
        return getDailyVocabulary(List.of());
    }

    @Override
    public VocabularyTerm getDailyVocabulary(List<String> blacklist) {
        try {
            String sourceDocument = filenameProvider.getRandomFilenameFromDocumentsFolder();
            var outputConverter = new BeanOutputConverter<>(VocabularyTerm.class);
            Stream<Advisor> advisors = Stream.of(qaAdvisor(sourceDocument), enableLogging ? simpleLoggerAdvisor : null);
            VocabularyTerm llmGeneratedVocabularyTerm = chatClient.prompt()
                                                                  .system(sp -> sp.text(SYSTEM_PROMPT)
                                                                                  .param("blackList", String.join(",", blacklist)))
                                                                  .user(up -> up.text(USER_REQUEST))
                                                                  .advisors(advisors.filter(Objects::nonNull)
                                                                                    .toList())
                                                                  .options(OpenAiChatOptions.builder()
                                                                                            .temperature(0.5)
                                                                                            .build())
                                                                  .call()
                                                                  .entity(outputConverter);
            if (llmGeneratedVocabularyTerm == null) {
                throw new LlmNotAvailableException("Llm service returned an empty response");
            }
            log.debug("Llama response: {}", llmGeneratedVocabularyTerm);
            return new VocabularyTerm(sourceDocument, llmGeneratedVocabularyTerm);
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llm service is not available");
        }
    }

    private QuestionAnswerAdvisor qaAdvisor(String filename) {
        Filter.Expression filterExpression = new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value(filename));
        return questionAwserAdvisorFactory.createAdvisorWithQuerySimilarityThresholdTopKAndFilterExpression(
                "Get the list of items from the vocabulary bank", .1, 2, filterExpression);

    }

}
