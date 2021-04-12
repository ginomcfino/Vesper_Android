package com.company.vesper.watchlist;

import org.w3c.dom.NameList;

public class WatchListItem {
    public String Ticker;
    public String Name;
    public float closingPrice;
    public float dailyChange;

    public WatchListItem(String Ticker) {
        // Simple constructor for now
        // As we build the app more we can modify for the rest of the parameter
        this.Ticker = Ticker;
    }
    public WatchListItem(String Ticker, String Name, float closingPrice, float dailyChange )  {
        // Constructor for complete version
        this.Ticker = Ticker;
        this.Name = Name;
        this.closingPrice = closingPrice;
        this.dailyChange = dailyChange;
    }

}
