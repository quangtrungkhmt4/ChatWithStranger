package com.example.quang.chatwithstranger.model;

public class JsonImage {

    private int id;
    private byte[] bytes;

    public JsonImage(int id, byte[] bytes) {
        this.id = id;
        this.bytes = bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
