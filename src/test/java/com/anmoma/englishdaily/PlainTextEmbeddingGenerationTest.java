package com.anmoma.englishdaily;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class PlainTextEmbeddingGenerationTest {
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private TextSplitter tokenTextSplitter;
    @Autowired
    private ChatClient chatClient;
    @Value("classpath:sample/quijote.txt")
    private Resource plainTextFile;

    @Test
    @DisplayName("Se generan los embeddings para un documento plano")
    void givenPlainTextFileShouldGenerateEmbeddings() {
        TextReader textReader = new TextReader(plainTextFile);
        textReader.getCustomMetadata()
                  .put("filename", "quijote.txt");
        List<Document> documents = textReader.get();
        List<Document> splittedDocuments = tokenTextSplitter.apply(documents);
        vectorStore.add(splittedDocuments);

        List<Document> result = vectorStore.similaritySearch("En un lugar de la mancha");

        assertThat(result).isNotEmpty();

        String response = chatClient.prompt()
                                    .user("¿Como era el protagonista de nuestra historia?")
                                    .call()
                                    .content();
        System.out.println(response);
    }
}
