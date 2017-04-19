package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Suraj G Desai on 4/18/2017.
 */

public class RecyclerViewTrips extends RecyclerView.Adapter<RecyclerViewTrips.ViewHolder> {

    Context gContext;
    List<Trip> gObjects;
    int currentLayout;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contextView = layoutInflater.inflate(currentLayout, parent, false);
        RecyclerViewTrips.ViewHolder viewHolder = new RecyclerViewTrips.ViewHolder(contextView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EditText tripsTxtVw = holder.tripsTxtVw;
        Button joinBtn = holder.joinBtn;
        tripsTxtVw.setText(gObjects.get(position).getTitle());


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return gObjects.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        EditText tripsTxtVw;
        Button joinBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            tripsTxtVw = (EditText) itemView.findViewById(R.id.tripsTxtVw);
            joinBtn = (Button) itemView.findViewById(R.id.joinBtn);
        }
    }
}
