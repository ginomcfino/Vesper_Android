package com.company.vesper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.company.vesper.services.AlphaVantage;
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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Initialize watchlist globally in this file
    List<String> watchlist = new ArrayList<String>() ;
    // Declare listView
    ListView myStockList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Instantiate search bar and get string
        SearchView simpleSearchView = (SearchView) view.findViewById(R.id.searchBar);
        // perform set on query text listener
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Maybe show options from the list of possible tickers
                // Needs to have access to some AlphaVantage API that returns a list of 
                return false;
            }

            @Override
            public boolean onQueryTextChange(String Ticker) {

                try {
                    // Try to add the ticker to the watchlist
                    State.getUser().addToWatchlist(Ticker);
                }
                catch(Exception e) {
                    System.out.println("Not able to add to watchlist, wait and try again");
                } finally{
                    // May be needed later
                }
                return false;
            }
        });

          // Find titles views within FragmentView
        TextView companyTitle = (TextView) view.findViewById(R.id.header1);
        TextView txtPrice = (TextView) view.findViewById(R.id.header2);
        TextView txtChange = (TextView) view.findViewById(R.id.header3);

        // Set titles
        companyTitle.setText("Company");
        txtPrice.setText("Price");
        txtChange.setText("Daily Change");

        // Construct array of watchlists
        List<WatchListItem> watchlist_array = new ArrayList<>();
        // Initialize custom watchlist adapter
        WatchListAdapter adapter = new WatchListAdapter(Objects.requireNonNull(getContext()), watchlist_array);

        // Attach the adapter to a ListView
        ListView listView = binding.listViewObject;
        listView.setAdapter(adapter);


//        // TODO:We need to create a header

        // THIS IS A PLACEHOLDER -  NOT A FINAL SOLUTION
//        TextView textView = new TextView(this.getActivity());
//        textView.setText("Company" + "Price" + "Daily Change");
//        textView.setTextSize(24);
//        listView.addHeaderView(textView);

        List<String> tickerSymbols = State.getUser().getWatchlist();

        // Loop over every ticker symbol, for each one create a watchListItem and add it to the array
        for (int i = 0; i < tickerSymbols.size(); i++) {
            // add it to watchlist_array
            AlphaVantage.getCurrentStockData(tickerSymbols.get(i), stockData -> {
                WatchListItem watchListItem = new WatchListItem(stockData.Ticker, stockData.Name, stockData.currentPrice, stockData.dailyChange);
                        watchlist_array.add(watchListItem);
                        adapter.notifyDataSetChanged();
            });
            // Now we must add it to adapter
        }
        // This tells adapter that we have added items to the list, and so listview needs to create the new ui elements for these items

        // Set an handler to catch which option the user chooses
        return view;
    }

}