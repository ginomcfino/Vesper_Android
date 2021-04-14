package com.company.vesper.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.company.vesper.App;
import com.company.vesper.MainActivity;
import com.company.vesper.R;
import com.company.vesper.SignalDetailFragment;
import com.company.vesper.databinding.SignalMessageBinding;
import com.company.vesper.signal.Signal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static androidx.core.content.ContextCompat.startActivity;
import static com.company.vesper.App.getContext;

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

    public static String getLongTimestamp(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy.MM.dd\nHH:mm aa", cal).toString();
        return date;
    }


    /**
     * Used to create a new GUI item of the signal message
     * @param inflater the inflater to use to inflate our gui
     * @param signal the signal object of the signal
     * @param withListener whether to attach an onclick listener to this or now.
     * @return the generated view
     */
    public static View createSignalMessage(LayoutInflater inflater, Signal signal, boolean withListener) {
        SignalMessageBinding binding = SignalMessageBinding.inflate(inflater);
        binding.txtTicker.setText(signal.getTicker());
        binding.txtBuy.setText(String.format("%s", signal.getBuy()));
        binding.txtSell.setText("" + signal.getSell());
        binding.txtLoss.setText("" + signal.getLoss());

        if (signal.isActive()) {
            binding.txtLive.setText(getContext().getString(R.string.live_signal));
            binding.txtLive.setTextColor(ContextCompat.getColor(getContext(), R.color.active_signal));
        } else {
            binding.txtLive.setText(getContext().getString(R.string.expired_signal));
            binding.txtLive.setTextColor(ContextCompat.getColor(getContext(), R.color.expired_signal));
        }

        if (withListener) {
            binding.layout.setOnClickListener(t -> {
                SignalDetailFragment fragment = new SignalDetailFragment(signal);
                MainActivity.instance.setCurrentFragment(fragment);
            });
        }

        return binding.getRoot();
    }
}
