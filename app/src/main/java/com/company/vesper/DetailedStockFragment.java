package com.company.vesper;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.company.vesper.databinding.FragmentChatBinding;
import com.company.vesper.databinding.FragmentDetailedStockBinding;
import com.company.vesper.services.AlphaVantage;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 *
 */
public class DetailedStockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String ticker;

    private FragmentDetailedStockBinding binding;


    public DetailedStockFragment(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailedStockBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        AnyChartView view = binding.anyChartView;

        AlphaVantage.makeStockChart(ticker,10,view);

        return binding.getRoot();
    }
}