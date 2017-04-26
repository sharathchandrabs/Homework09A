package com.example.surajgdesai.homework09a;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AcceptRequestsActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SearchView searchView;
    ArrayList<User> userList;
    RecyclerView friendsList;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_requests);

        userList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AcceptRequestsActivity
                .this);
        userKey = sharedPreferences.getString("userKey", null);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        friendsList = (RecyclerView) findViewById(R.id.acceptRequestsRV);
        RecyclerAcceptRequests adapter = new RecyclerAcceptRequests(databaseReference, userKey, this, R
                .layout.acceptfriendslayoutrv);
        friendsList.setAdapter(adapter);
        friendsList.setLayoutManager(new LinearLayoutManager(AcceptRequestsActivity.this));
        adapter.notifyDataSetChanged();


    }
}
