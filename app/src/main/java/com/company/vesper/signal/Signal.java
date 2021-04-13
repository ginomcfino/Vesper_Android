package com.company.vesper.signal;

import com.google.firebase.firestore.DocumentReference;

public class Signal {
    private static String TAG = Signal.class.getName();

    private String ticker;
    private double buy;
    private double sell;
    private double loss;
    private boolean active;
    private DocumentReference group;

    public Signal(String ticker, double buy, double sell, double loss, boolean active, DocumentReference group) {
        this.ticker = ticker;
        this.buy = buy;
        this.sell = sell;
        this.loss = loss;
        this.active = active;
        this.group = group;
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

    public DocumentReference getGroup() {
        return group;
    }
}
