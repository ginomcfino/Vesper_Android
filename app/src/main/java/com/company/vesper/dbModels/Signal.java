package com.company.vesper.dbModels;

import com.company.vesper.State;
import com.company.vesper.services.AlphaVantage;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

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
                State.getGroup().incrementExcellentSignal();
            } else if (buy < value) {
                // If performance was better than buying price but didn't hit the sell price, we still consider it a good signal
                State.getGroup().incrementGoodSignal();
            }
        });

        State.getDatabase().collection("signals").document(snapshot.getId()).update("active", false);
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
