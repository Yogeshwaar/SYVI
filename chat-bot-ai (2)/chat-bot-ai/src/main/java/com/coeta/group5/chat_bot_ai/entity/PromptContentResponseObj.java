package com.coeta.group5.chat_bot_ai.entity;

public class PromptContentResponseObj {

    private String publicPrompt;
    private String internalPrompt;

    public PromptContentResponseObj() {
    }

    public String getPublicPrompt() {
        return publicPrompt;
    }

    public void setPublicPrompt(String publicPrompt) {
        this.publicPrompt = publicPrompt;
    }

    public String getInternalPrompt() {
        return internalPrompt;
    }

    public void setInternalPrompt(String internalPrompt) {
        this.internalPrompt = internalPrompt;
    }

    public PromptContentResponseObj(String publicPrompt, String internalPrompt) {
        this.publicPrompt = publicPrompt;
        this.internalPrompt = internalPrompt;
    }
}
