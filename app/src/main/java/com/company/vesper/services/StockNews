package com.company.vesper.services;

import android.util.Log;

import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.company.vesper.DetailedStockFragment;
import com.company.vesper.MainActivity;
import com.company.vesper.lib.HttpConnectionLibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockNews {

    public static class NewsItem{

        public String title;
        public String bodyText;
        public String pictureUrl;
        public String sourceName;
        public Boolean sentiment;

        public NewsItem(String title, String bodyText, String pictureUrl, String sourceName, String sentiment){
            this.title = title;
            this.bodyText = bodyText;
            this.pictureUrl = pictureUrl;
            this.sourceName = sourceName;

            if(sentiment.equals("Positive")){
                this.sentiment = true;
            }else{
                this.sentiment = false;
            }

        }
    }

    private static String url1 = "http://128.31.25.3/stock-news/?tickers=";
    private static String url2 = "&items=8";

    /*
        Uses Volley to make GET request with http REST interface
    */
    public static void sendAndRequestResponse(String url, getResponseCallback callback) {
        Log.i("StockNews", "Trying to make GET request...");

        //jsonRequest is the GET request, processed by requestQueue
        HttpConnectionLibrary.sendGET(url, response -> {
            try {
                callback.callback(new JSONObject(response));
                Log.d("TESTING",response.toString());
            } catch (JSONException e) {
                Log.d("StockNews",e.toString());
            }
        });
    }
    public interface getResponseCallback {
        void callback(JSONObject response);
    }

    public static void getNewsData(String ticker, StockNewsResponseCallback callback){

        List<NewsItem> mDataset = new ArrayList<>();

        String fUrl = url1 + ticker + url2;

        //Send HTTP request and obtain JSONObject
        sendAndRequestResponse(fUrl, response -> {
            try{

                JSONArray fResponse = response.getJSONArray("data");

                for (int i = 0; i < fResponse.length(); i++) {

                    JSONObject item = fResponse.getJSONObject(i);

                    String news_url = item.getString("news_url");
                    String image_url = item.getString("image_url");
                    String title = item.getString("title");
                    String text = item.getString("text");
                    String sentiment = item.getString("sentiment");
                    String sourceName = item.getString("source_name");

                    mDataset.add(new NewsItem(title,text,image_url,sourceName,sentiment));
                    //seriesData.add(new ValueDataEntry(keys, tResponse.getDouble("4. close")));



                }


                callback.callback(mDataset);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    public interface StockNewsResponseCallback {
        void callback(List<NewsItem> response);
    }


}
