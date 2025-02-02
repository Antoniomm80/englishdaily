package com.anmoma.englishdaily.documentreader;

import com.anmoma.englishdaily.Fixtures;
import com.anmoma.englishdaily.catalog.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class FilenameProviderTest {
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    @BeforeEach
    void setUp() {
        BDDMockito.given(courseRepository.findAllVocabularySupportedCourses())
                  .willReturn(Fixtures.getVocabularySupportedCourses());

    }

    @DisplayName("Lecturas consecutivas de nombres de ficheros deben devolver nombres distintos")
    @Test
    void givenConsecutiveCallsShouldReturnDifferentFilenames() {
        FilenameProvider filenameProvider = new FilenameProvider(new FolderReader(), courseRepository);
        String filename1 = filenameProvider.getRandomFilenameFromDocumentsFolder();
        String filename2 = filenameProvider.getRandomFilenameFromDocumentsFolder();
        assertThat(filename1).isNotEqualTo(filename2);
    }

}