package com.company.vesper.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentGroupsDetailBinding;
import com.company.vesper.dbModels.GroupInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GroupsDetail extends Fragment {

    private final GroupInfo group;


    public GroupsDetail(GroupInfo group) {
        this.group = group;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentGroupsDetailBinding binding = FragmentGroupsDetailBinding.inflate(inflater);

        binding.txtGroupName.setText(group.getName());
        State.getDatabase().collection("users")
                .document(group.getSignaler())
                .get().addOnCompleteListener(task -> {
                    binding.SignalerName.setText(task.getResult().getString("displayName"));
                });
        binding.rating.setText(String.valueOf(group.getUpvote_count()));
        binding.numMembers.setText(String.valueOf(group.getNumMembers()));

        binding.excellentCount.setText(String.valueOf(group.getExcellent_signals()));
        binding.goodCount.setText(String.valueOf(group.getGood_signals()));


        State.getDatabase().collection("signals")
                .whereIn("group", Arrays.asList(group.getRef()))
                .get()
                .addOnCompleteListener(task -> {
                    QuerySnapshot qSnap = task.getResult();
                    int signalCount = qSnap.getDocuments().size();
                    binding.signalsCount.setText(String.valueOf(signalCount));
                });




        if (State.getUser().getGroups().contains(group.))
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}