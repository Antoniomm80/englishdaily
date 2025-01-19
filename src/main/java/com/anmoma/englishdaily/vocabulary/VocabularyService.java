package com.anmoma.englishdaily.vocabulary;

import com.anmoma.englishdaily.LlmNotAvailableException;
import com.anmoma.englishdaily.documentreader.FilenameProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
public class VocabularyService {
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
            
            """;
    private static final String USER_REQUEST = """
            Get one random slang term from vocabulary bank, along with its definition, part of speech, pronunciation, and an example sentence. If applicable, include common collocations or synonyms for additional context. 
            """;

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final FilenameProvider filenameProvider;

    public VocabularyService(VectorStore vectorStore, ChatClient chatClient, FilenameProvider filenameProvider) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
        this.filenameProvider = filenameProvider;

    }

    public VocabularyTerm getDailyVocabulary() {
        try {
            var outputConverter = new BeanOutputConverter<>(VocabularyTerm.class);
            return chatClient.prompt()
                             .system(SYSTEM_PROMPT)
                             .user(USER_REQUEST)
                             .advisors(qaAdvisor(filenameProvider.getRandomFilenameFromDocumentsFolder()))
                             .options(OllamaOptions.builder()
                                                   .format("json")
                                                   .temperature(0.5)
                                                   .build())
                             .call()
                             .entity(outputConverter);
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

}
