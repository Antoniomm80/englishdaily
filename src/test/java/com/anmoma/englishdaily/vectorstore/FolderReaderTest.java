package com.anmoma.englishdaily.vectorstore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FolderReaderTest {
    private final FolderReader folderReader = new FolderReader();

    @Test
    @DisplayName("La lectura del directorio de pruebas debe devolver dos ficheros")
    void givenTestDocumentsFolderShouldReturnTwoFiles() {
        List<Path> testDocuments = folderReader.getFilenamesFromFolder("documents");

        assertThat(testDocuments).isNotEmpty()
                                 .hasSize(2);
    }
}