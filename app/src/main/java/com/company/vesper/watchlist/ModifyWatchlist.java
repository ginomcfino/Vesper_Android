package com.company.vesper.watchlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

    int test = 0;

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
        // Attach the adapter to a ListView
        ListView listView = binding.watchlist;
        listView.setAdapter(adapter);

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
                    if (flag == nullValue) {
                        Toast.makeText(getActivity(), "Select Add or Remove", Toast.LENGTH_SHORT).show();
                    } else if (flag == deleteFlag) {

                        for (WatchListItem currentItem : watchlist_array) {
                            if (currentItem.Ticker.equals(Ticker)) {
                                // Remove from our watchlist
                                test = 1;
                                watchlist_array.remove(currentItem);
                                adapter.notifyDataSetChanged();
                                // Remove for  DB
                                UserInfo.removeFromWatchlist(Ticker);
                            }
                        }
                    } else {
                        // add it to watchlist_array
                        WatchListItem watchListItem = new WatchListItem(Ticker);
                        // Now we wait for the real data
                        AlphaVantage.getCurrentStockData(Ticker, stockData -> {
                            if (stockData.currentPrice < 0) {
                                Toast.makeText(getActivity(), "Not a valid ticker", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.added_ticker_toast), Toast.LENGTH_SHORT).show();
                                watchlist_array.add(watchListItem);
                                adapter.notifyDataSetChanged();
                                watchListItem.Name = stockData.Name;
                                watchListItem.currentPrice = stockData.currentPrice;
                                watchListItem.dailyChange = stockData.dailyChange;
                                UserInfo.addToWatchlist(Ticker);
                            }
                        });

                    }

                } catch (Exception e) {
                    System.out.println("Not able to add to watchlist, wait and try again");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
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
                watchListItem.currentPrice = stockData.currentPrice;
                watchListItem.dailyChange = stockData.dailyChange;
                watchListItem.percentChange = stockData.percentChange;
                adapter.notifyDataSetChanged();
            });
        }
        return view;
    }

//    private WatchListItem findWatchlistItem(String ticker, List<WatchListItem>  watchlist_array) {
//
//        for (WatchListItem currentItem : watchlist_array) {
//            if (currentItem.Ticker == ticker) {
//                return currentItem;
//            }
//        }
//        return 0;
//    }


//    private static boolean checkTicker(String Ticker) {
//        char ch;
//        boolean capitalFlag = false;
//        boolean lowerCaseFlag = false;
//        boolean numberFlag = false;
//        for(int i=0;i < Ticker.length();i++) {
//            ch = Ticker.charAt(i);
//            if( Character.isDigit(ch)) {
//                numberFlag = true;
//            }
//           else if (Character.isLowerCase(ch)) {
//                lowerCaseFlag = true;
//            }else if (!(Character.isUpperCase(ch))) {
//                lowerCaseFlag = true;
//            }
//            if(numberFlag || lowerCaseFlag || capitalFlag) {
//                return false;
//            }
//        }
//        return true;
//    }

}