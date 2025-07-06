package com.anmoma.englishdaily.documentreader;

import org.springframework.core.io.Resource;

import java.util.List;

public interface FolderReader {
    List<String> getResourceIdsFromFolder(String dir);

    List<String> getFilenamesFromFolder(String dir);

    Resource getResource(String dir);
}
