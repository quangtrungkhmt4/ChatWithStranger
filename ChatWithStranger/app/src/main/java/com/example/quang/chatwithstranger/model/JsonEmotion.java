package com.example.quang.chatwithstranger.model;

public class JsonEmotion {
    private int idConversation;
    private int idUser;
    private String emo;
    private String time;

    public JsonEmotion(int idConversation, int idUser, String emo, String time) {
        this.idConversation = idConversation;
        this.idUser = idUser;
        this.emo = emo;
        this.time = time;
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

    public String getEmo() {
        return emo;
    }

    public void setEmo(String emo) {
        this.emo = emo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
