package com.company.vesper.lib;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HttpConnectionLibrary {
    private static String TAG = "HttpConnectionLibrary";
    private static RequestQueue requestQueue;

    public static void init(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Sends a GET request using Volley.
     * @param requestURL Target URL of the GET request
     * @param callback callback function when response is returned. Best used in lambda format "(response: String) -> {}"
     */
    public static void sendGET(String requestURL, ResponseListener callback) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestURL, response -> {
        }, error -> {
        }) {
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

        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    /**
     * Send a POST request using Volley. Use when don't need to listen to POST response.
     * @param requestURL Target URL of the POST request
     * @param params HashMap of the params to be encoded in the POST body
     */
    public static void sendPOST(String requestURL, HashMap<String, String> params) {
        sendPOST(requestURL, params, null);
    }

    /**
     * Send a POST request using Volley. Use when need to subscribe to POST response.
     * @param requestURL Target URL of the POST request
     * @param params HashMap of the params to be encoded in the POST body
     * @param callback callback function when response is returned. Best used in lambda format "(response: String) -> {}"
     */
    public static void sendPOST(String requestURL,
                                HashMap<String, String> params, ResponseListener callback) {

        JSONObject jsonBody = new JSONObject();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
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
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
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

    public interface ResponseListener {
        void callback(Response<String> response);
    }

}

