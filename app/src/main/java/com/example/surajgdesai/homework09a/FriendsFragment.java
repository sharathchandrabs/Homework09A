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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by shara on 4/13/2017.
 */

public class FriendsFragment extends Fragment implements FetchUsersFriendsAsync.IGetEpisodes{



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    SearchView searchView;
    RecyclerView friendsFragmentRecycler;
    ArrayList<User> usersFriends;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.friends_fragment, container, false);
        FloatingActionButton fb = (FloatingActionButton) root.findViewById(R.id.fabFriends);
        friendsFragmentRecycler = (RecyclerView) root.findViewById(R.id.friendsFragmentRV);
        usersFriends = new ArrayList<>();
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(root, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent =  new Intent(getActivity(), FriendRequestActivity.class);
                getActivity().startActivity(intent);

            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userKey = sharedPreferences.getString("userKey", null);

        new FetchUsersFriendsAsync(FriendsFragment.this).execute(userKey);


        return root;
    }

    @Override
    public void fetchEpisodes(ArrayList<User> gameList) {
        Log.d("async",gameList.toString());
    }
}
