package com.application.arenda.entities.utils.retrofit;

import com.google.gson.annotations.SerializedName;

public class ServerHandler<T> {
    @SerializedName("code")
    private int code;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private T response;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }
}