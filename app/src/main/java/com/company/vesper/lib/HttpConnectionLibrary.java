package com.company.vesper.lib;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Library for HTTP Restful Connections. Simplifies using Volley
 */
public class HttpConnectionLibrary {
    private static final String TAG = "HttpConnectionLibrary";
    private static RequestQueue requestQueue;

    public static void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
//        requestQueue.start();
    }

    /**
     * Sends a GET request using Volley. Response type is a String
     *
     * @param requestURL Target URL of the GET request
     * @param callback   callback function when response is returned. Best used in lambda format "(response: String) -> {}"
     */
    public static void sendGET(String requestURL, getStringListener callback) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, response -> {
            if (callback != null) {
                callback.callback(response);
            }
        }, error -> {
        });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    /**
     * Sends a GET request using Volley. Response type is a JSONObject
     *
     * @param requestURL Target URL of the GET request
     * @param callback   callback function when response is returned. Best used in lambda format "(response: JSONObject) -> {}"
     */
    public static void sendGET(String requestURL, JSONObject jsonRequest, getJsonListener callback) {
        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, requestURL, jsonRequest, response -> {
            if (callback != null) {
                callback.callback(response);
            }
        }, error -> {
        });

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }




    /**
     * Send a POST request using Volley. Use when don't need to listen to POST response.
     *
     * @param requestURL Target URL of the POST request
     * @param params     HashMap of the params to be encoded in the POST body
     */
    public static void sendPOST(String requestURL, HashMap<String, Object> params) {
        sendPOST(requestURL, params, null);
    }

    /**
     * Send a POST request using Volley. Use when need to subscribe to POST response.
     *
     * @param requestURL Target URL of the POST request
     * @param params     HashMap of the params to be encoded in the POST body
     * @param callback   callback function when response is returned. Best used in lambda format "(response: String) -> {}"
     */
    public static void sendPOST(String requestURL,
                                HashMap<String, Object> params, postListener callback) {

        JSONObject jsonBody = new JSONObject();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                jsonBody.put(key, value);
            } catch (JSONException e) {

            }
        }

        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURL, response -> {
        }, error -> {
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (callback == null) {
                    return null;
                }

                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }

                callback.callback(Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response)));
                return null;
            }
        };

        requestQueue.add(stringRequest);
    }

    public interface postListener {
        void callback(Response<String> response);
    }

    public interface getStringListener {
        void callback(String response);
    }

    public interface getJsonListener {
        void callback(JSONObject response);
    }

}

