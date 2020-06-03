package com.application.arenda.entities.serverApi.chat;

import com.application.arenda.entities.serverApi.client.CodeHandler;

public class ChatError {
    private CodeHandler codeHandler;
    private String error;

    public CodeHandler getCodeHandler() {
        return codeHandler;
    }

    public void setCodeHandler(CodeHandler codeHandler) {
        this.codeHandler = codeHandler;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}