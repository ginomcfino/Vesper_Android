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

        FirebaseFirestore snapshot_db ;
        CollectionReference snapshot_collection;

        // Inflate the layout for this fragment
        // State ?
        // getDatabase() : entry point for all databases
        // .collection : used for specifying collection path
        //
        // get : Reads the document referred to by this

        // CHECK : Successfully getting Uid
        //String userId = (String) State.getUser().getUid() ;
        //QuerySnapshot snapshot = State.getDatabase().collection("user").whereEqualTo("email", "ericjyc@bu.edu").get().getResult();

        // CHECK : Seems to work fine, does not cause runtime crash
        // CHECK: No runtime crash yet ()
//        Task<DocumentSnapshot> snapshot_document = snapshot_collection.document(userId).get();
//        // THE LAST .getResult() is the problem
//        DocumentSnapshot get_object = snapshot_document.getResult();

        // Put the data into a local data structure
        //watchlist = (List<String>) snapshot.get("watchlist");

        // NEXT GOAL : Create listview according to diagram 10 in the UI

        State.getDatabase().collection("users").document(State.getUser().getUid()).get().addOnCompleteListener(t -> {
            DocumentSnapshot snapshot = t.getResult();
            List<String> watchlist = (List<String>) snapshot.get("watchlist");
        });
        //Log.i(watchlist);
        // WATCHLIST HAS TICKERS
        // LETS MAKE SOME NICE LISTVIEWS



        //Build array adapter object to with listview options
       # ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                #android.R.layout.z, watchlist);

        myStockList = (ListView) findViewById(R.id.);
        // Build the listview with the options in the array
        myStockList.setAdapter(adapter);
        // Set an handler to catch which option the user chooses in case we want it to do something


//        myStockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                choice = (String) myStockList.getItemAtPosition(position);
//
//                if( choice == "Easy" ){
//                    MODE = 1;
//                } else if(choice == "Medium" ) {
//                    MODE = 2;
//                } else if (choice == "Hard"){
//                    MODE = 3;
//                }
//            }
//        });
        return binding.getRoot();
    }
}