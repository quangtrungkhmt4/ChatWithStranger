package com.example.quang.chatwithstranger.model;

import java.io.Serializable;

public class Conversation implements Serializable{
    private int id;
    private String title;
    private String timeCreated;
    private int idUser;
    private int idGuest;

    public Conversation(int id, String title, String timeCreated, int idUser, int idGuest) {
        this.id = id;
        this.title = title;
        this.timeCreated = timeCreated;
        this.idUser = idUser;
        this.idGuest = idGuest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
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
