package com.document.management.Doctype;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ClassificationService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public String classifyText(String documentText) {
        String prompt = """
            You are a document classifier. Read the following content and reply in JSON format with:
            {
              "type": one of ["invoice", "contract", "report", "letter", "car document", "unknown"],
              "reason": short explanation
            }

            Content:
            """ + documentText;

        Map<String, Object> body = Map.of(
                "model", "gemma3:12b",
                "prompt", prompt,
                "stream", false // important!
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:11434/api/generate", request, String.class
        );

        String ollamaResponseText = response.getBody();
        logger.info(ollamaResponseText);

        return "";
    }
}
