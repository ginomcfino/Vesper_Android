package com.company.vesper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.vesper.services.YahooNewsActivity;

public class MainActivity extends AppCompatActivity {

    Button btnYahooNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnYahooNews = findViewById(R.id.btnYahooNews);

        btnYahooNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ynIntent = new Intent(MainActivity.this, YahooNewsActivity.class);
                MainActivity.this.startActivity(ynIntent);
            }
        });
    }
}