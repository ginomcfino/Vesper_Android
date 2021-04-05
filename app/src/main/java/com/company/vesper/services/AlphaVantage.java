package com.company.vesper.services;

import android.util.Log;

import com.company.vesper.MainActivity;
import com.company.vesper.lib.HttpConnectionLibrary;

import org.json.JSONObject;

public class AlphaVantage {


    private static String url = "http://128.31.25.3/alpha-vantage/?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&outputsize=compact";
    /*
    Uses Volley to make GET request with http REST interface
     */
    public static void sendAndRequestResponse(AlphaVantageResponseCallback callback) {
        Log.i(MainActivity.TAG, "Trying to make GET request...");

        //jsonRequest is the GET request, processed by requestQueue
        HttpConnectionLibrary.sendGET(url, null, response -> callback.callback(response));
    }

    public interface AlphaVantageResponseCallback {
        void callback(JSONObject response);
    }
}