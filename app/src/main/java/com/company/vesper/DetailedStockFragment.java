package com.company.vesper;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.company.vesper.databinding.FragmentChatBinding;
import com.company.vesper.databinding.FragmentDetailedStockBinding;
import com.company.vesper.services.AlphaVantage;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedStockFragment#newInstance} factory method to
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

<<<<<<< Updated upstream
    private FragmentDetailedStockBinding binding;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedStockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedStockFragment newInstance(String param1, String param2) {
        DetailedStockFragment fragment = new DetailedStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailedStockFragment() {
        // Required empty public constructor
=======

    private String ticker;
    private double currentPrice;
    private double dailyChange;

    private FragmentDetailedStockBinding binding;


    public DetailedStockFragment(String ticker, double currentPrice, double dailyChange) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.dailyChange = dailyChange;
>>>>>>> Stashed changes
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
        view.setProgressBar(binding.progressBar);

        TextView textTicker = binding.textViewTicker;
        TextView textCurrentPrice = binding.textViewPrice;
        TextView textChange = binding.textViewChange;



        textTicker.setText(ticker);

        textCurrentPrice.setText(Double.toString(currentPrice));

        if(dailyChange > 0){
            textChange.setTextColor(Color.GREEN);
            textChange.setText("+ $" + Double.toString(dailyChange));
            AlphaVantage.makeStockChart(ticker, true, 30, view);
        }else {
            textChange.setTextColor(Color.RED);
            textChange.setText("- $" + Double.toString(dailyChange));
            AlphaVantage.makeStockChart(ticker, false, 30, view);

        }

<<<<<<< Updated upstream
        AlphaVantage.makeStockChart("TSLA",10,view);
=======
>>>>>>> Stashed changes

        return binding.getRoot();
    }
}