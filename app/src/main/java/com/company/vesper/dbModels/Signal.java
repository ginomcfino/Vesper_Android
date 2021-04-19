package com.company.vesper.dbModels;

import com.company.vesper.State;
import com.company.vesper.lib.HttpConnectionLibrary;
import com.company.vesper.services.AlphaVantage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Container class that holds the data relevant to a signal
 */
public class Signal {
    private static String TAG = Signal.class.getName();
    private DocumentSnapshot snapshot;

    private String ticker;
    private double buy;
    private double sell;
    private double loss;
    private boolean active;
    private List<String> upvoted_users;
    private DocumentReference group;


    public Signal(DocumentSnapshot doc) {
        this.snapshot = doc;

        this.ticker = doc.getString("ticker");
        this.buy = doc.getDouble("buy");
        this.sell = doc.getDouble("sell");
        this.loss = doc.getDouble("loss");
        this.active = doc.getBoolean("active");
        this.group = doc.getDocumentReference("group");
        this.upvoted_users = (List<String>) doc.get("upvote_users");
    }

    public String getTicker() {
        return ticker;
    }

    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    public double getLoss() {
        return loss;
    }

    public boolean isActive() {
        return active;
    }

    public boolean userUpvoted(String userID) {
        return upvoted_users.contains(userID);
    }

    public void closeSignal() {
        AlphaVantage.getCurrentStockData(ticker, response -> {
            double value = response.currentPrice;

            if (sell < value) {
                // If the performance is better than the sell price, this was an excellent signal
                group.get().addOnCompleteListener(task -> {
                    DocumentSnapshot g = task.getResult();
                    new GroupInfo(g).incrementExcellentSignal();
                });
            } else if (buy < value) {
                group.get().addOnCompleteListener(task -> {
                    DocumentSnapshot g = task.getResult();
                    new GroupInfo(g).incrementGoodSignal();
                });
            }
        });

        State.getDatabase().collection("signals").document(snapshot.getId()).update("active", false);

        HashMap<String, Object> params = new HashMap<>();
        params.put("chatID", group.getId());
        params.put("signal", snapshot.getId());
        params.put("sourceDevice", State.getDeviceFCMToken());

        long time = (Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000L);
        params.put("time", Long.toString(time));

        HttpConnectionLibrary.sendPOST("http://128.31.25.3/close-signal", params);

        this.active = false;
    }

    public void addUpvote(String userID) {
        upvoted_users.add(userID);
        State.getDatabase().collection("signals").document(snapshot.getId()).update("upvote_users", upvoted_users);

    }

    public void removeUpvote(String userID) {
        upvoted_users.remove(userID);
        State.getDatabase().collection("signals").document(snapshot.getId()).update("upvote_users", upvoted_users);
    }

    public DocumentReference getGroup() {
        return group;
    }
}
