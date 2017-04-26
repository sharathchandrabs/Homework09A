package com.example.surajgdesai.homework09a;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shara on 4/13/2017.
 */

public class FriendsFragment extends Fragment {

//    IGetFriends getFriendsListener;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SearchView searchView;
    RecyclerView friendsFragmentRecycler;
    ArrayList<User> usersFriends;

    String currentUser;
    /*public FriendsFragment(IGetFriends getFriendsListener) {
        this.getFriendsListener = getFriendsListener;
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.friends_fragment, container, false);
        FloatingActionButton fb = (FloatingActionButton) root.findViewById(R.id.fabFriends);
        friendsFragmentRecycler = (RecyclerView) root.findViewById(R.id.friendsFragmentRV);
        usersFriends = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentUser = sharedPreferences.getString("userKey", null);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendRequestActivity.class);
                getActivity().startActivity(intent);
            }
        });
        root.findViewById(R.id.fabFriendRequests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AcceptRequestsActivity.class));
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        friendsFragmentRecycler = (RecyclerView) root.findViewById(R.id.friendsFragmentRV);
        RecyclerViewFriendsFragment adapter = new RecyclerViewFriendsFragment(getActivity(),
                currentUser, R.layout.friends_layout);
        friendsFragmentRecycler.setAdapter(adapter);
        friendsFragmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();


        return root;
    }
}
