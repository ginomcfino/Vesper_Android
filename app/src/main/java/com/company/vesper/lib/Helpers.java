package com.company.vesper.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.company.vesper.LoginActivity;
import com.company.vesper.MainActivity;

import static androidx.core.content.ContextCompat.startActivity;

public class Helpers {
    private static String TAG = Helpers.class.getName();


    /**
     * Helper function to switch to an activity with a delay.
     * @param context The current context that calls this function
     * @param DELAY Delay until switch. Useful if some feedback or some loading is needed first.
     * @param activityClass The class of the activity to switch to.
     */
    public static void switchToActivity(Context context, int DELAY, Class activityClass) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(context, activityClass);
            startActivity(context, intent, null);
        }, DELAY);
    }

}
