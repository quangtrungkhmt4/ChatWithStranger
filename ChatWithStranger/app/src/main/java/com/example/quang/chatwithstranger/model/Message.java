package com.example.quang.chatwithstranger.model;

public class Message {
    private int id;
    private int idConersation;
    private int idUser;
    private String text;
    private String photo;
    private String time;
    private String avatar;

    public Message(int id, int idConersation, int idUser, String text, String photo, String time, String avatar) {
        this.id = id;
        this.idConersation = idConersation;
        this.idUser = idUser;
        this.text = text;
        this.photo = photo;
        this.time = time;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdConersation() {
        return idConersation;
    }

    public void setIdConersation(int idConersation) {
        this.idConersation = idConersation;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
