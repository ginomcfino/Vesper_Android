package com.company.vesper.logins;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO! remove this in the future, only here for debugging.
        login("ericjyc@bu.edu", "password");
    }

    public void login(View v) {
        if (binding.edtEmail.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.blank_email), Toast.LENGTH_SHORT).show();
        }
        if (binding.edtPassword.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.blank_password), Toast.LENGTH_SHORT).show();
        }

        login(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
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
    protected void login(String email, String password) {
        FirebaseAuth auth = State.getAuth();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        State.setUser(auth.getCurrentUser());
                        Toast.makeText(LoginActivity.this, "Login success.",
                                Toast.LENGTH_SHORT).show();
                        Helpers.switchToActivity(LoginActivity.this, 1000, MainActivity.class);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}