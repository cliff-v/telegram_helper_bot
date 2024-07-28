package com.safronov.timofei.telegram.helper.bot.service.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safronov.timofei.telegram.helper.bot.model.db.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiService {

    @Value("${chat-gpt.token}")
    private String openAiKeyToken;

    public String handle(UserDao userDao, String message) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openAiKeyToken)
                .build();

        String requestBody = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
                + "\"stream\": true,"
                + "\"temperature\": 0.7"
                + "}";

        Flux<String> responseFlux = webClient.post()
                .uri("/chat/completions")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class);

        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuilder fullText = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        responseFlux.subscribe(chunk -> {
            fullText.append(extractTextFromChunk(chunk, objectMapper));
        }, future::completeExceptionally, () -> {
            future.complete(fullText.toString());
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractTextFromChunk(String chunk, ObjectMapper objectMapper) {
        if (chunk.equals("[DONE]")) {
            return "";
        }
        try {
            ChatCompletionChunk completionChunk = objectMapper.readValue(chunk, ChatCompletionChunk.class);

            StringBuilder text = new StringBuilder();
            for (ChatCompletionChunk.Choice choice : completionChunk.getChoices()) {
                if (choice.getDelta() != null && choice.getDelta().getContent() != null) {
                    text.append(choice.getDelta().getContent());
                }
            }
            return text.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
