package com.example.surajgdesai.homework09a;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shara on 4/13/2017.
 */

public class TripsFragment extends Fragment {

    String[] joinorAddTrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.trips_fragment, container, false);
        FloatingActionButton fb = (FloatingActionButton) root.findViewById(R.id.fabTrips);

        joinorAddTrip = getResources().getStringArray(R.array.AddOrJoinTrip);


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

                Snackbar.make(root, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}