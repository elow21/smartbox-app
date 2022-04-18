package com.example.smartbox_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.api.ResourceDescriptor;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotif extends FirebaseMessagingService {
    public PushNotif() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        super.onMessageReceived(message);
        System.out.println("From "+ message.getFrom());
        String notifTitle = message.getNotification().getTitle();
        String notifBody = message.getNotification().getBody();

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            notifTitle = message.getNotification().getTitle();
            notifBody = message.getNotification().getBody();
        }

        sendNotification(notifTitle, notifBody);

    }

    //Send push notification to user device
    private void sendNotification(String notifTitle, String notifBody) {
        //Directs users to delivery history page
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        //Creates the notification channel ID
        String channelId = "SmartBox App channel ID";
        //Get the user's default notification ringtone
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Creates the layout for our notification
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        //Notification icon is set to SmartBox app logo
                        .setSmallIcon(R.drawable.ic_stat_notif)
                        //Notification title and body will be obtained from the payload received
                        .setContentTitle(notifTitle)
                        .setContentText(notifBody)
                        //Notification will be removed once users click on it
                        .setAutoCancel(true)
                        //Set our notification ringtone as device default
                        .setSound(defaultSoundUri)
                        //Directs users to the SmartBox app once the notification is clicked
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Deliveries", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
