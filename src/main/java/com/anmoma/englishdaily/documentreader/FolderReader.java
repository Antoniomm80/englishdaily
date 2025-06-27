package com.anmoma.englishdaily.documentreader;

import org.springframework.core.io.Resource;

import java.util.List;

public interface FolderReader {
    List<String> getFilenamesFromFolder(String dir);

    Resource getResource(String dir);
}
