package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.documentreader.FilenameProvider;
import com.anmoma.englishdaily.documentreader.SelectedDocument;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "none")
public class OpenAiListVocabularyService implements VocabularyService {
    private static final Logger log = LoggerFactory.getLogger(OpenAiListVocabularyService.class);
    private static final String SYSTEM_PROMPT = """
            You are an advanced AI language assistant integrated into an application designed to help users learn English.
            Please stick to the context provided for this task.
            Your task is to extract information from these documents and provide it to users in a structured format.
            Please avoid returning one of the terms of this blacklist: {blackList}
            Please respond only with a json payload containing the complete list of vocabulary terms, where each term includes the word, its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations and synonyms if makes sense.
            """;
    private static final String USER_REQUEST = """
            Get the complete vocabulary list from the context below. The terms can be found under the section named "Vocabulary Bank" or "Vocabulary List" or similar and can be labelled as follows:
            - idiom which stands for idiom
            - n which stands for noun
            - v which stands for verb
            - adj which stands for adjective
            - adv which stands for adverb
            - abrv which stands for abbreviation
            - phr v which stands for phrasal verb
            """;

    private final QuestionAwserAdvisorFactory questionAwserAdvisorFactory;
    private final ChatClient chatClient;
    private final FilenameProvider filenameProvider;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;
    private final boolean enableLogging;
    private final Random random;

    public OpenAiListVocabularyService(QuestionAwserAdvisorFactory questionAwserAdvisorFactory, ChatClient chatClient,
            FilenameProvider filenameProvider, SimpleLoggerAdvisor simpleLoggerAdvisor,
            @Value("${englishdaily.logging-advisor.enabled:false}") boolean enableLogging) {
        this.questionAwserAdvisorFactory = questionAwserAdvisorFactory;
        this.chatClient = chatClient;
        this.filenameProvider = filenameProvider;
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.enableLogging = enableLogging;
        this.random = new Random();
    }

    @Override
    public VocabularyTerm getDailyVocabulary() {
        return getDailyVocabulary(List.of());
    }

    @Override
    public VocabularyTerm getDailyVocabulary(List<String> blacklist) {
        try {
            SelectedDocument doc = filenameProvider.getRandomSelectedDocument();
            List<VocabularyTerm> terms = getVocabularyTerms(doc, 1, blacklist);
            if (terms.isEmpty()) {
                throw new LlmNotAvailableException("Llm service returned an empty response or empty vocabulary list");
            }
            return terms.getFirst();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llm service is not available");
        }
    }

    @Override
    public List<VocabularyTerm> getVocabularyTerms(SelectedDocument doc, int count, List<String> blacklist) {
        try {
            var outputConverter = new BeanOutputConverter<>(LlmVocabularyTermList.class);
            String ragQuery = buildRagQuery(doc.folderPath());
            Stream<Advisor> advisors = Stream.of(qaAdvisor(doc.filename(), ragQuery), enableLogging ? simpleLoggerAdvisor : null);
            LlmVocabularyTermList llmGeneratedVocabularyList = chatClient.prompt()
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

            if (llmGeneratedVocabularyList == null || llmGeneratedVocabularyList.items() == null || llmGeneratedVocabularyList.items()
                                                                                                                              .isEmpty()) {
                log.warn("Empty vocabulary list returned from document {}", doc.filename());
                return List.of();
            }

            List<LlmVocabularyTerm> filtered = llmGeneratedVocabularyList.items()
                                                                          .stream()
                                                                          .filter(term -> !blacklist.contains(term.word()))
                                                                          .toList();

            if (filtered.isEmpty()) {
                log.warn("All vocabulary terms from {} are blacklisted", doc.filename());
                return List.of();
            }

            List<LlmVocabularyTerm> shuffled = new ArrayList<>(filtered);
            Collections.shuffle(shuffled, random);

            log.debug("Document {}: {} terms available, selecting {}", doc.filename(), filtered.size(), Math.min(count, shuffled.size()));

            return shuffled.stream()
                           .limit(count)
                           .map(t -> new VocabularyTerm(doc.filename(), t.word(), t.definition(), t.partOfSpeech(),
                                   t.pronunciation(), t.exampleSentence(), t.collocations(), t.synonyms()))
                           .toList();
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llm service is not available");
        }
    }

    private QuestionAnswerAdvisor qaAdvisor(String filename, String ragQuery) {
        Filter.Expression filterExpression = new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("file_name"),
                new Filter.Value(filename));
        return questionAwserAdvisorFactory.createAdvisorWithQuerySimilarityThresholdTopKAndFilterExpression(
                ragQuery, 0.45, 10, filterExpression);
    }

    private String buildRagQuery(String folderPath) {
        if (folderPath.contains("phrasal")) return "Get the list of phrasal verbs with their meanings and usage";
        if (folderPath.contains("slang")) return "Get the list of slang terms and informal expressions";
        if (folderPath.contains("business")) return "Get the list of business and professional vocabulary terms";
        if (folderPath.contains("listening")) return "Get the vocabulary terms and key words from this lesson";
        return "Get the list of items from the vocabulary bank";
    }
}
