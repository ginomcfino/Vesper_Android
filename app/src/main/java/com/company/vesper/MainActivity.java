package com.company.vesper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.company.vesper.databinding.ActivityMainBinding;
import com.company.vesper.services.AlphaVantage;
import com.company.vesper.services.YahooNewsActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName(); //TAG for debugging


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnYahooNews.setOnClickListener(view -> YahooNewsActivity.getNews(response -> binding.webView.loadUrl(response)));

        binding.btnAlphaVantage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent avIntent = new Intent(MainActivity.this, AlphaVantage.class);
                MainActivity.this.startActivity(avIntent);
                Log.e(TAG, "Going to make an AlphaVantage API CALL >>> >>> >>>");
            }
        });
    }
}