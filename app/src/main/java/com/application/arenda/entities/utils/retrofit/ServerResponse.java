package com.application.arenda.entities.utils.retrofit;

import com.google.gson.annotations.SerializedName;

public class ServerResponse<T> extends ApiHandler{
    @SerializedName("response")
    private T response;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}