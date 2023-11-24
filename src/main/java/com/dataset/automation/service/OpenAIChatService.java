package com.dataset.automation.service;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIChatService {

    //    @Value("${openai.api.key}")
//    private String apiKey = "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";
    private String apiKey = "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";
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatCompletion(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request body
        String requestBody = "{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\",\"content\": \"" + userMessage + "\"}]}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                OPENAI_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        JSONObject jsonObject = new JSONObject(response.getBody());
        String content = jsonObject.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
        return content;
    }
}
