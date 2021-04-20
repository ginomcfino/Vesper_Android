package com.company.vesper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.company.vesper.State;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.company.vesper.databinding.FragmentModifyWatchlistBinding;
import com.company.vesper.databinding.FragmentUserProfileBinding;
import com.company.vesper.dbModels.GroupInfo;
import com.company.vesper.dbModels.UserInfo;
import com.company.vesper.groups.GroupListAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfile extends Fragment {

    ExpandableListView expandableListView;
    TextView username;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfile newInstance() {
        UserProfile fragment = new UserProfile();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_profile, container, false);
        ;
        FragmentUserProfileBinding binding = FragmentUserProfileBinding.inflate(inflater);
        View view = binding.getRoot();

        UserInfo user = State.getUser();

        username = view.findViewById(R.id.username);
        username.setText(user.getName());

        List<GroupInfo> groups = new ArrayList<>();
        GroupListAdapter adapter = new GroupListAdapter(getContext(), groups);

        groups.addAll(State.getUser().getGroups());

        adapter.notifyDataSetChanged();

        binding.joinedGroups.setAdapter(adapter);

        return view;
    }
}