package com.company.vesper;

import android.app.Application;
import android.content.Context;

import com.company.vesper.lib.HttpConnectionLibrary;
import com.company.vesper.lib.Preferences;
import com.company.vesper.services.FCMServiceHandler;

/**
 * Custom App extends Application, used to handle some app level stuff like single instantiation of contexts.
 */
public class App extends Application {
    private static final String TAG = "App";
    private static App _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        HttpConnectionLibrary.init(this);

        FCMServiceHandler.loadFCMToken();
        State.init();
        Preferences.init(getContext());
    }

    public static Context getContext() {
        return _instance.getApplicationContext();
    }
}
