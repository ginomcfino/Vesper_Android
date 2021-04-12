package com.company.vesper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.company.vesper.databinding.FragmentHomeBinding;
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
        FirebaseFirestore snapshot_db ;
        CollectionReference snapshot_collection;

        // Construct array of watchlists
        List<WatchListItem> watchlist_array = new ArrayList<>();
        // Initialize custom watchlist adapter
        WatchListAdapter adapter = new WatchListAdapter(getContext(), watchlist_array);

        // Attach the adapter to a ListView
        // TODO: What xml does this go in?
        // TODO: Is converting frame layout to constraintlayout acceptable?
        ListView listView = binding.listViewObject;
//        ListView listView = (ListView) view.findViewById(R.id.listViewObject); // View binding feature not needed
        listView.setAdapter(adapter);

        // Calls and gets a list of the ticket symbols
        State.getDatabase().collection("users").document(State.getUser().getUid()).get().addOnCompleteListener(t -> {
            DocumentSnapshot snapshot = t.getResult();
            // We get a list of ticker symbols
            List<String> tickerSymbols = (List<String>) snapshot.get("watchlist");

            // Loop over every ticker symbol, for each one create a watchListItem and add it to the array
            for (int i = 0; i < tickerSymbols.size(); i++) {
                WatchListItem wItem = new WatchListItem(tickerSymbols.get(i));
                // add it to watchlist_array
                watchlist_array.add(wItem);
                // Now we must add it to adapter
            }
            adapter.notifyDataSetChanged();
            // This tells adapter that we have added items to the list, and so listview needs to create the new ui elements for these items
        });

        // Set an handler to catch which option the user chooses
        return view;
    }
}