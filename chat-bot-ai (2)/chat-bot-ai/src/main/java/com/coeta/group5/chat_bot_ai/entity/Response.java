package com.coeta.group5.chat_bot_ai.entity;

import reactor.core.publisher.Flux;

public class Response {

    private String responseText;

    public Response() {
    }

    public Response(String responseText) {
        this.responseText = responseText;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
