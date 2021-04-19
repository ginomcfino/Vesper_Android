package com.company.vesper.lib;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.company.vesper.R;

import static com.company.vesper.App.getContext;

/**
 * Helper class to keep a hold on the preference file to make easy access.
 */
public class Preferences {
    private static final String TAG = Preferences.class.getName();

    private static SharedPreferences preferences;
    public static void init(Context context) {
        preferences = context.getSharedPreferences("com.company.vesper.preferences", Context.MODE_PRIVATE);

        // Load in color prefs
        Helpers.putColor(R.color.expired_signal, getValue("EXPIRE_COLOR", ContextCompat.getColor(getContext(),R.color.expired_signal)));
        Helpers.putColor(R.color.active_signal, getValue("ACTIVE_COLOR", ContextCompat.getColor(getContext(),R.color.active_signal)));
    }

    public static void putValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static void putValue(String key, Float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static Float getValue(String key, Float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }
    public static void putValue(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getValue(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static void putValue(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getValue(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static void clear() {
        preferences.edit().clear().commit();
    }

    public static boolean contains(String key) {
        return preferences.contains(key);
    }
}
