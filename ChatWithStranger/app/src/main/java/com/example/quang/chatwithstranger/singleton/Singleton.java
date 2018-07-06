package com.example.quang.chatwithstranger.singleton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class Singleton {
    private static Singleton instance;
    Socket mSocket;

    private Singleton() {}

    public static Singleton Instance()
    {
        if (instance == null)
        {
            instance = new Singleton();
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
