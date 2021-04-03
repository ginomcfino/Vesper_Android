package com.company.vesper;

import android.app.Application;
import android.content.Context;

import com.company.vesper.lib.HttpConnectionLibrary;

/**
 * Custom App extends Application, used to handle some app level stuff like single instantiation of contexts.
 */
public class App extends Application {
    private static String TAG = "App";
    private static App _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        HttpConnectionLibrary.init(this);
    }

    public static Context getContext() {
        return _instance.getApplicationContext();
    }
}
