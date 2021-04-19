package com.company.vesper.watchlist;

/**
 * Holder Item for watchlist
 */
public class WatchListItem {
    public String Ticker;
    public String Name;
    public double dailyChange;
    public String closingPriceLoading;
    public String dailyChangeLoading;

    public WatchListItem(String Ticker) {
        // Simple constructor for now
        // As we build the app more we can modify for the rest of the parameter
        this.Ticker = Ticker;
    }
    public WatchListItem(String Ticker, double currentPrice, double dailyChange, String percentChange )  {
        // Constructor for complete version
        this.Ticker = Ticker;
        this.Name = Name;
        this.currentPrice = currentPrice;
        this.dailyChange = dailyChange;
        this.percentChange = percentChange;
    }
}
