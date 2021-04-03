package com.company.vesper.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.company.vesper.R;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class YahooNewsActivity extends AppCompatActivity {

    Button btnPull;

    String url;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yahoo_news);

        // 1. setup the views, button view to pull the news, and webview to display
        btnPull = findViewById(R.id.btnPull);
        webView =  findViewById(R.id.webView);

        // 2. setup the onclick listener:
        //    when the button is clicked, execute the asynctask in background to pull the news
        btnPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYahooFinance yahooFinance = new getYahooFinance();
                yahooFinance.execute();

                webView.loadUrl(url);
            }
        });
    }

    // 3. the AsyncTask that makes the API call
    private class getYahooFinance extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // 4. setup the okhttp client to make api call
            OkHttpClient client = new OkHttpClient();
            // 5. make the api call
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "Pass in the value of uuids field returned right in this endpoint to load the next page, or leave empty to load first page");
            Request request = new Request.Builder()
                    .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/news/v2/list?region=US&snippetCount=28")
                    .post(body)
                    .addHeader("content-type", "text/plain")
                    .addHeader("x-rapidapi-key", "f55858111fmshee3fce031d25022p19647cjsn90b05365ac87")
                    .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                    .build();

            try {
                // 6. get the request, and convert it to JSON object for future decomposition
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject Jobject = new JSONObject(jsonData);

                // 7. decompose the JSON object to find the url to news
                url = Jobject.getJSONObject("data")
                        .getJSONObject("main")
                        .getJSONArray("stream")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONObject("clickThroughUrl")
                        .getString("url");

                // note: the commented code below is preparation to generate the news list,
                //       it is not used in this API research, please ignore.
//                JSONArray news = Jobject.getJSONObject("data")
//                                        .getJSONObject("main")
//                                        .getJSONArray("stream");

//                Log.i("aydry",url);        // for test
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }
}