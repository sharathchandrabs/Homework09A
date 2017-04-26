package com.example.surajgdesai.homework09a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    int selectedTab = 0;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    TabLayout tabLayout;

    ProgressDialog progressDialog;

    DatabaseReference mdBRef;
    ArrayList<User> usersFriendsList;
    ArrayList<String> userfriends;
    FirebaseUser fireBaseUser;
    String currentUser;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.AppTitle);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        currentUser = preferences.getString("userKey", null);
        mdBRef = FirebaseDatabase.getInstance().getReference().child("Users/" + currentUser + "/Friends");
        usersFriendsList = new ArrayList<>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent editintent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                startActivity(editintent);
                break;

            case R.id.signoutMenu:
                prefEditor.clear();
                prefEditor.commit();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public FriendsFragment friendsFragment;
        public TripsFragment tripsFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    tripsFragment = new TripsFragment();
                    selectedTab = 0;
                    return tripsFragment;
                case 1:
                    friendsFragment = new FriendsFragment();
                    selectedTab = 1;
                    return friendsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Trips";
                case 1:
                    return "Friends";
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(selectedTab).select();
        currentUser = preferences.getString("userKey", null);
        //mSectionsPagerAdapter.friendsFragment.setUsersFriends(gameList);
        /*getUserFrds(currentUser);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }
}
