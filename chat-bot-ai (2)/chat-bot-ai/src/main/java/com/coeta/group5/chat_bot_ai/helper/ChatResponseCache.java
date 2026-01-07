package com.coeta.group5.chat_bot_ai.helper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatResponseCache {
    // Thread-safe cache to store responses and questions
    private final ConcurrentHashMap<String, List<String>> responseCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> questionCache = new ConcurrentHashMap<>();

    public void addResponse(String username, String responseChunk) {
        responseCache
                .computeIfAbsent(username, key -> new CopyOnWriteArrayList<>()) // Ensure thread safety
                .add(responseChunk);
    }

    public void storeQuestion(String username, String question) {
        questionCache.put(username, question);
    }

    public List<String> getResponses(String username) {
        return responseCache.getOrDefault(username, List.of());
    }

    public String getQuestion(String username) {
        return questionCache.getOrDefault(username, "");
    }

    public void removeResponses(String username) {
        responseCache.remove(username);
        questionCache.remove(username);
    }
}