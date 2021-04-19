package com.company.vesper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.company.vesper.chat.ChatFragment;
import com.company.vesper.databinding.ActivityMainBinding;
import com.company.vesper.lib.Preferences;
import com.company.vesper.services.FCMServiceHandler;
import com.company.vesper.signal.SignalFragment;

/**
 * Main activity of the app. Holds the bottom navbar and a layout on top for switch fragments. Most page switches should utilize
 * MainActivity.instance.setCurrentFragment() to switch the pages.
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName(); //TAG for debugging

    private ActivityMainBinding binding;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FCMServiceHandler.registerUser(State.getUser().getUid());

        String firstPage = Preferences.getValue("FirstPage", "Home");
        if (firstPage.equals("Home")) {
            setCurrentFragment(new HomeFragment());
        } else if (firstPage.equals("Chat")){
            setCurrentFragment(new ChatFragment());
        } else if (firstPage.equals("Signal")){
            setCurrentFragment(new SignalFragment());
        }

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
            return true;
        });
        instance = this;
    }

    /**
     * Switch the currently loaded fragment to a new one (maintains the same window)
     * @param fragment New fragment be instantiated. (The actual fragment object and not just the class)
     */
    public void setCurrentFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_wrapper, fragment, null).addToBackStack(TAG);

        transaction.commit();
    }
}

