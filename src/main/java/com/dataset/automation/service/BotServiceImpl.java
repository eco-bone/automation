package com.dataset.automation.service;

import com.dataset.automation.config.OpenAiConfig;
import com.dataset.automation.dto.BotRequest;
import com.dataset.automation.dto.ChatGptRequest;
import com.dataset.automation.dto.ChatGptResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Service@Slf4j
public class BotServiceImpl implements BotService {

    private static RestTemplate restTemplate = new RestTemplate();

    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(OpenAiConfig.MEDIA_TYPE));
        headers.add(OpenAiConfig.AUTHORIZATION, OpenAiConfig.BEARER + OpenAiConfig.API_KEY);
        log.debug(headers.toString());
        log.info("HttpEntity Created.............");
        return new HttpEntity<>(chatRequest, headers);
    }

    public ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatRequestHttpEntity) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                OpenAiConfig.URL,
                chatRequestHttpEntity,
                ChatGptResponse.class);
        log.info("Request Sent to OpenAi..........");
        return responseEntity.getBody();
    }

//    public ChatGptResponse askQuestion(BotRequest botRequest) {
//        return this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequest(
//                                OpenAiConfig.MODEL,
//                                botRequest.getMessage(),
//                                OpenAiConfig.TEMPERATURE,
//                                OpenAiConfig.MAX_TOKEN,
//                                OpenAiConfig.TOP_P)));
//    }

    public String getOutput(BotRequest botRequest) {
        ChatGptResponse response = this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                OpenAiConfig.MODEL,
                                botRequest.getMessage(),
                                OpenAiConfig.TEMPERATURE,
                                OpenAiConfig.MAX_TOKEN,
                                OpenAiConfig.TOP_P)));
        log.info("Response Received from OpenAi......");
        return response.getChoices().get(0).getText();
    }
}
