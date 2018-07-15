package com.example.quang.chatwithstranger.model;

public class JsonPhotoMessage {

    private int idConversation;
    private int idUser;
    private byte[] photo;
    private String time;
    private int idLastMess;

    public JsonPhotoMessage(int idConversation, int idUser, byte[] photo, String time,int idLastMess) {
        this.idConversation = idConversation;
        this.idUser = idUser;
        this.photo = photo;
        this.time = time;
        this.idLastMess = idLastMess;
    }

    public int getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(int idConversation) {
        this.idConversation = idConversation;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIdLastMess() {
        return idLastMess;
    }

    public void setIdLastMess(int idLastMess) {
        this.idLastMess = idLastMess;
    }
}
