package com.company.vesper.services;

import android.content.Context;
import android.util.Log;

import com.company.vesper.MainActivity;
import com.company.vesper.lib.HttpConnectionLibrary;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AlphaVantage {


    private static String url = "http://128.31.25.3/alpha-vantage/?function=TIME_SERIES_DAILY&symbol=";
    private static String url2 = "&outputsize=compact";

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
    Returns the historical data available for a particular stock in a List<Double> format, starting with the most recent date.
     */
    public static void getHistoricalStockData(String symbol, HistoricalDataResponseCallback callback){
        String fUrl = url + symbol + url2;
        List<Double> values = new ArrayList<Double>();

        sendAndRequestResponse(fUrl, response -> {
            try {
                JSONObject fResponse = response.getJSONObject("Time Series (Daily)");
                JSONArray dates = fResponse.names();

                for (int i = 0; i < dates.length(); i++) {

                    String keys = dates.getString(i);
                    JSONObject tResponse = fResponse.getJSONObject(keys);
                    double val = tResponse.getDouble("4. close");
                    //Log.d("TESTING",Double.toString(val));
                    values.add(val);
                }

                callback.callback(values);
            }catch(JSONException e){
                Log.d("TESTING","ERROR");
            }

        });
    }

    public interface HistoricalDataResponseCallback {
        void callback(List<Double> response);
    }



}