package com.anmoma.englishdaily.documentreader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathFolderReaderTest {
    private final ClassPathFolderReader folderReader = new ClassPathFolderReader();

    @Test
    @DisplayName("La lectura del directorio de pruebas debe devolver ficheros")
    void givenTestDocumentsFolderShouldReturnTwoFiles() {
        List<String> testDocuments = folderReader.getFilenamesFromFolder("documents/slangchallenge");
        assertThat(testDocuments).isNotEmpty()
                                 .hasSizeGreaterThan(10)
                                 .satisfiesOnlyOnce(p -> assertThat(p).contains("Slang Challenge (English with Lucy) 1.pdf"));
    }
}