package com.company.vesper.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        TextView watchListRow = (TextView) convertView.findViewById(R.id.stockInfo);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        TextView txtChange = (TextView) convertView.findViewById(R.id.txtChange);
        // Populate the data into the template view using the data object
        // TODO: We may have to construct a data object when we have more fields to pass to the listView
        watchListRow.setText(item.Ticker);
        txtPrice.setText("" + item.closingPrice);
        txtChange.setText(Helpers.formatDecimal(item.dailyChange));

        if (item.dailyChange > 0) {
            txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.active_signal));
        } else {
            txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.expired_signal));
        }
        //watchListRow.setText(item.Name);

        convertView.setOnClickListener(v -> {
            DetailedStockFragment fragment = new DetailedStockFragment(item.Ticker);
            MainActivity.instance.setCurrentFragment(fragment);
        });

        return convertView;
    }
}
