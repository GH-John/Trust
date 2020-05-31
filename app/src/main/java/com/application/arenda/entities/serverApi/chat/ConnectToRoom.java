package com.application.arenda.entities.serverApi.chat;

public class ConnectToRoom {
    private String userToken;
    private long idUser_To;

    public ConnectToRoom() {
    }

    public ConnectToRoom(String userToken, long idUser_To) {
        this.userToken = userToken;
        this.idUser_To = idUser_To;
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
}