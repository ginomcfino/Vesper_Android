package com.company.vesper.lib;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class to keep a hold on the preference file to make easy access.
 */
public class Preferences {
    private static String TAG = Preferences.class.getName();

    private static SharedPreferences preferences;
    public static void init(Context context) {
        preferences = context.getSharedPreferences("com.company.vesper.preferences", Context.MODE_PRIVATE);
    }

    public static void putValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getValue(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static boolean contains(String key) {
        return preferences.contains(key);
    }
}
