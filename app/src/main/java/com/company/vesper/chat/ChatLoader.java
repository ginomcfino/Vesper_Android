package com.company.vesper.chat;

import com.company.vesper.State;
import com.company.vesper.dbModels.Signal;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * Loader for chat fragment to get messages from the database and then pass it back through the UI callback.
 * Keeps track of the earliest loaded data so that it can load back further when user scrolls upwards.
 */
public class ChatLoader {
    private long lastLoadTime;
    private int unloaded_count = 0;


    private String TAG = ChatLoader.class.getName();

    public ChatLoader() {
        lastLoadTime = Long.MAX_VALUE;
    }

    public void loadMessages(String chatId, LoadChatCallback callback) {
        loadMessages(chatId, lastLoadTime, callback);
    }


    public void loadMessages(String chatId, Long latestTime, LoadChatCallback callback) {
        State.getDatabase().collection("messages")
                .whereEqualTo("chatID", chatId)
                .whereLessThan("time", latestTime)
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(10).get().addOnCompleteListener(task -> {
            List<ChatMessage> messages = new ArrayList<>();

            QuerySnapshot snapshot = task.getResult();

            for (DocumentSnapshot docSnap : snapshot.getDocuments()) {
                lastLoadTime = Math.min(docSnap.getLong("time"), lastLoadTime);

                if (docSnap.getString("type").equals("message")) {
                    String senderID = docSnap.getString("sender");
                    String sender = State.getName(senderID);
                    String message = docSnap.getString("message");
                    long timestamp = docSnap.getLong("time");
                    messages.add(0, new ChatMessage(sender, senderID, senderID.equals(State.getGroup().getSignaler()), message, timestamp));
                } else {
                    ChatMessage chm = new ChatMessage();
                    messages.add(0, chm);

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

            }

            // only return if everything was loaded, otherwise we let the load-callbacks to return the messages
            if (unloaded_count == 0) {
                callback.callback(messages);
            }
        });
    }

    public interface LoadChatCallback {
        void callback(List<ChatMessage> messages) ;
    }

}
