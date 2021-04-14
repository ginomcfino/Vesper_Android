package com.company.vesper.chat;

import com.company.vesper.lib.Helpers;
import com.company.vesper.signal.Signal;

public class ChatMessage {
    private static String TAG = ChatMessage.class.getName();

    String senderName = "";
    String senderID = "'";
    boolean isLeader;

    String type;

    String message;
    String timestamp;

    Signal signal;



    public ChatMessage() {
        type = "signal";
    }

    public void setSignalMessage(Signal signal) {
        this.signal = signal;
    }

    public ChatMessage(String sender, String senderID, boolean isLeader, String message, long timestamp) {
        type = "chat";
        this.senderName = sender;

        this.senderID = senderID;
        this.isLeader = isLeader;

        this.message = message;

        this.timestamp = Helpers.getLongTimestamp(timestamp);

    }
}
