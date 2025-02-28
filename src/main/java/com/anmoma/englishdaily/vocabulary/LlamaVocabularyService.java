package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import com.anmoma.englishdaily.documentreader.FilenameProvider;
import com.anmoma.englishdaily.vectorstore.QuestionAwserAdvisorFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "llama")
public class LlamaVocabularyService implements VocabularyService {
    private static final String SYSTEM_PROMPT = """
            You are an advanced AI language assistant integrated into an application designed to help users learn English.
            You are provided with a set of documents that contain a variety of information about the English language. 
            Your task is to extract information from these documents and provide it to users in a structured format.
            One of this sections is the vocabulary bank section which contains a list of items along with its definition.
            Each term may be labelled as follows:
            - idiom which stands for idiom
            - n which stands for noun
            - v which stands for verb
            - adj which stands for adjective
            - adv which stands for adverb
            - abrv which stands for abbreviation
            - phr v which stands for phrasal verb
            
            In order to avoid repetition please avoid returning one of the terms of this blacklist: {blackList}
            """;
    private static final String USER_REQUEST = """
            Get one random slang term from vocabulary bank, along with its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations or synonyms for additional context. 
            """;

    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final FilenameProvider filenameProvider;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    public LlamaVocabularyService(QuestionAwserAdvisorFactory questionAwserAdvisorFactory, ChatClient chatClient, FilenameProvider filenameProvider,
            SimpleLoggerAdvisor simpleLoggerAdvisor) {
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.chatClient = chatClient;
        this.filenameProvider = filenameProvider;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
    }

    @Override
    public VocabularyTerm getDailyVocabulary() {
        return getDailyVocabulary(List.of());
    }

    @Override
    public VocabularyTerm getDailyVocabulary(List<String> blacklist) {
        try {
            var outputConverter = new BeanOutputConverter<>(LlmVocabularyTerm.class);
            String sourceDocument = filenameProvider.getRandomFilenameFromDocumentsFolder();
            LlmVocabularyTerm llmGeneratedVocabularyTerm = chatClient.prompt()
                                                                     .system(sp -> sp.text(SYSTEM_PROMPT)
                                                                                     .param("blackList", blacklist))
                                                                     .user(USER_REQUEST)
                                                                     .advisors(qaAdvisor(sourceDocument), simpleLoggerAdvisor)
                                                                     .options(OllamaOptions.builder()
                                                                                           .format("json")
                                                                                           .temperature(0.5)
                                                                                           .build())
                                                                     .call()
                                                                     .entity(outputConverter);
            return new VocabularyTerm(sourceDocument, llmGeneratedVocabularyTerm.word(), llmGeneratedVocabularyTerm.definition(),
                    llmGeneratedVocabularyTerm.partOfSpeech(), llmGeneratedVocabularyTerm.pronunciation(),
                    llmGeneratedVocabularyTerm.exampleSentence(), llmGeneratedVocabularyTerm.collocations(), llmGeneratedVocabularyTerm.synonyms());
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llama service is not available");
        }
    }

    private QuestionAnswerAdvisor qaAdvisor(String filename) {
        Filter.Expression filterExpression = new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value(filename));
        return questionAwserAdvisorFactory.createAdvisorWithQuerySimilarityThresholdTopKAndFilterExpression(
                "Get the list of items from the vocabulary bank", .1, 2, filterExpression);

    }

}
