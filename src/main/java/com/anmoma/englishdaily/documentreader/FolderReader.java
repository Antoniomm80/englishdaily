package com.anmoma.englishdaily.documentreader;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FolderReader {
    public List<Path> getFilenamesFromFolder(String dir) {
        String path = FolderReader.class.getClassLoader()
                                        .getResource(dir)
                                        .getPath();
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            return stream.filter(file -> !Files.isDirectory(file))
                         .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
