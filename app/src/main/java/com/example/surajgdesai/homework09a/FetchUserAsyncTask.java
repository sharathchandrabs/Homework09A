package com.example.surajgdesai.homework09a;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shara on 4/19/2017.
 */

public class FetchUserAsyncTask extends AsyncTask<String, Void, ArrayList<User>> {
    FetchUserAsyncTask.IGetEpisodes mainActivity;
    FirebaseStorage firebaseStorage;

    ArrayList<User> userList;

    public FetchUserAsyncTask(FetchUserAsyncTask.IGetEpisodes mainActivity) {
        this.mainActivity = mainActivity;
        userList = new ArrayList<>();
    }

    public static interface IGetEpisodes {
        public void fetchedUsers(ArrayList<User> gameList);
    }

    @Override
    protected ArrayList<User> doInBackground(String... params) {

        final String key = params[0];
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User post = postSnapshot.getValue(User.class);
                    if(post.getKey().equals(key)){
                        continue;
                    }else{
                        userList.add(post);
                        Log.d("Get Data", post.toString());
                    }

                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return userList;

    }

    @Override
    protected void onPostExecute(ArrayList<User> gameList) {
        mainActivity.fetchedUsers(gameList);
    }
}
