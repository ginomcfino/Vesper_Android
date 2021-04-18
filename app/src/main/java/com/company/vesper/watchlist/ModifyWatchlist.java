package com.company.vesper.watchlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentModifyWatchlistBinding;
import com.company.vesper.services.AlphaVantage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ModifyWatchlist extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentModifyWatchlistBinding binding = FragmentModifyWatchlistBinding.inflate(inflater);
        View view = binding.getRoot();


        // Construct array of watchlists
        List<WatchListItem> watchlist_array = new ArrayList<>();
        // Initialize custom watchlist adapter
        WatchListAdapter adapter = new WatchListAdapter(Objects.requireNonNull(getContext()), watchlist_array);

        // Attach the adapter to a ListView
        ListView listView = binding.watchlist;
        listView.setAdapter(adapter);


        //Instantiate search bar and get string
        SearchView simpleSearchView = view.findViewById(R.id.searchBar);
        // perform set on query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String Ticker) {
                try {
                    // Try to add the ticker to the watchlist
                    State.getUser().addToWatchlist(Ticker);
                    adapter.notifyDataSetChanged();
                }
                catch(Exception e) {
                    System.out.println("Not able to add to watchlist, wait and try again");
                } finally{
                    // May be needed later
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String Ticker) {
                // Maybe show options from the list of possible tickers
                // Needs to have access to some AlphaVantage API that returns a list of
                return false;
            }
        });

        List<String> tickerSymbols = State.getUser().getWatchlist();

        // Loop over every ticker symbol, for each one create a watchListItem and add it to the array
        for (int i = 0; i < tickerSymbols.size(); i++) {
            // add it to watchlist_array
            WatchListItem watchListItem = new WatchListItem(tickerSymbols.get(i), "", 0, 0);
            watchlist_array.add(watchListItem);
            adapter.notifyDataSetChanged();
            AlphaVantage.getCurrentStockData(tickerSymbols.get(i), stockData -> {
                watchListItem.Name = stockData.Name;
                watchListItem.closingPrice = stockData.currentPrice;
                watchListItem.dailyChange = stockData.dailyChange;
                adapter.notifyDataSetChanged();
            });
        }

        return view;
    }
}