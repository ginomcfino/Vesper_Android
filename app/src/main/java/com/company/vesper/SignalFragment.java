package com.company.vesper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.company.vesper.databinding.FragmentSignalBinding;
import com.company.vesper.databinding.SignalMessageBinding;
import com.company.vesper.lib.Helpers;
import com.company.vesper.signal.Signal;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
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
        // Inflate the layout for this fragment

        List<DocumentReference> groups = new ArrayList<>();
        for (State.GroupInfo group : State.getUser().getGroups()) {
            groups.add(group.getRef());
        }

        activeSignalList = new SignalDisplayFragment(getString(R.string.live_signal));
        expiredSignalList = new SignalDisplayFragment(getString(R.string.expired_signal));

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activeSignals, activeSignalList, null);
        transaction.replace(R.id.expiredSignals, expiredSignalList, null);

        transaction.commit();

        State.getDatabase().collection("signals").whereIn("group", groups).get().addOnCompleteListener(task -> {
            QuerySnapshot snapshots = task.getResult();
            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                Signal signal = new Signal(
                        doc.getString("ticker"),
                        doc.getDouble("buy"),
                        doc.getDouble("sell"),
                        doc.getDouble("loss"),
                        doc.getBoolean("active"),
                        doc.getDocumentReference("group"));

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