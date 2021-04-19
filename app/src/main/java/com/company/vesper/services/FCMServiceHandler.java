package com.company.vesper.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.company.vesper.R;
import com.company.vesper.State;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Handler for Firebase Cloud Messaging.
 * All push messages from FCM will be received in {@link #onMessageReceived(RemoteMessage) onMessageReceived}
 */
public class FCMServiceHandler extends FirebaseMessagingService {
    private static final String TAG = "FCMServiceHandler";

    /**
     * When a App is newly installed/wiped, it will generate a new token. We might want to push the token to firebase here for each user.
     * (Will need to handle how to deal with switching accounts for users.
     * @param s Not sure.
     */
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    /**
     * Event handler for FCM push messages. Need to handle routing here to send different intents to the app.
     * @param remoteMessage The message sent by FCM
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent();
        intent.setAction("NewMessage");
        intent.putExtra("chatID", remoteMessage.getData().get("chatID"));
        intent.putExtra("sender", remoteMessage.getData().get("sender"));
        intent.putExtra("message", remoteMessage.getData().get("message"));
        intent.putExtra("time", remoteMessage.getData().get("time"));

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        super.onMessageReceived(remoteMessage);
    }

    /**
     * Load the FCM token
     */
    public static void loadFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    State.setDeviceFCMToken(task.getResult());
                });
    }


    /**
     * Register this device to the user. Allows for this user to receive push notifications to this device
     * @param userID The user id of the user than owns this device.
     */
    public static void registerUser(String userID) {
        State.getDatabase().collection("users").document(userID).update("device", State.getDeviceFCMToken());
    }
}
