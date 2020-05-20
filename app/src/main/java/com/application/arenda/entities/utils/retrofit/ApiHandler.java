package com.application.arenda.entities.utils.retrofit;

import com.google.gson.annotations.SerializedName;

public class ApiHandler {
    @SerializedName("code")
    private CodeHandler handler;

    @SerializedName("error")
    private String error;

    public CodeHandler getHandler() {
        return handler;
    }

    public void setHandler(CodeHandler handler) {
        this.handler = handler;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}