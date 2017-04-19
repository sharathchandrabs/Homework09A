package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Suraj G Desai on 4/18/2017.
 */

public class RecyclerViewFriends extends RecyclerView.Adapter<RecyclerViewFriends.ViewHolder> {

    Context gContext;
    List<User> gObjects;
    int currentLayout;

    public RecyclerViewFriends(Context gContext, List<User> gObjects, int currentLayout) {
        this.gContext = gContext;
        this.gObjects = gObjects;
        this.currentLayout = currentLayout;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView frdsTxtVw = holder.frdsTxtVw;
        ImageView frndsImgVw = holder.frndsImageView;
        frdsTxtVw.setText(gObjects.get(position).getDisplayName());
        Picasso.with(gContext).load(gObjects.get(position).getProfilePicUrl()).into(frndsImgVw);
    }

    @Override
    public int getItemCount() {
        return gObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView frdsTxtVw;
        public ImageView frndsImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            frdsTxtVw = (TextView) itemView.findViewById(R.id.frdsTxtVw);
            frndsImageView = (ImageView) itemView.findViewById(R.id.friendsProfileImageView);
        }
    }

}
