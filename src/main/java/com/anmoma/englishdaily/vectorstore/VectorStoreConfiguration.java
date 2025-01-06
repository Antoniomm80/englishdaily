package com.anmoma.englishdaily.vectorstore;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.PgVectorStore.PgIndexType.HNSW;

@Configuration
public class VectorStoreConfiguration {

    @ConditionalOnMissingBean
    @Bean
    PgVectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return new PgVectorStore(jdbcTemplate, embeddingModel, 768, COSINE_DISTANCE, true, HNSW, true);
    }

    @Bean
    TextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }
}
