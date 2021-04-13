package com.company.vesper;

import android.icu.number.NumberFormatter;

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
    private static String TAG = State.class.getName();

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

    public static String getDeviceFCMToken() {
        return deviceFCMToken;
    }

    public static void setDeviceFCMToken(String token) {
        deviceFCMToken = token;
    }

    public static class UserInfo {
        FirebaseUser user;
        String display_name;
        String email;
        List<GroupInfo> groups;

        UserInfo(FirebaseUser user) {
            this.user = user;

            // populate
            populateData();
        }

        private void populateData() {
            State.getDatabase().collection("users").document(user.getUid()).get().addOnCompleteListener(t -> {
                DocumentSnapshot snapshot = t.getResult();
                display_name = snapshot.getString("displayName");
                email = snapshot.getString("email");
                List<DocumentReference> groupRefs = (List<DocumentReference>) snapshot.get("groups");
                groups = new ArrayList<>();
                for (int i = 0; i < groupRefs.size(); i++) {
                    DocumentReference docs = groupRefs.get(i);
                    int finalI = i;
                    docs.get().addOnCompleteListener(task -> {
                        DocumentSnapshot ss = task.getResult();
                        groups.add(new GroupInfo(ss));
                        if (finalI == 0) {
                            // TODO fix this, we currently just load in the first group of a user. What to do if there is no groups?
                            State.getDatabase().collection("groups").document(groups.get(0).groupID).get().addOnCompleteListener(groupTask -> {
                                DocumentSnapshot groupSnapshot = groupTask.getResult();
                                group = new GroupInfo(groupSnapshot);
                            });
                        }
                    });
                }
            });

            // TODO check if current user is the same as the previously logged in person.
        }

        public String getUid() {
            return user.getUid();
        }

        public String getName() {
            return display_name;
        }

        public List<GroupInfo> getGroups() {
            return groups;
        }
    }

    public static class GroupInfo {

        String name;
        String groupID;
        String signaler;
        List<String> members;
        DocumentReference ref;

        GroupInfo(String name, String ID) {
            this.name = name;
            this.groupID = ID;
        }

        GroupInfo(DocumentSnapshot snapshot) {
            name = snapshot.getString("name");
            signaler = snapshot.getString("signaler");
            groupID = snapshot.getId();

            members = (List<String>) snapshot.get("members");
            ref = snapshot.getReference();
        }

        public String getID() {
            return groupID;
        }

        public String getSignaler() {
            return signaler;
        }

        public String getName() {
            return name;
        }

        public DocumentReference getRef() {
            return ref;
        }
    }
}
