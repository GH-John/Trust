package com.application.arenda.entities.serverApi.chat;

public class ResponseConnect {
    private long idChat;

    public ResponseConnect() {
    }

    public ResponseConnect(long idChat) {
        this.idChat = idChat;
    }

    public long getIdChat() {
        return idChat;
    }

    public void setIdChat(long idChat) {
        this.idChat = idChat;
    }
}