package com.coeta.group5.chat_bot_ai.entity;

import org.bson.types.ObjectId;

public class NameEmailDTO {

    private ObjectId id;
    private String fullName;
    private String userName;

    public NameEmailDTO() {
    }

    public NameEmailDTO(ObjectId id, String fullName, String userName) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
