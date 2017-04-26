package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shara on 4/21/2017.
 */

public class RecyclerViewTripsFragment extends RecyclerView.Adapter<RecyclerViewTripsFragment
        .ViewHolder> {

    Context gContext;
    ArrayList<Trip> gObjects = new ArrayList<>();
    ArrayList<String> userTrips = new ArrayList<>();
    int currentLayout;
    DatabaseReference tripDbRef;
    boolean isRequest;
    SharedPreferences preferences;
    String currentUser;

    RecyclerViewTripsFragment(DatabaseReference dbRef, Context context, int currentlayout, final boolean isRequest) {
        this.gContext = context;
        this.currentLayout = currentlayout;
        this.tripDbRef = dbRef;
        this.isRequest = isRequest;

        preferences = PreferenceManager.getDefaultSharedPreferences(gContext);
        currentUser = preferences.getString("userKey", null);

        tripDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gObjects.clear();
                final ArrayList<String> tripskeys = new ArrayList<String>();
                final ArrayList<Trip> trippo = new ArrayList<Trip>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    if (isRequest) {
                        Trip value = d.getValue(Trip.class);
                        trippo.add(value);
                    } else {
                        String tempTrip = d.getValue(String.class);
                        tripskeys.add(tempTrip);
                    }
                }

                if (isRequest) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users/" + currentUser + "/Trips");
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.getChildrenCount() == trippo.size()) {
                                    Toast.makeText(gContext, "There are no more trips to join, go with existing ones!", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        for (int count = 0; count < trippo.size(); count++) {
                                            if (snapshot.getValue(String.class).equals(trippo.get(count).getTripKey())) {
                                                trippo.remove(count);
                                            }
                                        }
                                    }

                                    gObjects = new ArrayList<Trip>(trippo);
                                    notifyDataSetChanged();
                                }

                            } else {
                                gObjects = new ArrayList<Trip>(trippo);
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Trips");
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                Trip t = d.getValue(Trip.class);
                                if (tripskeys.contains(t.getTripKey())) {
                                    gObjects.add(t);
                                }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage());
            }
        });

    }

    @Override
    public RecyclerViewTripsFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, parent, false);
        RecyclerViewTripsFragment.ViewHolder viewHolder = new RecyclerViewTripsFragment.ViewHolder(contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewTripsFragment.ViewHolder holder, final int position) {
        TextView frdsTxtVw = holder.tripTitle;
        ImageView frndsImgVw = holder.tripImgVw;
        final Button addfriend = holder.btn;
        frdsTxtVw.setTag(position);
        frdsTxtVw.setText(gObjects.get(position).getTitle());
        Picasso.with(gContext).load(gObjects.get(position).getTripProfilePhotoUrl()).into(frndsImgVw);
        if (this.isRequest) {
            addfriend.setVisibility(View.VISIBLE);
            addfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Trips");
                    String userTripKey = userRef.push().getKey();
                    String tripsKey = gObjects.get(position).getTripKey();
                    userRef.child(userTripKey).setValue(tripsKey);
                }
            });


        } else {
            addfriend.setVisibility(View.INVISIBLE);
        }

        frdsTxtVw.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int tag = (int) v.getTag();

                AlertDialog.Builder builder = new AlertDialog.Builder(gContext);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference tripref = FirebaseDatabase.getInstance().getReference().child("Trips");
                        tripref.child(gObjects.get(tag).getTripKey()).removeValue();
                        gObjects.remove(tag);
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setMessage("Are you sure?").setTitle("Delete?").show();

                return false;
            }
        });

        frdsTxtVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final int tag = (int) v.getTag();
                    Intent messageIntent = new Intent(gContext, MessagesActivity.class);
                    messageIntent.putExtra("TripKey", gObjects.get(tag).getTripKey());
                    gContext.startActivity(messageIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return gObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tripTitle;
        ImageView tripImgVw;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);

            tripTitle = (TextView) itemView.findViewById(R.id.tripsTxtVw);
            tripImgVw = (ImageView) itemView.findViewById(R.id.tripImageView);
            btn = (Button) itemView.findViewById(R.id.joinBtn);

            btn.setVisibility(View.INVISIBLE);
        }
    }
}
