package com.application.arenda.entities.serverApi.chat;

import android.annotation.SuppressLint;
import android.content.Context;

import com.application.arenda.entities.serverApi.client.ApiClient;

import io.socket.client.Socket;

public final class ApiChat {
    @SuppressLint("StaticFieldLeak")
    private static ApiChat instance;
    private static Socket socket;

    private ApiChat(Context context) {
        socket = ApiClient.getWebSocketApi();
    }

    public static ApiChat getInstance(Context context) {
        if (instance == null)
            instance = new ApiChat(context);
        return instance;
    }

    public static Socket getSocket() {
        return socket;
    }
}