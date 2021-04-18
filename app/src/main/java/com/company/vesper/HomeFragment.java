package com.company.vesper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.data.Table;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.company.vesper.databinding.FragmentHomeBinding;
import com.company.vesper.dbModels.GroupInfo;
import com.company.vesper.dbModels.Signal;
import com.company.vesper.lib.Helpers;
import com.company.vesper.services.AlphaVantage;
import com.company.vesper.signal.SignalDisplayFragment;
import com.company.vesper.watchlist.WatchListAdapter;
import com.company.vesper.watchlist.WatchListItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadSignalList();

        // Construct array of watchlists
        List<WatchListItem> watchlist_array = new ArrayList<>();
        // Initialize custom watchlist adapter
        WatchListAdapter adapter = new WatchListAdapter(Objects.requireNonNull(getContext()), watchlist_array);

        // Attach the adapter to a ListView
        ListView listView = binding.listViewObject;
        listView.setAdapter(adapter);

        //Instantiate search bar and get string
        SearchView simpleSearchView = (SearchView) view.findViewById(R.id.searchBar);
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
            AlphaVantage.getCurrentStockData(tickerSymbols.get(i), stockData -> {
                WatchListItem watchListItem = new WatchListItem(stockData.Ticker, stockData.Name, stockData.currentPrice, stockData.dailyChange);
                        watchlist_array.add(watchListItem);
                        adapter.notifyDataSetChanged();
            });
        }
        return view;
    }

    /**
     * Load the live signals onto the home page.
     */
    private void loadSignalList() {
        SignalDisplayFragment liveSignals = new SignalDisplayFragment(getString(R.string.live_signal));

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.topLayout.getId(), liveSignals, null);
        transaction.commit();

        List<DocumentReference> groups = new ArrayList<>();
        for (GroupInfo group : State.getUser().getGroups()) {
            groups.add(group.getRef());
        }

        State.getDatabase().collection("signals").whereIn("group", groups).whereEqualTo("active", true).get().addOnCompleteListener(task -> {
            QuerySnapshot snapshots = task.getResult();
            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                Signal signal = new Signal(doc);
                liveSignals.addView(Helpers.createSignalMessage(getLayoutInflater(), signal, true));
            }
        });
    }

}