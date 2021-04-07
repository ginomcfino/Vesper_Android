package com.company.vesper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class State {
    private static String TAG = State.class.getName();

    private static FirebaseAuth auth;
    private static FirebaseFirestore database;
    private static FirebaseUser user;


    public static void init() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }


    public static FirebaseAuth getAuth() {
        return auth;
    };

    public static FirebaseFirestore getDatabase() {
        return database;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        State.user = user;
    }
}
