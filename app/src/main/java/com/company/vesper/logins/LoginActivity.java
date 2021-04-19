package com.company.vesper.logins;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.company.vesper.MainActivity;
import com.company.vesper.R;
import com.company.vesper.State;
import com.company.vesper.databinding.ActivityLoginBinding;
import com.company.vesper.lib.Helpers;
import com.company.vesper.lib.Preferences;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * The first page the user sees. If login credentials are stored in preferences, we automatically log the users in.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Preferences.contains("email")) {
            login(Preferences.getValue("email", ""), Preferences.getValue("password", ""), this);
            binding.edtEmail.setText(Preferences.getValue("email", ""));
            binding.edtPassword.setText(Preferences.getValue("password", ""));
        }
    }

    public void login(View v) {
        if (binding.edtEmail.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.blank_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.edtPassword.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.blank_password), Toast.LENGTH_SHORT).show();
            return;
        }

        login(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString(), this);
    }

    public void switchToSignup(View v) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    /**
     * Login with with Firebase User Auth
     * @param email email of user trying to login
     * @param password password of user trying to login
     */
    public static void login(String email, String password, Context context) {
        FirebaseAuth auth = State.getAuth();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        State.setUser(auth.getCurrentUser());
                        Toast.makeText(context, "Login success.",
                                Toast.LENGTH_SHORT).show();

                        Helpers.switchToActivity(context, 1000, MainActivity.class);

                        Preferences.putValue("email", email);
                        Preferences.putValue("password", password);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}