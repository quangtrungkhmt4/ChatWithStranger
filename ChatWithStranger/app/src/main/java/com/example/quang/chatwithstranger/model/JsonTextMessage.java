package com.example.quang.chatwithstranger.model;

public class JsonTextMessage {
    private int idConversation;
    private int idUser;
    private String text;
    private String time;
    private int idLastMess;

    public JsonTextMessage(int idConversation, int idUser, String text, String time,int idLastMess) {
        this.idConversation = idConversation;
        this.idUser = idUser;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
