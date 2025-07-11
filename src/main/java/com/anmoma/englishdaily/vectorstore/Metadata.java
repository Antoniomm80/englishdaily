package com.anmoma.englishdaily.vectorstore;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Metadata(String fileName, int pageNumber, int endPageNumber) {
}
