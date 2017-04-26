package com.example.surajgdesai.homework09a;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by shara on 4/13/2017.
 */

public class TripsFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.trips_fragment, container, false);
        FloatingActionButton fb = (FloatingActionButton) root.findViewById(R.id.fabTrips);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentUser = sharedPreferences.getString("userKey", null);
        joinorAddTrip = getResources().getStringArray(R.array.AddOrJoinTrip);
        tripRV = (RecyclerView) root.findViewById(R.id.tripsRecyclerVwTrip);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TripsFragment.this.getContext());
                builder.setCancelable(true);
                builder.setItems(R.array.AddOrJoinTrip, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (joinorAddTrip[which]) {
                            case "Create a Trip":
                                Intent intent = new Intent(getContext(), CreateTripActivity.class);
                                startActivity(intent);
                                break;
                            case "Join in a Trip":
                                Intent tripintent = new Intent(getContext(), JoinTripActivity.class);
                                startActivity(tripintent);
                                break;
                        }
                    }
                });

                builder.show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users/" + currentUser + "/Trips");
        RecyclerViewTripsFragment adapter = new RecyclerViewTripsFragment(databaseReference, getActivity()
                , R
                .layout.trips_layout, false);
        tripRV.setAdapter(adapter);
        tripRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}