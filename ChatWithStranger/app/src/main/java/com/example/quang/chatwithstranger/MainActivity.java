package com.example.quang.chatwithstranger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.quang.chatwithstranger.consts.Constants;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

    }
}
