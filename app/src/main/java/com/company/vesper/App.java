package com.company.vesper;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static String TAG = "App";
    private static App _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
    }

    public static Context getContext() {
        return _instance.getApplicationContext();
    }
}
