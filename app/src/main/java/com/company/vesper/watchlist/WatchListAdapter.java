package com.company.vesper.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.vesper.R;

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
        TextView watchListRow = (TextView) convertView.findViewById(R.id.stockInfo);
        // Populate the data into the template view using the data object
<<<<<<< Updated upstream
        // TODO: We may have to construct a data object when we have more fields to pass to the listView
        watchListRow.setText(item.Ticker);
        //watchListRow.setText(item.Name);
=======
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
            DetailedStockFragment fragment = new DetailedStockFragment(item.Ticker,item.currentPrice,item.dailyChange);
            MainActivity.instance.setCurrentFragment(fragment);
        });
>>>>>>> Stashed changes

        return convertView;
    }
}
