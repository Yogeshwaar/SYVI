package com.coeta.group5.chat_bot_ai.entity;

public class Request {

    private String question;

    public Request(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Request() {
    }
}
