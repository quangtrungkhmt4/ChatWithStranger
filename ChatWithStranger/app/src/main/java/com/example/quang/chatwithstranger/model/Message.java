package com.example.quang.chatwithstranger.model;

public class Message {
    private int id;
    private int idConersation;
    private int idUser;
    private String text;
    private String photo;
    private String video;
    private String gif;
    private String emotion;
    private String time;

    public Message(int id, int idConersation, int idUser, String text, String photo, String video, String gif, String emotion, String time) {
        this.id = id;
        this.idConersation = idConersation;
        this.idUser = idUser;
        this.text = text;
        this.photo = photo;
        this.video = video;
        this.gif = gif;
        this.emotion = emotion;
        this.time = time;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getGif() {
        return gif;
    }

    public void setGif(String gif) {
        this.gif = gif;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
