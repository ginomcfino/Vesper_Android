package com.company.vesper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.company.vesper.databinding.ActivityMainBinding;
import com.company.vesper.services.AlphaVantage;
import com.company.vesper.services.YahooNewsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName(); //TAG for debugging

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setCurrentFragment(new HomeFragment());

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setCurrentFragment(new HomeFragment());
                    break;
                case R.id.nav_signal:
                    setCurrentFragment(new SignalFragment());
                    break;
                case R.id.nav_chat:
                    setCurrentFragment(new ChatFragment());
                    break;
                case R.id.nav_preference:
                    setCurrentFragment(new SettingsFragment());
                    break;
            }
            return false;
        });

    }

    protected void setCurrentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_wrapper, fragment, null);

        transaction.commit();

    }
}

