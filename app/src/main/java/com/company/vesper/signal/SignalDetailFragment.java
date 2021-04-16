package com.company.vesper.signal;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentSignalDetailBinding;
import com.company.vesper.databinding.SignalMessageBinding;
import com.company.vesper.lib.Helpers;
import com.company.vesper.services.AlphaVantage;
import com.company.vesper.signal.Signal;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SignalDetailFragment extends Fragment {
    private Signal signal;

    public SignalDetailFragment(Signal signal) {
        this.signal = signal;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignalDetailBinding binding = FragmentSignalDetailBinding.inflate(inflater);
        View view = binding.getRoot();

        signal.getGroup().get().addOnCompleteListener(task -> {
            DocumentSnapshot snap = task.getResult();
            binding.txtGroupName.setText(snap.getString("name"));
            binding.txtLeader.setText(State.getName(snap.getString("signaler")));
        });

        binding.layout.addView(Helpers.createSignalMessage(inflater, signal, false), 0);

        AlphaVantage.getCurrentStockData(signal.getTicker(), response -> {
            binding.txtPrice.setText("" + response.currentPrice);
            binding.txtChange.setText(Helpers.formatDecimal(response.dailyChange) + "%");
            if (response.dailyChange > 0) {
                binding.txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.active_signal));
            } else {
                binding.txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.expired_signal));
            }
        });
        return view;
    }

}