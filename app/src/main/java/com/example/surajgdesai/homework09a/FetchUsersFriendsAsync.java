package com.example.surajgdesai.homework09a;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Created by shara on 4/19/2017.
 */

public class FetchUsersFriendsAsync extends AsyncTask<String, Void, ArrayList<String>> {
    FetchUsersFriendsAsync.IGetEpisodes mainActivity;
    FirebaseStorage firebaseStorage;

    ArrayList<User> userList;
    ArrayList<String> friendsKeys;
    DatabaseReference friendslistusersRef;

    public FetchUsersFriendsAsync(FetchUsersFriendsAsync.IGetEpisodes mainActivity) {
        this.mainActivity = mainActivity;
        userList = new ArrayList<>();
        friendsKeys = new ArrayList<>();
    }

    public static interface IGetEpisodes {
        public void fetchEpisodes(ArrayList<String> gameList);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {

        final String key = params[0];
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(key).child("Friends");
        try {
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Log.d("friends", postSnapshot.toString());
                        friendsKeys.add(postSnapshot.getValue().toString());

                    }
                    /*friendslistusersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    friendslistusersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()
                                    ) {
                                if (friendsKeys.contains(d.getKey())) {
                                    userList.add(d.getValue(User.class));
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return friendsKeys;

    }

    @Override
    protected void onPostExecute(ArrayList<String> gameList) {
        mainActivity.fetchEpisodes(gameList);
    }
}

