package com.anmoma.englishdaily.documentreader;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class FilenameProvider {
    private final FolderReader folderReader;

    public FilenameProvider(FolderReader folderReader) {
        this.folderReader = folderReader;
    }

    public String getRandomFilenameFromDocumentsFolder() {
        List<String> documents = folderReader.getFilenamesFromFolder("documents")
                                             .stream()
                                             .map(Path::getFileName)
                                             .map(Path::toString)
                                             .toList();
        return documents.get((int) (Math.random() * documents.size()));
    }

}
