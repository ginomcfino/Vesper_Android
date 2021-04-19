package com.company.vesper.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.vesper.MainActivity;
import com.company.vesper.databinding.GroupItemBinding;
import com.company.vesper.dbModels.GroupInfo;

import java.util.List;

public class GroupListAdapter extends ArrayAdapter<GroupInfo> {

    public GroupListAdapter(@NonNull Context context, @NonNull List<GroupInfo> groups) {
        super(context, 0, groups);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupInfo group = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        GroupItemBinding binding = GroupItemBinding.inflate(inflater);
        binding.txtGroupName.setText(group.getName());
        String description = "";
        // TODO REMEMBER to enable this VVV, DUMMY VALUE RIGHT NOW
//        binding.txtDescription.setText(description);

        binding.layout.setOnClickListener(view -> {
            GroupsDetail fragment = new GroupsDetail(group);
            MainActivity.instance.setCurrentFragment(fragment);

        });

        return binding.getRoot();
    }
}
