package com.example.lpgcontroller;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage);
    }

    public void showNotification(RemoteMessage remoteMessage) {

//        Intent intent = null;
//        Map<String,String> data = remoteMessage.getData();
        RemoteMessage.Notification message = remoteMessage.getNotification();
//        String notificationType = data.get("notificationType")!=null ? data.get("notificationType") : "";

//        if(notificationType.equals("RaidMessage"))
//        {
//            if (message!=null)
//            {
//                String messageTitle = message.getTitle();
//                String messageBody = message.getBody();
//                String click_action = message.getClickAction();
//
//                String raidId = data.get("raid");
//                String raidPokemon = data.get("raidPokemon");
//
//                intent = new Intent(click_action);
//                intent.putExtra("notificationType","RaidMessage");
//                intent.putExtra("raid",raidId);
//                intent.putExtra("raidPokemon",raidPokemon);
//            }
//        }
//        else if (data.get("notificationType").equals("PersonalMessage"))
//        {
//            if (message!=null)
//            {
//                String messageTitle = message.getTitle();
//                String messageBody = message.getBody();
//                String click_action = message.getClickAction();
//
//                String friendUid = data.get("friendId");
//                String friendUsername = data.get("userName");
//                String sharedKey = data.get("sharedKey");
//
//                intent = new Intent(click_action);
//                intent.putExtra("notificationType","PersonalMessage");
//                intent.putExtra("friendId",friendUid);
//                intent.putExtra("userName",friendUsername);
//                intent.putExtra("sharedKey",sharedKey);
//            }
//        }

//        if (intent != null) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }

//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        String NOTIFICATION_CHANNEL_ID = "com.example.lpgcontroller";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Remote Raid");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 100, 200, 100});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.gas_1)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}
