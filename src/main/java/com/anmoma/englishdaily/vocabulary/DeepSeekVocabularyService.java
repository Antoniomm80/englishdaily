package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.chatclient.SimpleLoggerAdvisor;
import com.anmoma.englishdaily.documentreader.FilenameProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@ConditionalOnProperty(value = "englishdaily.chat-service.llm-model", havingValue = "deepseek")
public class DeepSeekVocabularyService implements VocabularyService {
    private static final String SYSTEM_PROMPT = """
            <context>
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
            Please avoid returning one of the terms of this blacklist: {blackList}
            Please respond only with a json payload containing the word picked up and its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations and synonyms if makes sense.          
            </context>
            
            """;
    private static final String USER_REQUEST = """
            <question>Get one random term from vocabulary bank</question> 
            """;

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final FilenameProvider filenameProvider;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    public DeepSeekVocabularyService(VectorStore vectorStore, ChatClient chatClient, FilenameProvider filenameProvider,
            SimpleLoggerAdvisor simpleLoggerAdvisor) {
        this.vectorStore = vectorStore;
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
            String sourceDocument = filenameProvider.getRandomFilenameFromDocumentsFolder();

            String llmGeneratedVocabularyTerm = chatClient.prompt(SYSTEM_PROMPT.replace("{blackList}", String.join(",", blacklist)) + USER_REQUEST)
                                                          .advisors(qaAdvisor(sourceDocument), simpleLoggerAdvisor)
                                                          .options(OllamaOptions.builder()
                                                                                .temperature(0.4)
                                                                                .build())
                                                          .call()
                                                          .content();

            var term = deserializeJson(getJsonString(llmGeneratedVocabularyTerm));
            return new VocabularyTerm(sourceDocument, (String) term.get("term"), (String) term.get("definition"), (String) term.get("part_of_speech"),
                    (String) term.get("pronunciation"), (String) term.get("example_sentence"), (List) term.get("collocations"),
                    (List) term.get("synonyms"));
        } catch (ResourceAccessException rae) {
            throw new LlmNotAvailableException("Llama service is not available");
        }
    }

    private QuestionAnswerAdvisor qaAdvisor(String filename) {
        Filter.Expression filterExpression = new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("file_name"), new Filter.Value(filename));
        return new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder()
                                                                   .similarityThreshold(0.10)
                                                                   .topK(6)
                                                                   .filterExpression(filterExpression)
                                                                   .build());

    }

    private String getJsonString(String llmResponse) {
        Pattern JSON_PATTERN = Pattern.compile("```json\\n(\n|.)*?```", Pattern.DOTALL);
        Matcher matcher = JSON_PATTERN.matcher(llmResponse);
        if (matcher.find()) {
            return matcher.group()
                          .replace("```json\n", "")
                          .replace("```", "")
                          .trim();
        }
        return null;
    }

    private Map<String, Object> deserializeJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing json", e);
        }
    }

}
