package com.example.surajgdesai.homework09a;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class FriendRequestActivity extends AppCompatActivity implements FetchUserAsyncTask.IGetEpisodes{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SearchView searchView;
    ArrayList<User> userList;
    RecyclerView friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FriendRequestActivity
                .this);
        String userKey = sharedPreferences.getString("userKey",null);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        new FetchUserAsyncTask(this).execute(userKey);


    }

    @Override
    public void fetchEpisodes(ArrayList<User> gameList) {
        friendsList = (RecyclerView) findViewById(R.id.friendsRecycler);
        RecyclerViewFriends adapter = new RecyclerViewFriends(this, gameList, R.layout
                .friends_layout);
        friendsList.setAdapter(adapter);
        friendsList.setLayoutManager(new LinearLayoutManager(FriendRequestActivity.this));
        adapter.notifyDataSetChanged();

    }
}
