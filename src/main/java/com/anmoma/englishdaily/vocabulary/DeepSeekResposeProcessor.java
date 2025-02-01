package com.anmoma.englishdaily.vocabulary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class DeepSeekResposeProcessor {
    VocabularyTerm process(String response) {
        Map<String, Object> jsonFields = deserializeJson(extractJsonPayload(response));
        String word = jsonFields.containsKey("term") ? (String) jsonFields.get("term") : (String) jsonFields.get("word");
        return new VocabularyTerm(null, word, (String) jsonFields.get("definition"), (String) jsonFields.get("part_of_speech"),
                (String) jsonFields.get("pronunciation"), (String) jsonFields.get("example_sentence"), (List) jsonFields.get("collocations"),
                (List) jsonFields.get("synonyms"));
    }

    private String extractJsonPayload(String llmResponse) {
        Pattern JSON_PATTERN = Pattern.compile("```json\\n(\n|.)*?```", Pattern.DOTALL);
        Matcher matcher = JSON_PATTERN.matcher(llmResponse);
        if (matcher.find()) {
            return matcher.group()
                          .replace("```json\n", "")
                          .replace("```", "")
                          .trim();
        }
        return null;
    }

    private Map<String, Object> deserializeJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing json", e);
        }
    }
}
