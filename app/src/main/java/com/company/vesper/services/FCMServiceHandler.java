package com.company.vesper.services;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMServiceHandler extends FirebaseMessagingService {
    private static String TAG = "FCMServiceHandler";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // TODO When a new token gets generated. We will want to push this to firestore.
    }

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
}
