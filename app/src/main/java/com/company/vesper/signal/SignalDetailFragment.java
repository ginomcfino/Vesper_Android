package com.company.vesper.signal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.FragmentSignalDetailBinding;
import com.company.vesper.dbModels.Signal;
import com.company.vesper.lib.Helpers;
import com.company.vesper.services.AlphaVantage;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Detailed page of a signal
 */
public class SignalDetailFragment extends Fragment {
    private Signal signal;

    private FragmentSignalDetailBinding binding;

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
        binding = FragmentSignalDetailBinding.inflate(inflater);
        View view = binding.getRoot();

        signal.getGroup().get().addOnCompleteListener(task -> {
            DocumentSnapshot snap = task.getResult();
            binding.txtGroupName.setText(snap.getString("name"));
            binding.txtLeader.setText(State.getName(snap.getString("signaler")));

            // We are the leader of this group
            if (snap.getString("signaler").equals(State.getUser().getUid())) {
                binding.btnAction.setText(getString(R.string.close_signal));
                binding.btnAction.setOnClickListener(v -> closeSignal());
                binding.btnAction.setVisibility(View.VISIBLE);
            } else if (!signal.isActive()) {
                if (!signal.userUpvoted(State.getUser().getUid())) {
                    binding.btnAction.setText(getString(R.string.signal_upvote));
                    binding.btnAction.setOnClickListener(v -> upvote());
                } else {
                    binding.btnAction.setText(getString(R.string.remove_signal_upvote));
                    binding.btnAction.setOnClickListener(v -> remove_upvote());
                }
                binding.btnAction.setVisibility(View.VISIBLE);
            }
        });

        binding.txtDescription.setText(signal.getDescription());
        binding.layout.addView(Helpers.createSignalMessage(inflater, signal, false), 0);

        AlphaVantage.getCurrentStockData(signal.getTicker(), response -> {
            binding.txtPrice.setText("" + response.currentPrice);
            binding.txtChange.setText(Helpers.formatDecimal(response.dailyChange) + "%");

            if (response.dailyChange > 0) {
                binding.txtChange.setTextColor(Helpers.getColor(R.color.active_signal));
            } else {
                binding.txtChange.setTextColor(Helpers.getColor(R.color.expired_signal));
            }
        });


        binding.anyChartView.setProgressBar(binding.progressBar);
        AlphaVantage.makeStockChart(signal.getTicker(), 30, binding.anyChartView);

        return view;
    }

    protected void closeSignal() {
        signal.closeSignal();
        binding.btnAction.setVisibility(View.INVISIBLE);

        // Need to recreate the message to update the LIVE to EXPIRED
        binding.layout.removeViewAt(0);
        binding.layout.addView(Helpers.createSignalMessage(getLayoutInflater(), signal, false), 0);
    }

    protected void upvote() {
        signal.addUpvote(State.getUser().getUid());
        binding.btnAction.setText(getString(R.string.remove_signal_upvote));
        binding.btnAction.setOnClickListener(v -> remove_upvote());
    }

    protected void remove_upvote() {
        signal.removeUpvote(State.getUser().getUid());

        binding.btnAction.setText(getString(R.string.signal_upvote));
        binding.btnAction.setOnClickListener(v -> upvote());
    }


}