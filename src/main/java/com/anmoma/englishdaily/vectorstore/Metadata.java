package com.anmoma.englishdaily.vectorstore;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Metadata {
    private String fileName;
    private int pageNumber;

    public String getFileName() {
        return fileName;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
