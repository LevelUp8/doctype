package com.document.management.Doctype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ClassificationSpringBootService {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationSpringBootService.class);

    public ChatClient chatClient;

    public ClassificationSpringBootService(ChatClient chatClient)
    {
        this.chatClient = chatClient;
    }

    public String classifyText(String documentText) throws JsonProcessingException {
        String jsonSchema = """
        {
            "documentType": "string",
            "reason": "string"
        }
        """;



        String initial = """
            You are a document classifier. Read the following content and reply in JSON format with:
            {
              "documentType": one of ["invoice", "contract", "report", "letter", "car document", "unknown"],
              "reason": short explanation
            }
            Content:
            
            """ + documentText;

        Prompt prompt = new Prompt(initial,
                OllamaOptions.builder()
                        .model("gemma3:12b")
                        //.format(new ObjectMapper().readValue(jsonSchema, Map.class))
                        .build());

        String rawResponse = this.chatClient.prompt(prompt).call().content();
        String json = extractJsonFromCodeBlock(rawResponse);


        ObjectMapper mapper = new ObjectMapper();
        Document result = mapper.readValue(json, Document.class);
        System.out.println("Type: " + result.documentType());
        System.out.println("Reason: " + result.reason());

        logger.info(json);
        return json;
    }

    public static String extractJsonFromCodeBlock(String input) {
        Pattern pattern = Pattern.compile("(?s)```(?:json)?\\s*(\\{.*?\\})\\s*```");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return input; // fallback if no match
    }
}
