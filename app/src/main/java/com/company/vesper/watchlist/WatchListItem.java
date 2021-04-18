package com.company.vesper.watchlist;

import org.w3c.dom.NameList;

public class WatchListItem {
    public String Ticker;
    public String Name;
<<<<<<< Updated upstream
    public float closingPrice;
    public float dailyChange;
=======
    public double currentPrice;
    public double closingPrice;
    public double dailyChange;
>>>>>>> Stashed changes

    public WatchListItem(String Ticker) {
        // Simple constructor for now
        // As we build the app more we can modify for the rest of the parameter
        this.Ticker = Ticker;
    }
<<<<<<< Updated upstream
    public WatchListItem(String Ticker, String Name, float closingPrice, float dailyChange )  {
=======
    public WatchListItem(String Ticker, String Name, double currentPrice, double dailyChange )  {
>>>>>>> Stashed changes
        // Constructor for complete version
        this.Ticker = Ticker;
        this.Name = Name;
        this.currentPrice = currentPrice;
        this.dailyChange = dailyChange;
    }
}
