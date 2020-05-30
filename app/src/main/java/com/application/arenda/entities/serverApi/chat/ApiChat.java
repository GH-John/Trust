package com.application.arenda.entities.serverApi.chat;

import android.annotation.SuppressLint;
import android.content.Context;

import com.application.arenda.entities.serverApi.client.ApiClient;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public final class ApiChat {
    @SuppressLint("StaticFieldLeak")
    private static ApiChat instance;
    private static WebSocket socket;

    private ApiChat(Context context, WebSocketListener listener) {
        socket = ApiClient.getWebSocketApi(listener);
    }

    public static ApiChat getInstance(Context context, WebSocketListener listener) {
        if (instance == null)
            instance = new ApiChat(context, listener);
        return instance;
    }
}