package com.company.vesper.services;

import com.company.vesper.lib.HttpConnectionLibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YahooNewsActivity {

    public static void getNews(YahooNewsResponseCallback callback) {
        HttpConnectionLibrary.sendGET("http://128.31.25.3/yahoo-news/?region=US&snippetCount=28", response -> {
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                // 7. decompose the JSON object to find the url to news

                JSONArray stream = jsonObject.getJSONObject("data").getJSONObject("main").getJSONArray("stream");
                String url = "";
                for (int i = 0; i < stream.length(); i++) {
                    if (!((JSONObject)(stream.get(i))).getJSONObject("content").isNull("clickThroughUrl")) {
                        url = ((JSONObject)(stream.get(i))).getJSONObject("content").getJSONObject("clickThroughUrl").getString("url");
                    }
                }
                callback.callback(url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public interface YahooNewsResponseCallback {
        void callback(String url);
    }
}