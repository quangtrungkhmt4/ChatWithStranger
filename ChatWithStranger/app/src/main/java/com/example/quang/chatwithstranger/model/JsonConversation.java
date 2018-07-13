package com.example.quang.chatwithstranger.model;

import java.io.Serializable;

public class JsonConversation implements Serializable{
    private String title;
    private int idUser;
    private int idGuest;
    private String time;

    public JsonConversation(String title, int idUser, int idGuest, String time) {
        this.title = title;
        this.idUser = idUser;
        this.idGuest = idGuest;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGuest() {
        return idGuest;
    }

    public void setIdGuest(int idGuest) {
        this.idGuest = idGuest;
    }
}
