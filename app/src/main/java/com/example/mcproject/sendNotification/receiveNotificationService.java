package com.example.mcproject.sendNotification;

import com.example.mcproject.AppDatabase;
import com.example.mcproject.MainActivity;
import com.example.mcproject.R;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mcproject.mainMenuActivity;
import com.example.mcproject.notficationEntity;
import com.example.mcproject.tempactivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class receiveNotificationService extends FirebaseMessagingService {
    String name,bloodGroup, mobileNo, requirement, quantity, currentTime, userID;
    Double latitude, longitude;
    boolean sendByReceiver = false;

    AppDatabase databaseObj;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendByReceiver = Boolean.parseBoolean(remoteMessage.getData().get("sendByReceiver"));
        if(sendByReceiver == false) {
            name = remoteMessage.getData().get("Name");
            bloodGroup = remoteMessage.getData().get("bloodGroup");
            mobileNo = remoteMessage.getData().get("mobileNo");
            requirement = remoteMessage.getData().get("requirement");
            userID = remoteMessage.getData().get("userID");
            latitude = Double.valueOf(remoteMessage.getData().get("latitude"));
            longitude = Double.valueOf(remoteMessage.getData().get("longitude"));
            quantity = remoteMessage.getData().get("quantity");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            currentTime = formatter.format(date);


            databaseObj = AppDatabase.getDatabaseInstance(getApplicationContext());

            databaseObj.userDao().insertnotificationEntry(new notficationEntity(name, latitude, longitude, requirement, bloodGroup, mobileNo, quantity, currentTime, userID));


            String CHANNEL_ID = "MESSAGE";
            String CHANNEL_NAME = "MESSAGE";


            NotificationManagerCompat manager = NotificationManagerCompat.from(receiveNotificationService.this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(this, tempactivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("notificationService", "hello");
//        intent.putExtras(bundle);
            intent.putExtra("callFromNotification", true);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent activity = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                activity = PendingIntent.getActivity
                        (this, 0, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                activity = PendingIntent.getActivity
                        (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            }

            Notification notification = new NotificationCompat.Builder(receiveNotificationService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.lifeline_logo)
                    .setContentTitle("Patient: " + name + " requested via Lifeline")
                    .setContentText("Requested For: " + requirement + " donation")
                    .setAutoCancel(true)
                    .setContentIntent(activity)
                    .build();


            manager.notify(1, notification);
        }

        else
        {
            String CHANNEL_ID = "MESSAGE";
            String CHANNEL_NAME = "MESSAGE";


            NotificationManagerCompat manager = NotificationManagerCompat.from(receiveNotificationService.this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }



            Notification notification = new NotificationCompat.Builder(receiveNotificationService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.lifeline_logo)
                    .setContentTitle("Your Request has been Accepted")
                    .setContentText("The Donor will contact you soon!")
                    .setAutoCancel(true)
                    .build();


            manager.notify(1, notification);
        }



    }


}

