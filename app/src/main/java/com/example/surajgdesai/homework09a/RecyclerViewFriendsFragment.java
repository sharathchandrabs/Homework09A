package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by shara on 4/20/2017.
 */

public class RecyclerViewFriendsFragment extends RecyclerView.Adapter<RecyclerViewFriendsFragment.ViewHolder> {

    Context gContext;
    ArrayList<User> gObjects = new ArrayList<>();
    ArrayList<String> friendsList = new ArrayList<>();
    int currentLayout;
    SharedPreferences sharedPreferences;
    String loggedInUserKey;


     RecyclerViewFriendsFragment(Context gContext, String currentuser, int currentLayout) {
        this.gContext = gContext;
        this.currentLayout = currentLayout;
        loggedInUserKey = currentuser;
        DatabaseReference friendsDbRef = FirebaseDatabase.getInstance().getReference().child
                ("Users/"+loggedInUserKey+"/Friends");
        friendsDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gObjects.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    final String s = d.getValue(String.class);
                    notifyDataSetChanged();
                    DatabaseReference friendsUserList = FirebaseDatabase.getInstance().getReference().child
                            ("Users");
                    friendsUserList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d: dataSnapshot.getChildren()
                                    ) {
                                User u = d.getValue(User.class);
                                if(s.equals(d.getKey())) {
                                    gObjects.add(u);
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

            }
        });

    }



    @Override
    public RecyclerViewFriendsFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, parent, false);
        RecyclerViewFriendsFragment.ViewHolder viewHolder = new RecyclerViewFriendsFragment.ViewHolder(contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewFriendsFragment.ViewHolder holder, final int position) {
        TextView frdsTxtVw = holder.frdsTxtVw;
        ImageView frndsImgVw = holder.frndsImageView;
        final Button addfriend = holder.sendRequestBtn;
        frdsTxtVw.setText(gObjects.get(position).getDisplayName());
        Picasso.with(gContext).load(gObjects.get(position).getProfilePicUrl()).into(frndsImgVw);
        addfriend.setVisibility(View.INVISIBLE);
        frdsTxtVw.setText(gObjects.get(position).getDisplayName());


    }

    @Override
    public int getItemCount() {
        return gObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView frdsTxtVw;
        public ImageView frndsImageView;
        Button sendRequestBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            frdsTxtVw = (TextView) itemView.findViewById(R.id.frdsTxtVw);
            frndsImageView = (ImageView) itemView.findViewById(R.id.friendsProfileImageView);
            sendRequestBtn = (Button) itemView.findViewById(R.id.addFriendbtn);
            sendRequestBtn.setVisibility(View.INVISIBLE);
        }
    }

}

