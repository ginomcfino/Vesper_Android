package com.company.vesper.watchlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentModifyWatchlistBinding;
import com.company.vesper.dbModels.UserInfo;
import com.company.vesper.services.AlphaVantage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ModifyWatchlist extends Fragment {



    Button addButton;
    Button removeButton;
    int flag;
    int nullValue = 0;
    int deleteFlag = -1;
    int addFlag = 1;

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
        WatchListAdapter adapter = new WatchListAdapter(Objects.requireNonNull(getContext()), watchlist_array);
        // Initialize flag as 0
        flag = nullValue;
        // Instatiate both buttons
        addButton = view.findViewById(R.id.aButton);
        removeButton = view.findViewById(R.id.rButton);
        // If clicked set the submit button to add
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = addFlag;
                // Toast you are adding a stock
                Toast.makeText(getActivity(), "Ready to ADD", Toast.LENGTH_SHORT).show();
            }

        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = deleteFlag;
                Toast.makeText(getActivity(), "Ready to DELETE", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize custom watchlist adapter
        // TODO: ACCESS THE watchlist_array TO delete from watchlist
        //WatchListAdapter adapter = new WatchListAdapter(Objects.requireNonNull(getContext()), watchlist_array);
        // Attach the adapter to a ListView
        ListView listView = binding.watchlist;
        listView.setAdapter(adapter);
        //Instantiate search bar and get string
        SearchView simpleSearchView = view.findViewById(R.id.searchBar);
        simpleSearchView.setSubmitButtonEnabled(true);
        // Tell user what to do
        simpleSearchView.setQueryHint("Select Add or Remove");
        // perform set on query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String Ticker) {
                try {
                    // Check the flag
                    if (flag == nullValue){
                        Toast.makeText(getActivity(), "Select Add or Remove", Toast.LENGTH_SHORT).show();
                    } else if (flag == deleteFlag){
                        Toast.makeText(getActivity(), "Deleted Ticker", Toast.LENGTH_SHORT).show();
                        // Check that Ticker is in proper format
                        if (checkTicker(Ticker)) {
                            //deleteStock(Ticker, adapter);
                        }else{
                            Toast.makeText(getActivity(), "Enter a Ticker", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "Capital Letters Only ", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        // Try to add the ticker to the watchlist
                        // Put this in a method.
                        Toast.makeText(getActivity(), "Added Ticker", Toast.LENGTH_SHORT).show();
                        // Pass the ticker to add and the adapter
                        //processIntoWatchlist(Ticker, adapter);

                        // add it to watchlist_array
                        WatchListItem watchListItem = new WatchListItem(Ticker);
                        watchlist_array.add(watchListItem);
                        adapter.notifyDataSetChanged();
                        // Now we wait for the real data
                        AlphaVantage.getCurrentStockData(Ticker, stockData -> {
                            watchListItem.Name = stockData.Name;
                            watchListItem.closingPrice = stockData.currentPrice;
                            watchListItem.dailyChange = stockData.dailyChange;
                            adapter.notifyDataSetChanged();
                        });

                        UserInfo.addToWatchlist(Ticker);
                        // Check that its all caps lock
                    }

                }
                catch(Exception e) {
                    System.out.println("Not able to add to watchlist, wait and try again");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String Ticker) {
//                if (flag == nullValue){
//                    // Send toast telling them to "Select add to add a stock "
//                    // Send toast telling them to "Click add to add a stock"
//                } else if (flag == deleteFlag ){
//
//
//                } else {
//                    // Check all caps
//                    // If stock does not appear in menu in 1 minute, contact support
//                }
                return false;
            }
        });

        List<String> tickerSymbols = State.getUser().getWatchlist();

        // Loop over every ticker symbol, for each one create a watchListItem and add it to the array
        for (int i = 0; i < tickerSymbols.size(); i++) {
            //processIntoWatchlist(tickerSymbols.get(i), adapter);

            // add it to watchlist_array
            WatchListItem watchListItem = new WatchListItem(tickerSymbols.get(i));
            watchlist_array.add(watchListItem);
            adapter.notifyDataSetChanged();
            // Now we wait for the real data
            AlphaVantage.getCurrentStockData(tickerSymbols.get(i), stockData -> {
                watchListItem.Name = stockData.Name;
                watchListItem.closingPrice = stockData.currentPrice;
                watchListItem.dailyChange = stockData.dailyChange;
                adapter.notifyDataSetChanged();
            });
        }
        return view;
    }

//    private void processIntoWatchlist(String Ticker,WatchListAdapter adapter){
//        // add it to watchlist_array
//        WatchListItem watchListItem = new WatchListItem(Ticker);
//        watchlist_array.add(watchListItem);
//        adapter.notifyDataSetChanged();
//        // Now we wait for the real data
//        AlphaVantage.getCurrentStockData(Ticker, stockData -> {
//            watchListItem.Name = stockData.Name;
//            watchListItem.closingPrice = stockData.currentPrice;
//            watchListItem.dailyChange = stockData.dailyChange;
//            adapter.notifyDataSetChanged();
//        });
//    }
    // TODO: figuret this oit
//    private void deleteStock(String Ticker,WatchListAdapter adapter) {
//        watchlist_array.remove(Ticker);
//        adapter.notifyDataSetChanged();
//    }

    private static boolean checkTicker(String Ticker) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        for(int i=0;i < Ticker.length();i++) {
            ch = Ticker.charAt(i);
            if( Character.isDigit(ch)) {
                numberFlag = true;
            }
           else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }else if (!(Character.isUpperCase(ch))) {
                lowerCaseFlag = true;
            }
            if(numberFlag || lowerCaseFlag || capitalFlag) {
                return false;
            }
        }
        return true;
    }

}