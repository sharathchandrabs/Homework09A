package com.example.surajgdesai.homework09a;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class JoinTripActivity extends AppCompatActivity {


    String[] joinorAddTrip;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SearchView searchView;
    RecyclerView friendsFragmentRecycler;
    ArrayList<User> usersFriends;
    String currentUser;
    RecyclerView tripRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trip);
        tripRV =(RecyclerView) findViewById(R.id.tripsRecyclerVw);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentUser = sharedPreferences.getString("userKey", null);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");

        RecyclerViewTripsFragment adapter = new RecyclerViewTripsFragment(databaseReference, this
                , R.layout.trips_layout, true);
        tripRV.setAdapter(adapter);
        tripRV.setLayoutManager(new LinearLayoutManager(this));
    }
}
