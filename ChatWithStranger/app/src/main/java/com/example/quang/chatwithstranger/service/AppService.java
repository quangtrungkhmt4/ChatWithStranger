package com.example.quang.chatwithstranger.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.quang.chatwithstranger.MessageActivity;
import com.example.quang.chatwithstranger.consts.Constants;
import com.example.quang.chatwithstranger.model.Prefs;
import com.example.quang.chatwithstranger.model.User;
import com.example.quang.chatwithstranger.singleton.Singleton;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Calendar;

public class AppService extends Service {

    Socket mSocket;

    Emitter.Listener onResultNewMessage;
    {
        try {
            mSocket = IO.socket(Constants.PORT);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        onResultNewMessage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                resultNewMessage(args[0]);
            }
        };
    }

    private void resultNewMessage(final Object arg) {

        Handler handler = new Handler(getApplicationContext().getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject obj = (JSONObject) arg;
                Prefs prefs = new Prefs(getApplicationContext());
                User user = prefs.getUser(getApplicationContext(),Constants.KEY_USER_LOGIN);
                if (user != null){
                    int id = 0;
                    try {
                        id = obj.getInt("idFriend");
                        int idCon = obj.getInt("idCon");
                        SharedPreferences pre=getSharedPreferences("data_chat", MODE_PRIVATE);
                        SharedPreferences.Editor edit=pre.edit();
                        String listCon = pre.getString("listCon","");
                        if (!listCon.contains(""+idCon)){
                            listCon = listCon + idCon + ",";
                        }
                        edit.putString("listCon",listCon);
                        edit.commit();
                        if (id == user.getId() && !MessageActivity.CHECK_MESSAGE_ACTIVITY_IS_RUNNING){
                            Intent i = new Intent(AppService.this,NotiService.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(i);
                            }
                            else {
                                startService(i);
                            }
                        }else if (id == user.getId() && MessageActivity.CHECK_MESSAGE_ACTIVITY_IS_RUNNING
                                && idCon != MessageActivity.ID_CONVERSATION){
                            Intent i = new Intent(AppService.this,NotiService.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(i);
                            }
                            else {
                                startService(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSocket.connect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //thread.start();

//        Intent i = new Intent(AppService.this,AlarmService.class);
//        startService(i);
        mSocket.on(Constants.SERVER_SEND_NEW_MESSAGE,onResultNewMessage);
        return START_STICKY;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
