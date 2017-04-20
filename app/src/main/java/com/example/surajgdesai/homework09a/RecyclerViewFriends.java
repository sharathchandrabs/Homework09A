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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suraj G Desai on 4/18/2017.
 */

public class RecyclerViewFriends extends RecyclerView.Adapter<RecyclerViewFriends.ViewHolder> {

    Context gContext;
    List<User> gObjects;
    int currentLayout;
    SharedPreferences sharedPreferences;
    String loggedInUserKey;


    public RecyclerViewFriends(Context gContext, List<User> gObjects, int currentLayout) {
        this.gContext = gContext;
        this.gObjects = gObjects;
        this.currentLayout = currentLayout;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(gContext);
        loggedInUserKey = sharedPreferences.getString("userKey",null);
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return gContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, parent, false);
        ViewHolder viewHolder = new ViewHolder(contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView frdsTxtVw = holder.frdsTxtVw;
        ImageView frndsImgVw = holder.frndsImageView;
        final Button addfriend = holder.sendRequestBtn;
        frdsTxtVw.setText(gObjects.get(position).getDisplayName());
        Picasso.with(gContext).load(gObjects.get(position).getProfilePicUrl()).into(frndsImgVw);

        frdsTxtVw.setText(gObjects.get(position).getDisplayName());
        addfriend.setTag(position);

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) addfriend.getTag();
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child
                        ("Users/"+loggedInUserKey+"/Friends").push();
                dbref.setValue(gObjects.get(tag).getKey());
                addfriend.setVisibility(View.INVISIBLE);



            }
        });

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
        }
    }

}
