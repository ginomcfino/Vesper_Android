package com.company.vesper.chat;

import com.company.vesper.State;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatLoader {
    private static String TAG = ChatLoader.class.getName();

    public static void loadMessages(String chatId, LoadChatCallback callback) {
        loadMessages(chatId, Long.MAX_VALUE, callback);
    }


    public static void loadMessages(String chatId, Long latestTime, LoadChatCallback callback) {
        State.getDatabase().collection("messages")
                .whereEqualTo("chatID", chatId)
                .whereLessThan("time", latestTime)
                .orderBy("time")
                .limit(20).get().addOnCompleteListener(task -> {
            List<ChatMessage> messages = new ArrayList<>();

            QuerySnapshot snapshot = task.getResult();

            for (DocumentSnapshot docSnap : snapshot.getDocuments()) {
                String senderID = docSnap.getString("sender");
                String sender = State.getName(senderID);
                String message = docSnap.getString("message");
                long timestamp = docSnap.getLong("time");
                messages.add(new ChatMessage(sender, senderID, senderID.equals(State.getGroup().getSignaler()), message, timestamp));
            }

            callback.callback(messages);
        });
    }

    public interface LoadChatCallback {
        void callback(List<ChatMessage> messages) ;
    }
}
