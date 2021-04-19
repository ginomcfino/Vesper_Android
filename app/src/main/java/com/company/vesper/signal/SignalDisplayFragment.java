package com.company.vesper.signal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.databinding.FragmentSignalDisplayBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SignalDisplayFragment extends Fragment {

    private FragmentSignalDisplayBinding binding;
    private String title;

    public SignalDisplayFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignalDisplayBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.txtTitle.setText(title);
        return view;
    }

    public void addView(View v) {
        binding.layout.addView(v);
    }
}