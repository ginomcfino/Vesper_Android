package com.company.vesper.chat;

import com.company.vesper.State;
<<<<<<< Updated upstream
=======
import com.company.vesper.dbModels.Signal;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                String senderID = docSnap.getString("sender");
                String sender = State.getName(senderID);
                String message = docSnap.getString("message");
                long timestamp = docSnap.getLong("time");
                messages.add(new ChatMessage(sender, senderID, senderID.equals(State.getGroup().getSignaler()), message, timestamp));
=======
                if (docSnap.getString("type").equals("message")) {
                    String senderID = docSnap.getString("sender");
                    String sender = State.getName(senderID);
                    String message = docSnap.getString("message");
                    long timestamp = docSnap.getLong("time");
                    messages.add(new ChatMessage(sender, senderID, senderID.equals(State.getGroup().getSignaler()), message, timestamp));
                } else {
                    ChatMessage chm = new ChatMessage();
                    messages.add(chm);

                    // We need to fetch another piece of document, so increment
                    unloaded_count += 1;

                    docSnap.getDocumentReference("signal").get().addOnCompleteListener(signalTask -> {
                        DocumentSnapshot signal = signalTask.getResult();
                        Signal summary = new Signal(signal);
                        chm.setSignalMessage(summary);
                        // decrement unloaded pieces of document
                        unloaded_count -= 1;

                        // if everything has finished loading then we can return
                        if (unloaded_count == 0) {
                            callback.callback(messages);
                        }
                    });
                }

>>>>>>> Stashed changes
            }

            callback.callback(messages);
        });
    }

    public interface LoadChatCallback {
        void callback(List<ChatMessage> messages) ;
    }
}
