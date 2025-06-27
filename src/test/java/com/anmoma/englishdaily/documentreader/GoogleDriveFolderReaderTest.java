package com.anmoma.englishdaily.documentreader;

import com.anmoma.englishdaily.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class GoogleDriveFolderReaderTest {
    @Autowired
    private GoogleDriveFolderReader googleDriveFolderReader;

    private String folderId;

    @Test
    @DisplayName("Dado un path de carpeta de Google Drive, el servicio debe recuperar los identificadores de los archivos")
    void givenAGDriveFolderIdShouldServiceRetrieveFilenames() {
        List<String> filenamesFromFolder = googleDriveFolderReader.getFilenamesFromFolder("advancedgrammarchallenge");
        assertThat(filenamesFromFolder).isNotEmpty()
                                       .hasSize(13)
                                       .containsAll(List.of("1u1J0PcJicu_FZt4Z715eIqyN4cE8WKGR", "1mAtdJ26iHCEo4nkMAQ8c8qQO3e004ATU",
                                               "1ygdy6-EZWifYxweZ-1XXJyFXuPuBsS0v", "1iiAXaA19pfiQMYy9Lx_IGGt3FKXtKRSD",
                                               "1huhS5Ckw6DOgDpd_G9kQ8DSYyKCF4ZYk", "1Om0clRlR7tLqmGJwQ5TY_rxmC_zvD1BM",
                                               "1ip1PWF2kgSMRmyJ5SO7SVo_QO_F78OOG", "1h9Mtw8_wwpOtBU7nmUmvVjx_163eYnVB",
                                               "13ye6rRtlh4JLClq1vCvo0NhMNNbSWpD1", "1IvTeUzyLq0LEm4VLyksgp2YBXKbXoUFi",
                                               "1KMnI6eQFdKrssQw1NGwKurKjtLndHnL6", "1VPqNiPc2UN5CTxiMS6gDiU1u_G32E_PQ",
                                               "1iTktF_VwcbzRYuNqRrjme0nScUzOLguC"));
    }

    @Test
    @DisplayName("Dado un identificador de archivo, el servicio debe recuperar el recurso asociado")
    void givenAGDriveResourceIdShouldRetrieveResource() throws IOException {
        Resource resource = googleDriveFolderReader.getResource("1dB1w8kXv6OOOBHl4sJa0vJeyw5RdYpn_");
        assertThat(resource).isNotNull();
        assertThat(resource.getFilename()).isEqualTo("Advanced Grammar - Section 4 (Summary).pdf");
        assertThat(resource.contentLength()).isGreaterThan(100);
    }
}