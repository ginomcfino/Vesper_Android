package com.company.vesper.chat;

import com.company.vesper.lib.Helpers;

public class ChatMessage {
    private static String TAG = ChatMessage.class.getName();

    public final String senderName;
    public final String senderID;
    public final boolean isLeader;

    public final String message;
    public final String timestamp;

    public ChatMessage(String sender, String senderID, boolean isLeader, String message, long timestamp) {
        this.senderName = sender;

        this.senderID = senderID;
        this.isLeader = isLeader;

        this.message = message;

        this.timestamp = Helpers.getLongTimestamp(timestamp);
    }
}
