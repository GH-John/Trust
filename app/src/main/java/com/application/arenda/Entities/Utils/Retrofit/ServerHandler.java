package com.application.arenda.Entities.Utils.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerHandler<T> {
    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("response")
    @Expose
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