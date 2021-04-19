package com.company.vesper.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.State;
import com.company.vesper.databinding.FragmentExploreGroupsBinding;
import com.company.vesper.dbModels.GroupInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ExploreGroups extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentExploreGroupsBinding binding = FragmentExploreGroupsBinding.inflate(inflater);
        View view = binding.getRoot();

        List<GroupInfo> groups = new ArrayList<>();
        GroupListAdapter adapter = new GroupListAdapter(getContext(), groups);


        State.getDatabase().collection("groups").get().addOnCompleteListener(task -> {
            QuerySnapshot qSnap = task.getResult();

            for(DocumentSnapshot dSnap: qSnap.getDocuments()) {
                groups.add(new GroupInfo(dSnap));
            }

            adapter.notifyDataSetChanged();
        });

        binding.listGroups.setAdapter(adapter);



        return view;
    }
}