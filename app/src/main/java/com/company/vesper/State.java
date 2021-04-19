package com.company.vesper;

import android.icu.number.NumberFormatter;
import android.widget.Toast;

import com.company.vesper.dbModels.GroupInfo;
import com.company.vesper.dbModels.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class State {
    private static final String TAG = State.class.getName();

    private static FirebaseAuth auth;
    private static FirebaseFirestore database;
    private static UserInfo user;

    private static GroupInfo group;
    private static String deviceFCMToken;
    private static Map<String, String> usernameMap;

    public static void init() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();


        usernameMap = new HashMap<>();
        State.getDatabase().collection("users").get().addOnCompleteListener(task -> {
            QuerySnapshot querySnapshot = task.getResult();
            for (DocumentSnapshot docSnap: querySnapshot.getDocuments()) {
                usernameMap.put(docSnap.getId(), docSnap.getString("displayName"));
            }
        });
    }

    /**
     * Get the corresponding display name of a user from their ID
     * @param id the user ID of the queried user
     * @return the display name of the queried user
     */
    public static String getName(String id) {
        return usernameMap.get(id);
    }

    public static FirebaseAuth getAuth() {
        return auth;
    }

    public static FirebaseFirestore getDatabase() {
        return database;
    }

    public static UserInfo getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        // TODO also update device token here.
        State.user = new UserInfo(user);

    }

    public static GroupInfo getGroup() {
        return group;
    }

    public static void setGroup(DocumentSnapshot snapshot) {
        group = new GroupInfo(snapshot);
    }

    public static void setGroup(GroupInfo group) {
        group = group;
    }

    public static String getDeviceFCMToken() {
        return deviceFCMToken;
    }

    public static void setDeviceFCMToken(String token) {
        deviceFCMToken = token;
    }

}
