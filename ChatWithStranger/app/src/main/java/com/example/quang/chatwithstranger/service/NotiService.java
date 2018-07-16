package com.example.quang.chatwithstranger.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.chatwithstranger.R;

public class NotiService extends Service {


    private NotificationCompat.Builder notBuilder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Tin nhắn mới.",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Chat with Stranger")
                    .setContentText("Bạn có tin nhắn mới.").build();

            startForeground(1, notification);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Chat with Stranger";
            String description = "Bạn có tin nhắn mới";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_message);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_message));
            mBuilder.setContentTitle("Chat with Stranger");
            mBuilder.setContentText("Bạn có tin nhắn mới.");

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(111, mBuilder.build());
        }


        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(500);
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("destroy","-----------destroy");
    }
}
