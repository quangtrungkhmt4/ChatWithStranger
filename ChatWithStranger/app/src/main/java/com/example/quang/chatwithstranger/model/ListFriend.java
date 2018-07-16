package com.example.quang.chatwithstranger.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ListFriend implements Serializable {
    private ArrayList<User> arrUser;
    private int idContact;

    public ListFriend(ArrayList<User> arrUser, int idContact) {
        this.arrUser = arrUser;
        this.idContact = idContact;
    }

    public ArrayList<User> getArrUser() {
        return arrUser;
    }

    public void setArrUser(ArrayList<User> arrUser) {
        this.arrUser = arrUser;
    }

    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }
}
