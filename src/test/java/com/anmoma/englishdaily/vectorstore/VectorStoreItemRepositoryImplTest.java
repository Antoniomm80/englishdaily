package com.anmoma.englishdaily.vectorstore;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class VectorStoreItemRepositoryImplTest {
    @Autowired
    private IngestionPipeline ingestionPipeline;
    @Autowired
    private VectorStoreItemRepository vectorStoreItemRepository;

    @BeforeEach
    void setUp() {
        ingestionPipeline.populateVectorStore();
    }

    @Test
    @DisplayName("Busqueda por nombre de fichero debe devolver registros")
    void givenSearchByMetadataFilenameShouldReturnRegisters() {
        List<VectorStoreItem> vectorStoreItems = vectorStoreItemRepository.findItemsByFileName("Advanced Grammar - Section 2 (Summary).pdf");
        assertThat(vectorStoreItems).isNotEmpty()
                                    .allSatisfy(vectorStoreItem -> {
                                        assertThat(vectorStoreItem.getFileName()).isEqualTo("Advanced Grammar - Section 2 (Summary).pdf");
                                    });

    }

}