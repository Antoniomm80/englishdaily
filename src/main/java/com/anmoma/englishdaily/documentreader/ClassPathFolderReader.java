package com.anmoma.englishdaily.documentreader;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ClassPathFolderReader implements FolderReader {
    private static final String DOCUMENTS_FOLDER = "documents/";

    @Override
    public List<String> getResourceIdsFromFolder(String dir) {
        if (!dir.startsWith(DOCUMENTS_FOLDER)) {
            dir = DOCUMENTS_FOLDER + dir;
        }
        String path = ClassPathFolderReader.class.getClassLoader()
                                                 .getResource(dir)
                                                 .getPath();
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            return stream.filter(file -> !Files.isDirectory(file))
                         .map(p -> p.getFileName()
                                    .toString())
                         .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getFilenamesFromFolder(String dir) {
        return getResourceIdsFromFolder(dir);
    }

    @Override
    public Resource getResource(String dir) {
        String path = ClassPathFolderReader.class.getClassLoader()
                                                 .getResource(dir)
                                                 .getPath();
        return new org.springframework.core.io.PathResource(Paths.get(path));
    }
}
