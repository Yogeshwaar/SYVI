package com.coeta.group5.chat_bot_ai.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_entries")
@Data
public class ChatEntry {
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public @NonNull String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(@NonNull String questionText) {
        this.questionText = questionText;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ChatEntry(ObjectId id, String questionText, String responseText, LocalDateTime timestamp) {
        this.id = id;
        this.questionText = questionText;
        this.responseText = responseText;
        this.timestamp = timestamp;
    }

    public ChatEntry(@NonNull String questionText, String responseText) {
        this.questionText = questionText;
        this.responseText = responseText;
        this.timestamp = LocalDateTime.now();
    }

    public ChatEntry() {
    }

    @Id
    private ObjectId id;
    @NonNull
    private String questionText;
    private String responseText;
    private LocalDateTime timestamp;

}
