package com.company.vesper.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.company.vesper.MainActivity;
import com.company.vesper.R;
import com.company.vesper.signal.SignalDetailFragment;
import com.company.vesper.databinding.SignalMessageBinding;
import com.company.vesper.dbModels.Signal;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;
import static com.company.vesper.App.getContext;
import static com.company.vesper.databinding.SignalMessageBinding.inflate;

public class Helpers {
    private static final String TAG = Helpers.class.getName();
    private static final Map<Integer, Integer> Colors = new HashMap<>();

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
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(context, intent, null);
        }, DELAY);
    }

    /**
     * Convert the time into timestamp with yyyy.MM.dd\nHH:mm aa
     * @param time
     * @return
     */
    public static String getLongTimestamp(long time) {
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy.MM.dd\nHH:mm aa", cal).toString();
        return date;
    }

    /**
     * Convert the time into timestamp with yyyy.MM.dd
     * @param time
     * @return
     */
    public static String getShortTimestamp(long time) {
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy.MM.dd", cal).toString();
        return date;
    }

    public static String formatDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return df.format(value);
    }


    /**
     * Used to create a new GUI item of the signal message
     * @param inflater the inflater to use to inflate our gui
     * @param signal the signal object of the signal
     * @param withListener whether to attach an onclick listener to this or now.
     * @return the generated view
     */
    public static View createSignalMessage(LayoutInflater inflater, Signal signal, boolean withListener) {
        SignalMessageBinding binding = inflate(inflater);
        binding.txtTicker.setText(signal.getTicker());
        binding.txtBuy.setText(String.format("%s", signal.getBuy()));
        binding.txtSell.setText("" + signal.getSell());
        binding.txtLoss.setText("" + signal.getLoss());

        if (signal.isActive()) {
            binding.txtLive.setText(getContext().getString(R.string.live_signal));
            binding.txtLive.setTextColor(getColor(R.color.active_signal));
        } else {
            binding.txtLive.setText(getContext().getString(R.string.expired_signal));
            binding.txtLive.setTextColor(getColor(R.color.expired_signal));
        }

        if (withListener) {
            binding.layout.setOnClickListener(t -> {
                SignalDetailFragment fragment = new SignalDetailFragment(signal);
                MainActivity.instance.setCurrentFragment(fragment);
            });
        }

        return binding.getRoot();
    }

    /**
     * Provides a static access to a dict of colors
     * @param id The id of the color requested
     * @return The color code of the corresponding ID
     */
    public static int getColor(int id) {
        if (!Colors.containsKey(id)) {
            Colors.put(id, ContextCompat.getColor(getContext(), id));
        }

        return Colors.get(id);
    }

    /**
     * Provides a static access to a dict of color
     * @param id The id of the color being stored
     * @param val The color code of the color being stored
     */
    public static void putColor(int id, int val) {
        Colors.put(id, val);
    }

    /**
     * Helper method to convert color code in int to String form
      * @param code int form of color code
     * @return Formatted string form of the color
     */
    public static String formatColor(int code) {
        return String.format("#%06X", 0xFFFFFF & code);
    }
}
