package com.company.vesper.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.company.vesper.DetailedStockFragment;
import com.company.vesper.MainActivity;
import com.company.vesper.R;
import com.company.vesper.lib.Helpers;

import java.util.List;

public class WatchListAdapter extends ArrayAdapter<WatchListItem> {


    public WatchListAdapter(@NonNull Context context, @NonNull List<WatchListItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // We choose a watchlist item
        WatchListItem item = getItem(position);

        if (convertView == null ){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.watch_list_item, parent, false);
        }
        // Bind the views
        TextView watchListRow = convertView.findViewById(R.id.tickerView);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        TextView txtChange = convertView.findViewById(R.id.txtChange);
        // Populate the data into the template view using the data object
        watchListRow.setText(item.Ticker.toString());
        String closingPriceString = "$" + String.valueOf(item.currentPrice);
        txtPrice.setText(closingPriceString);
        String dailyChangeString =  "$" + String.valueOf(Helpers.formatDecimal(item.dailyChange));
        txtChange.setText(dailyChangeString);

        if (item.dailyChange > 0) {
            txtChange.setTextColor(Helpers.getColor(R.color.active_signal));
        } else {
            txtChange.setTextColor(Helpers.getColor(R.color.expired_signal));
        }

        convertView.setOnClickListener(v -> {
            DetailedStockFragment fragment = new DetailedStockFragment(item.Ticker, item.currentPrice, item.dailyChange, item.percentChange);
            MainActivity.instance.setCurrentFragment(fragment);
        });

        return convertView;
    }
}
