package com.company.vesper.services;

import android.content.Context;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Stock;
import com.company.vesper.MainActivity;
import com.company.vesper.databinding.FragmentHomeBinding;
import com.company.vesper.lib.HttpConnectionLibrary;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantage {

    public static class StockData {
        public String Ticker;
        public String Name;
        public Double closingPrice;
        public Double dailyChange;
        public Double currentPrice;
    }


    private static String url = "http://128.31.25.3/alpha-vantage/?function=TIME_SERIES_DAILY&symbol=";
    private static String url2 = "&outputsize=compact";

    private static String urlCurrent = "http://128.31.25.3/alpha-vantage/?function=GLOBAL_QUOTE&symbol=";


    /*
    Uses Volley to make GET request with http REST interface
     */
    public static void sendAndRequestResponse(String url, AlphaVantageResponseCallback callback) {
        Log.i(MainActivity.TAG, "Trying to make GET request...");

        //jsonRequest is the GET request, processed by requestQueue
        HttpConnectionLibrary.sendGET(url, response -> {
            try {
                callback.callback(new JSONObject(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
    public interface AlphaVantageResponseCallback {
        void callback(JSONObject response);
    }

    /*
        Helper functions
     */

    /*
        Returns the historical data of 100 days for a particular stock in a List<DataEntry> format (Date, Value).
     */
    public static void getHistoricalStockData(String symbol, HistoricalDataResponseCallback callback){
        String fUrl = url + symbol + url2;

        List<DataEntry> seriesData = new ArrayList<>();

        sendAndRequestResponse(fUrl, response -> {
            try {
                JSONObject fResponse = response.getJSONObject("Time Series (Daily)");
                JSONArray dates = fResponse.names();

                for (int i = dates.length()-1; i >= 0; i--) {

                    String keys = dates.getString(i);
                    JSONObject tResponse = fResponse.getJSONObject(keys);

                    seriesData.add(new ValueDataEntry(keys, tResponse.getDouble("4. close")));

                }

                callback.callback(seriesData);

            }catch(JSONException e){
                Log.d("TESTING","ERROR");
            }

        });
    }

    public interface HistoricalDataResponseCallback {
        void callback(List<DataEntry> response);
    }

        /*
        Returns a DataStock object of the given stock ticker with live information.
     */

    public static void getCurrentStockData(String symbol, CurrentDataResponseCallback callback){

        String fUrl = urlCurrent + symbol;
        StockData data = new StockData();

        sendAndRequestResponse(fUrl, response -> {
            try {
                JSONObject fResponse = response.getJSONObject("Global Quote");
                data.Ticker = symbol;
                data.currentPrice = fResponse.getDouble("05. price");
                data.dailyChange = fResponse.getDouble("09. change");

                callback.callback(data);

            } catch (JSONException e) {
                Log.d("TESTING","Error JSON Exception");
            }
        });
    }


    public interface CurrentDataResponseCallback {
        void callback(StockData response);
    }

    

    /*
        Creates a historical stock data in the given view.
            - symbol: Symbol of the stock to display in the graph
            - days: Number of past days to display in the graph
            - view: AnyChartView in which to display the graph
     */
    public static void makeStockChart(String symbol, Integer days, AnyChartView view){
        AnyChartView anyChartView = view;

        Cartesian cartesian = AnyChart.line();

        AlphaVantage.getHistoricalStockData(symbol, response -> {
            List<DataEntry> filteredResponse = response.subList(response.size()-days,response.size());
            Log.d("TESTING","Data: " + filteredResponse.toString());

            cartesian.label(symbol);
            cartesian.line(filteredResponse);
            anyChartView.setChart(cartesian);

        });
    }



}
