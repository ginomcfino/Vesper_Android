package com.company.vesper.dbModels;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Holds the data for a group, also some update functions to update values to the DB
 */
public class GroupInfo {
    private static final String TAG = GroupInfo.class.getName();
    private final String name;
    private final String groupID;
    private final String signaler;
    private final List<String> members;
    private final int upvote_count;
    private int excellent_signals;
    private int good_signals;

    DocumentReference ref;

    /**
     * Create a group from the DB snapshot
     * @param snapshot the current snapshot from the DB to load from
     */
    public GroupInfo(DocumentSnapshot snapshot) {
        name = snapshot.getString("name");
        signaler = snapshot.getString("signaler");
        groupID = snapshot.getId();

        members = (List<String>) snapshot.get("members");
        upvote_count = snapshot.get("upvote_count", Integer.class);
        ref = snapshot.getReference();


        excellent_signals = snapshot.get("excellent_signals", Integer.class);
        good_signals = snapshot.get("good_signals", Integer.class);
    }

    public String getID() {
        return groupID;
    }

    public String getSignaler() {
        return signaler;
    }

    public int getNumMembers() {return members.size();}

    public String getName() {
        return name;
    }

    public int getUpvote_count() {
        return upvote_count;
    }

    public int getExcellent_signals() {
        return excellent_signals;
    }

    public int getGood_signals() {
        return good_signals;
    }

    public DocumentReference getRef() {
        return ref;
    }

    /**
     * Add 1 to the excellent signal count of the group, both locally and push to the DB
     */
    public void incrementExcellentSignal() {
        excellent_signals += 1;
        ref.update("excellent_signals", excellent_signals);
    }

    /**
     * Add 1 to the good signal count of the group, both locally and push to the DB
     */
    public void incrementGoodSignal() {
        good_signals += 1;
        ref.update("good_signals", good_signals);
    }
}
