package com.company.vesper;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.company.vesper.databinding.FragmentSettingsBinding;
import com.company.vesper.dialogs.EditTextDialog;
import com.company.vesper.lib.Helpers;
import com.company.vesper.lib.Preferences;
import com.company.vesper.logins.LoginActivity;

////// int color = Color.parseColor("#2222FF");

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        bindColorButtons();

        binding.btnlogout.setOnClickListener(view -> {
            Preferences.clear();
            Helpers.switchToActivity(getContext(), 0, LoginActivity.class);
        });

        return binding.getRoot();
    }

    private void bindColorButtons() {
        binding.btnBull.setOnClickListener(view -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();

            EditTextDialog dialog = new EditTextDialog("Pick color",
                    Helpers.formatColor(Helpers.getColor(R.color.active_signal)),
                    "#[0-9A-F]{6}",
                    text -> {
                        if (text.length() > 0) {
                            Helpers.putColor(R.color.active_signal, Color.parseColor(text));
                            Preferences.putValue("ACTIVE_COLOR", Color.parseColor(text));
                        }
                    });

            dialog.show(fm, "");
        });

        binding.btnBear.setOnClickListener(view -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();

            EditTextDialog dialog = new EditTextDialog("Pick color",
                    Helpers.formatColor(Helpers.getColor(R.color.expired_signal)),
                    "#[0-9A-F]{6}",
                    text -> {
                        if (text.length() > 0) {
                            Helpers.putColor(R.color.expired_signal, Color.parseColor(text));
                            Preferences.putValue("EXPIRE_COLOR", Color.parseColor(text));
                        }
                    });

            dialog.show(fm, "");
        });
    }


}