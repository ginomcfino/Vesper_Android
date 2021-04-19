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
    private static final String TAG = UserInfo.class.getName();


    static FirebaseUser user;
    static String display_name;
    static String email;
    static List<GroupInfo> groups;
    static List<String> watchlist;

    public UserInfo(FirebaseUser user) {
        UserInfo.user = user;
        // populate
        populateData();
    }

    public static void addToWatchlist(String Ticker) {
        getWatchlist().add(Ticker);
        State.getDatabase().collection("users").document(getUid()).update("watchlist", watchlist);
    }
    public static void removeFromWatchlist(String Ticker) {
        getWatchlist().remove(Ticker);
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

    public static String getUid() {
        return user.getUid();
    }

    public String getName() {
        return display_name;
    }

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public static List<String> getWatchlist() {
        return watchlist;
    }

    public void joinGroup(GroupInfo group) {
        groups.add(group);

        List<DocumentReference> docRefs = new ArrayList<>();
        for(GroupInfo g : groups) {
            docRefs.add(g.ref);
        }

        State.getDatabase().collection("users").document(getUid()).update("groups", docRefs);
        group.ref.get().addOnCompleteListener(task -> {
            List<DocumentReference> members = (List<DocumentReference>) task.getResult().get("members");
            members.add(State.getDatabase().collection("users").document(getUid()));
            group.ref.update("members", members);
        });
    }
}
