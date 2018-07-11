package com.example.quang.chatwithstranger.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quang.chatwithstranger.consts.Constants;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class Prefs {

    private String key;
    private User value;
    private Context context;

    public Prefs(Context context, String key, User value) {
        this.key = key;
        this.value = value;
        this.context = context;
    }

    public Prefs(Context context) {
        this.context = context;
    }

    public void comitUser(){
        SharedPreferences mPrefs = context.getSharedPreferences("chatwithstranger",MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public User getUser(Context context, String key){
        SharedPreferences mPrefs = context.getSharedPreferences("chatwithstranger",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(key, "");
        return gson.fromJson(json, User.class);
    }

    public void deleteUser(Context context, String key){
        SharedPreferences mPrefs = context.getSharedPreferences("chatwithstranger",MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, "");
        prefsEditor.commit();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getValue() {
        return value;
    }

    public void setValue(User value) {
        this.value = value;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
