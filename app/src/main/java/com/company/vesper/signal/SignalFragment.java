package com.company.vesper.signal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentSignalBinding;
import com.company.vesper.dbModels.GroupInfo;
import com.company.vesper.dbModels.Signal;
import com.company.vesper.lib.Helpers;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Core signal fragment that lists all active and expired signals of groups that a user is in.
 */
public class SignalFragment extends Fragment {

    private FragmentSignalBinding binding;

    private SignalDisplayFragment activeSignalList;
    private SignalDisplayFragment expiredSignalList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignalBinding.inflate(inflater, container, false);

        List<DocumentReference> groups = new ArrayList<>();
        for (GroupInfo group : State.getUser().getGroups()) {
            groups.add(group.getRef());
        }

        activeSignalList = new SignalDisplayFragment(getString(R.string.live_signal));
        expiredSignalList = new SignalDisplayFragment(getString(R.string.expired_signal));

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activeSignals, activeSignalList, null);
        transaction.replace(R.id.expiredSignals, expiredSignalList, null);
        transaction.commit();

        if (groups.size() == 0) {
            return binding.getRoot();
        }

        State.getDatabase().collection("signals").whereIn("group", groups).get().addOnCompleteListener(task -> {
            QuerySnapshot snapshots = task.getResult();
            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                Signal signal = new Signal(doc);

                if (signal.isActive()) {
                    activeSignalList.addView(Helpers.createSignalMessage(inflater, signal, true));
                } else {
                    expiredSignalList.addView(Helpers.createSignalMessage(inflater, signal, true));
                }
            }
        });

        return binding.getRoot();
    }

}