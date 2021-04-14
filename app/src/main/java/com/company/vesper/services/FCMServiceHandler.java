package com.company.vesper.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.company.vesper.State;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Handler for Firebase Cloud Messaging.
 * All push messages from FCM will be received in {@link #onMessageReceived(RemoteMessage) onMessageReceived}
 */
public class FCMServiceHandler extends FirebaseMessagingService {
    private static String TAG = "FCMServiceHandler";

    /**
     * When a App is newly installed/wiped, it will generate a new token. We might want to push the token to firebase here for each user.
     * (Will need to handle how to deal with switching accounts for users.
     * @param s Not sure.
     */
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // TODO When a new token gets generated. We will want to push this to firestore.
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

}
