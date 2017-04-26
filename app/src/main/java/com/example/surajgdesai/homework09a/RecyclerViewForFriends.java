package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.surajgdesai.homework09a.R.id.parent;

/**
 * Created by shara on 4/21/2017.
 */

public class RecyclerViewForFriends extends RecyclerView.Adapter<RecyclerViewForFriends.ViewHolder> {

    ArrayList<User> allusers = new ArrayList<>();
    Context gContext;

    int currentLayout;
    SharedPreferences sharedPreferences;
    String loggedInUserKey;

    RecyclerViewForFriends(DatabaseReference dbRef, String userkey, Context context, int currentlayout) {
        this.gContext = context;
        this.currentLayout = currentlayout;
        this.loggedInUserKey = userkey;

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allusers.clear();
                final ArrayList<User> bedderoUsers = new ArrayList<User>();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    User u = d.getValue(User.class);
                    bedderoUsers.add(u);
                }

                for (int count = 0; count < bedderoUsers.size(); count++) {
                    if (bedderoUsers.get(count).getKey().equals(loggedInUserKey)) {
                        bedderoUsers.remove(count);
                    }
                }


                DatabaseReference userfrndRef = FirebaseDatabase.getInstance().getReference().child("Users/" + loggedInUserKey + "/Friends");
                userfrndRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            String s = d.getValue(String.class);

                            for (int count = 0; count < bedderoUsers.size(); count++) {
                                if (bedderoUsers.get(count).getKey().equals(s)) {
                                    bedderoUsers.remove(count);
                                }
                            }


                        }

                        DatabaseReference userfrndRef1 = FirebaseDatabase.getInstance().getReference().child("Users/" + loggedInUserKey + "/SentNotifications");

                        userfrndRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    String s = d.getValue(String.class);

                                    for (int count = 0; count < bedderoUsers.size(); count++) {
                                        if (bedderoUsers.get(count).getKey().equals(s)) {
                                            bedderoUsers.remove(count);
                                        }
                                    }


                                }

                                allusers = new ArrayList<User>(bedderoUsers);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
/*
                        for (User beddUser : bedderoUsers) {


                            DatabaseReference userfrndRef1 = FirebaseDatabase.getInstance().getReference().child("Users/" + beddUser.getKey() + "/Notifications");
                            userfrndRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                                        String s = d.getValue(String.class);

                                        for (int count = 0; count < bedderoUsers.size(); count++) {
                                            if (bedderoUsers.get(count).getKey().equals(s)) {
                                                bedderoUsers.remove(count);
                                            }
                                        }
                                    }

                                    allusers = new ArrayList<User>(bedderoUsers);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }*/
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
    public RecyclerViewForFriends.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, viewGroup, false);
        RecyclerViewForFriends.ViewHolder viewHolder = new RecyclerViewForFriends.ViewHolder
                (contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewForFriends.ViewHolder viewHolder, final int position) {
        TextView frdsTxtVw = viewHolder.frdsTxtVw;
        ImageView frndsImgVw = viewHolder.frndsImageView;
        final Button addfriend = viewHolder.sendRequestBtn;
        frdsTxtVw.setText(allusers.get(position).getDisplayName());
        Picasso.with(gContext).load(allusers.get(position).getProfilePicUrl()).into(frndsImgVw);

        frdsTxtVw.setText(allusers.get(position).getDisplayName());
        addfriend.setTag(position);

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) addfriend.getTag();
                try {
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + allusers.get(tag).getKey() + "/Notifications").push();
                    DatabaseReference dbref1 = FirebaseDatabase.getInstance().getReference().child
                            ("Users/" + loggedInUserKey + "/SentNotifications").push();
                    dbref.setValue(loggedInUserKey);
                    dbref1.setValue(allusers.get(tag).getKey());
                    allusers.remove(tag);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
        Button sendRequestBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            frdsTxtVw = (TextView) itemView.findViewById(R.id.frdsTxtVw);
            frndsImageView = (ImageView) itemView.findViewById(R.id.friendsProfileImageView);
            sendRequestBtn = (Button) itemView.findViewById(R.id.addFriendbtn);
        }
    }
}
