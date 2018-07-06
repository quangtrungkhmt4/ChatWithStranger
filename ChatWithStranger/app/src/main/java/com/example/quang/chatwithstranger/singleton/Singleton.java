package com.example.quang.chatwithstranger.singleton;

import android.util.Log;

import com.example.quang.chatwithstranger.consts.Constants;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class Singleton {
    private static Singleton instance;
    Socket mSocket;

    private Singleton() {
        try {
            mSocket = IO.socket(Constants.PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Singleton Instance()
    {
        if (instance == null)
        {
            instance = new Singleton();
            Log.e("----------------","new");
        }
        return instance;
    }
    public Socket getmSocket()
    {
        return this.mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }
}
