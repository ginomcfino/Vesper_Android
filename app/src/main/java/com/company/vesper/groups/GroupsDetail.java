package com.company.vesper.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.R;
import com.company.vesper.databinding.FragmentGroupsDetailBinding;
import com.company.vesper.dbModels.GroupInfo;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GroupsDetail extends Fragment {

    private GroupInfo group;


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
        binding.SignalerName.setText(group.getSignaler());
        binding.rating.setText(String.valueOf(group.getUpvote_count()));
        binding.numMembers.setText(String.valueOf(group.getNumMembers()));

        binding.excellentCount.setText(String.valueOf(group.getExcellent_signals()));
        binding.goodCount.setText(String.valueOf(group.getGood_signals()));


        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}