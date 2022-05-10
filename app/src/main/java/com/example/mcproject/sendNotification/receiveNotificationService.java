package com.example.mcproject.sendNotification;

import com.example.mcproject.MainActivity;
import com.example.mcproject.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mcproject.mainMenuActivity;
import com.example.mcproject.tempactivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class receiveNotificationService extends FirebaseMessagingService {
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title=remoteMessage.getData().get("Name");
//        message=remoteMessage.getData().get("Message");

        String CHANNEL_ID="MESSAGE";
        String CHANNEL_NAME="MESSAGE";
        NotificationManagerCompat manager=NotificationManagerCompat.from(receiveNotificationService.this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, tempactivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent activity = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            activity = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            activity = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
       

        Notification notification = new NotificationCompat.Builder(receiveNotificationService.this,CHANNEL_ID)
                .setSmallIcon(R.drawable.email_icon)
                .setContentTitle("hello")
                .setContentText("MessageTB.getText().toString()")
                .setAutoCancel(true)
                .setContentIntent(activity)
                .build();


        manager.notify(1,notification);



    }


}

