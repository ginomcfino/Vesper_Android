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
        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        TextView txtChange = (TextView) convertView.findViewById(R.id.txtChange);
        // Populate the data into the template view using the data object
        // TODO: We may have to construct a data object when we have more fields to pass to the listView
        watchListRow.setText(item.Ticker);
        txtPrice.setText("" + item.closingPrice);
        txtChange.setText("" + item.dailyChange);
        //watchListRow.setText(item.Name);

        return convertView;
    }
}
