package com.company.vesper;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.company.vesper.databinding.FragmentChatBinding;
import com.company.vesper.databinding.FragmentDetailedStockBinding;
import com.company.vesper.services.AlphaVantage;
import com.company.vesper.services.StockNews;

import java.util.List;

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


    private final String ticker;
    private final double currentPrice;
    private final double dailyChange;
    private final String percentChange;

    private FragmentDetailedStockBinding binding;

    private RecyclerView viewNews;
    private com.company.vesper.CustomNewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public DetailedStockFragment(String ticker, double currentPrice, double dailyChange, String percentChange) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
        this.dailyChange = dailyChange;
        this.percentChange = percentChange;
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

        viewNews = binding.recyclerViewNews;
        mLayoutManager = new LinearLayoutManager(getActivity());
        viewNews.setLayoutManager(mLayoutManager);

        //Initialize dataSet
        //List<StockNews.NewsItem> mData = StockNews.getNewsData("AAPL");

        StockNews.getNewsData(ticker, response -> {
            mAdapter = new com.company.vesper.CustomNewsAdapter(response);
            viewNews.setAdapter(mAdapter);

        });


        textTicker.setText(ticker);

        textCurrentPrice.setText(Double.toString(currentPrice));

        if (dailyChange > 0) {
            textChange.setTextColor(Color.GREEN);
            textChange.setText(dailyChange + "$ (" + percentChange + ")");
            AlphaVantage.makeStockChart(ticker, 30, view);
        } else {
            textChange.setTextColor(Color.RED);
            textChange.setText(dailyChange + "$ (" + percentChange + ")");
            AlphaVantage.makeStockChart(ticker, 30, view);

        }


        return binding.getRoot();
    }
}