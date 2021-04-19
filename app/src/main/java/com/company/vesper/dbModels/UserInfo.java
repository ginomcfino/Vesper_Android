package com.company.vesper.dbModels;

import com.company.vesper.State;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class that holds the data relevant to a user
 */
public class UserInfo {
    private static String TAG = UserInfo.class.getName();


    FirebaseUser user;
    String display_name;
    String email;
    List<GroupInfo> groups;
    List<String> watchlist;

    public UserInfo(FirebaseUser user) {
        this.user = user;
        // populate
        populateData();
    }

    public void addToWatchlist(String Ticker) {
        getWatchlist().add(Ticker);
        State.getDatabase().collection("users").document(getUid()).update("watchlist", watchlist);
    }

    private void populateData() {
        State.getDatabase().collection("users").document(user.getUid()).get().addOnCompleteListener(t -> {
            DocumentSnapshot snapshot = t.getResult();
            display_name = snapshot.getString("displayName");
            email = snapshot.getString("email");
            watchlist = (List<String>) snapshot.get("watchlist");
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
                        State.getDatabase().collection("groups").document(groups.get(0).getID()).get().addOnCompleteListener(groupTask -> {
                            DocumentSnapshot groupSnapshot = groupTask.getResult();
                            State.setGroup(groupSnapshot);
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

    public List<String> getWatchlist() {
        return watchlist;
    }
}
