package com.application.arenda.entities.serverApi.chat;

public class SendMessage {
    private String userToken;
    private long idUser_To;
    private long idChat;
    private String message;

    public SendMessage() {
    }

    public SendMessage(String userToken, long idUser_To, long idChat, String message) {
        this.userToken = userToken;
        this.idUser_To = idUser_To;
        this.idChat = idChat;
        this.message = message;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public long getIdUser_To() {
        return idUser_To;
    }

    public void setIdUser_To(long idUser_To) {
        this.idUser_To = idUser_To;
    }

    public long getIdChat() {
        return idChat;
    }

    public void setIdChat(long idChat) {
        this.idChat = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}