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
<<<<<<< Updated upstream
        FirebaseFirestore snapshot_db ;
        CollectionReference snapshot_collection;
=======

        loadSignalList();

        // Find titles views within FragmentView
        TextView companyTitle = (TextView) view.findViewById(R.id.header1);
        TextView txtPrice = (TextView) view.findViewById(R.id.header2);
        TextView txtChange = (TextView) view.findViewById(R.id.header3);

        // Set titles
        companyTitle.setText("Company");
        txtPrice.setText("Price");
        txtChange.setText("Daily Change");
>>>>>>> Stashed changes

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