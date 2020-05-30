package com.application.arenda.entities.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.application.arenda.BuildConfig;
import com.application.arenda.R;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.mainWorkspace.activities.ActivityMain;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage.Notification;

import timber.log.Timber;

public class MessageService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Utils.messageOutput(this, "Message");

            Timber.d("Message - %s", remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Utils.messageOutput(this, "Notification");

            sendNotification(remoteMessage.getNotification());

            Timber.d("Message Notification Body: %s", remoteMessage.getNotification());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Utils.messageOutput(this, "Token - " + s);
        Timber.d("Token - %s", s);
    }

    @SuppressLint("ServiceCast")
    private void sendNotification(Notification not) {
        Intent intent = new Intent(this, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, BuildConfig.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(not.getTitle())
                        .setContentText(not.getBody())
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(BuildConfig.NOTIFICATION_CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}