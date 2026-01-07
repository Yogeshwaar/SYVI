package com.coeta.group5.chat_bot_ai.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    public User() {
    }

    public User(ObjectId id, String fullName, String userName, List<String> roles, String password, List<ChatEntry> chatEntries) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.roles = roles;
        this.password = password;
        this.chatEntries = chatEntries;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public @NonNull String getFullName() {
        return fullName;
    }

    public void setFullName(@NonNull String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<ChatEntry> getChatEntries() {
        return chatEntries;
    }

    public void setChatEntries(List<ChatEntry> chatEntries) {
        this.chatEntries = chatEntries;
    }

    @Id
    private ObjectId id;

    @NonNull
    private String fullName;
    @Indexed(unique = true)
    private String userName;
    @NonNull
    private String password;
    private List<String> roles;

    @DBRef
    private List<ChatEntry> chatEntries = new ArrayList<>();

}
