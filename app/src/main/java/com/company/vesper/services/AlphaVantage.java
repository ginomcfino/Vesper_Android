package com.company.vesper.services;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.company.vesper.MainActivity;
import com.company.vesper.R;

import org.json.JSONObject;

public class AlphaVantage extends AppCompatActivity {

    private Button toastBtn;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonRequest;
    private String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=TSLA&interval=5min&outputsize=compact&apikey=9IP6K9XMU7Y4JG38";
    //Makes a simple API call to get the intra-day stock prices
    //5 min interval for stock price updates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alpha_vantage);

        toastBtn = (Button) findViewById(R.id.getAV);

        toastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndRequestResponse();
            }
        });
    }

    /*
    Uses Volley to make GET request with http REST interface
     */
    private void sendAndRequestResponse() {
        Log.i(MainActivity.TAG, "Trying to make GET request...");
        requestQueue = Volley.newRequestQueue(this);

        //jsonRequest is the GET request, processed by requestQueue
        jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //response is stored as a JSON object, which can be parsed and utilized with JSONParser
                Log.i(MainActivity.TAG, "JSON Response: \n" + response.toString());

                //currently shows the returned JSON as a toast to show that it is received correctly
                //TODO: Make stock chart functions, connect to visual UI
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(MainActivity.TAG, "!! Encountered Error !!");
                //TODO: Handle ERROR
            }
        });

        requestQueue.add(jsonRequest);
    }
}