package com.anmoma.englishdaily.documentreader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FilenameProviderTest {
    @DisplayName("Lecturas consecutivas de nombres de ficheros deben devolver nombres distintos")
    @Test
    void givenConsecutiveCallsShouldReturnDifferentFilenames() {
        FilenameProvider filenameProvider = new FilenameProvider(new FolderReader());
        String filename1 = filenameProvider.getRandomFilenameFromDocumentsFolder();
        String filename2 = filenameProvider.getRandomFilenameFromDocumentsFolder();
        assertThat(filename1).isNotEqualTo(filename2);
    }

}