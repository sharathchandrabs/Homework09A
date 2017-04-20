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

public class FetchUsersFriendsAsync extends AsyncTask<String, Void, ArrayList<User>> {
    FetchUsersFriendsAsync.IGetEpisodes mainActivity;
    FirebaseStorage firebaseStorage;

    ArrayList<User> userList;

    public FetchUsersFriendsAsync(FetchUsersFriendsAsync.IGetEpisodes mainActivity) {
        this.mainActivity = mainActivity;
        userList = new ArrayList<>();
    }

    public static interface IGetEpisodes {
        public void fetchEpisodes(ArrayList<User> gameList);
    }

    @Override
    protected ArrayList<User> doInBackground(String... params) {

        final String key = params[0];
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(key).child("Friends");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("friends", postSnapshot.toString());




                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userList;

    }

    @Override
    protected void onPostExecute(ArrayList<User> gameList) {
        mainActivity.fetchEpisodes(gameList);
    }
}

