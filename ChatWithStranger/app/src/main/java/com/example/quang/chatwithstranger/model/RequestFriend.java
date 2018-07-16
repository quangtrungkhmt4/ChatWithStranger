package com.example.quang.chatwithstranger.model;

public class RequestFriend {

    private int idRequest;
    private int idRequester;
    private String name;
    private String image;


    public RequestFriend(int idRequest, int idRequester, String name, String image) {
        this.idRequest = idRequest;
        this.idRequester = idRequester;
        this.name = name;
        this.image = image;
    }

    public int getIdRequester() {
        return idRequester;
    }

    public void setIdRequester(int idRequester) {
        this.idRequester = idRequester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }
}
