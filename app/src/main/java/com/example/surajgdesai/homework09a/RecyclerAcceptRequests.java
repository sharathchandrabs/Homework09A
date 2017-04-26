package com.example.surajgdesai.homework09a;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
 * Created by Suraj G Desai on 4/21/2017.
 */

public class RecyclerAcceptRequests extends RecyclerView.Adapter<RecyclerAcceptRequests.ViewHolder> {

    ArrayList<User> allusers = new ArrayList<>();
    Context gContext;

    int currentLayout;
    SharedPreferences sharedPreferences;
    String loggedInUserKey;
    ArrayList newList;

    RecyclerAcceptRequests(DatabaseReference dbRef, String userkey, Context context, int currentlayout) {
        this.gContext = context;
        this.currentLayout = currentlayout;
        this.loggedInUserKey = userkey;

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<User> bedderoUsers = new ArrayList<User>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    bedderoUsers.add(u);
                }

                DatabaseReference userfrndRef = FirebaseDatabase.getInstance().getReference().child("Users/" + loggedInUserKey + "/Notifications");
                userfrndRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String s = d.getValue(String.class);
                                newList = new ArrayList(bedderoUsers);
                                for (User newUser : bedderoUsers) {
                                    if (!newUser.getKey().equals(s)) {
                                        newList.remove(newUser);
                                    }
                                }

                            }

                            allusers.clear();
                            allusers = new ArrayList<User>(newList);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(gContext, "No friend requests at this point in time!", Toast.LENGTH_SHORT).show();
                            ((Activity) gContext).finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public RecyclerAcceptRequests.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, viewGroup, false);
        RecyclerAcceptRequests.ViewHolder viewHolder = new RecyclerAcceptRequests.ViewHolder
                (contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAcceptRequests.ViewHolder viewHolder, final int position) {
        TextView frdsTxtVw = viewHolder.frdsTxtVw;
        ImageView frndsImageView = viewHolder.frndsImageView;
        ImageButton acceptRequestBtn = viewHolder.acceptRequestBtn;
        ImageButton rejectRequestBtn = viewHolder.rejectRequestBtn;

        acceptRequestBtn.setTag(position);
        rejectRequestBtn.setTag(position);

        frdsTxtVw.setText(allusers.get(position).getDisplayName());
        Picasso.with(gContext).load(allusers.get(position).getProfilePicUrl()).into(frndsImageView);


        acceptRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int tag = (int) v.getTag();
                Log.d("allusers", allusers.get(tag).toString());
                try {

                    DatabaseReference dbref1 = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + loggedInUserKey + "/Friends");

                    dbref1.push().setValue(allusers.get(tag).getKey());

                    DatabaseReference dbref3 = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + allusers.get(tag).getKey() + "/Friends");

                    dbref3.push().setValue(loggedInUserKey);

                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + loggedInUserKey + "/Notifications");

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getValue(String.class).equals(allusers.get(tag).getKey())) {
                                    snapshot.getRef().removeValue();
                                    allusers.remove(tag);
                                    notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    dbref.child(allusers.get(position).getKey()).removeValue();
                    DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + allusers.get(tag).getKey() + "/SentNotifications");

                    dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getValue(String.class).equals(loggedInUserKey)) {
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        rejectRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int tag = (int) v.getTag();

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child
                        ("Users/" + loggedInUserKey + "/Notifications");


                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue(String.class).equals(allusers.get(tag).getKey())) {
                                snapshot.getRef().removeValue();
                                allusers.remove(tag);
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                dbref.child(allusers.get(position).getKey()).removeValue();

                DatabaseReference dbref2 = FirebaseDatabase.getInstance().getReference().child
                        ("Users/" + allusers.get(tag).getKey() + "/SentNotifications");

                dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getValue(String.class).equals(loggedInUserKey)) {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return allusers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView frdsTxtVw;
        public ImageView frndsImageView;
        ImageButton acceptRequestBtn;
        ImageButton rejectRequestBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            frdsTxtVw = (TextView) itemView.findViewById(R.id.frdsTxtVwAR);
            frndsImageView = (ImageView) itemView.findViewById(R.id.friendsProfileImageViewAR);
            acceptRequestBtn = (ImageButton) itemView.findViewById(R.id.imageButtonaddAR);
            rejectRequestBtn = (ImageButton) itemView.findViewById(R.id.imageButtoncancelAR);
        }
    }
}
