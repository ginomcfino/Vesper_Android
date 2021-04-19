package com.company.vesper.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.company.vesper.MainActivity;
import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.ActivitySignupBinding;
import com.company.vesper.lib.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    public static String TAG = SignupActivity.class.getName();

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void signup(View v) {
        if (binding.edtEmail.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.email_empty_error), Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (binding.edtPassword.getText().length() < 8) {
            Toast.makeText(this, getString(R.string.password_short_error), Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!binding.edtPassword.getText().toString().equals(binding.edtPasswordConfirm.getText().toString())) {
            Toast.makeText(this, getString(R.string.password_unmatch_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            Toast.makeText(this, getString(R.string.email_invalid_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.edtName.getText().length() == 0) {
            Toast.makeText(this, "Please choose a display name", Toast.LENGTH_SHORT).show();
            return;
        }

        createNewUser(binding.edtName.getText().toString(), binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
    }


    /**
     * Signup a new user with Firebase User Auth
     * @param displayName the displayed name of the user
     * @param email email of user trying to sign up
     * @param password password of user trying to sign up
     */
    protected void createNewUser(String displayName, String email, String password) {
        State.getAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("device", "");
                        data.put("displayName", displayName);
                        data.put("email", email);
                        data.put("groups", new ArrayList<String>());
                        data.put("watchlist", new ArrayList<String>());

                        State.getDatabase().collection("users").document(task.getResult().getUser().getUid()).set(data);

                        LoginActivity.login(email, password, SignupActivity.this);
                    } else {
                        Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException().getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void switchToLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}