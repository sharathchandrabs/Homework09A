package com.example.surajgdesai.homework09a;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements UploadImageAsyncTask.IGetEpisodes {
    String currentUser;
    SharedPreferences preferences;
    Button doneBtn, cancelBtn;
    EditText fnameEdt, lnameEdt;
    ImageView profilePic;
    Spinner spinner;
    User user;
    Bitmap profilePhoto;
    public int SELECT_IMAGE = 100;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        fnameEdt = (EditText) findViewById(R.id.fnameedtEP);
        lnameEdt = (EditText) findViewById(R.id.lnameedtEP);
        profilePic = (ImageView) findViewById(R.id.pickProfileImgVwEP);
        spinner = (Spinner) findViewById(R.id.genderedtEP);

        ArrayAdapter<String> gameKindArray = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout
                .simple_spinner_item, getResources().getStringArray(R.array.GenderArray));

        spinner.setAdapter(gameKindArray);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentUser = preferences.getString("userKey", null);


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        userRef.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                fnameEdt.setText(user.getFname());
                lnameEdt.setText(user.getLname());

                if (user.getGender() == "Male") {
                    spinner.setSelection(0);
                } else {
                    spinner.setSelection(1);
                }

                Picasso.with(EditProfileActivity.this).load(user.getProfilePicUrl()).into(profilePic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        findViewById(R.id.signupbtnsignupEP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname, lname, gender;

                fname = fnameEdt.getText().toString().trim();
                lname = lnameEdt.getText().toString().trim();
                gender = spinner.getSelectedItem().toString();

                if (!(fname.equals("") || lname.equals("") || gender.equals("--Select--"))) {
                    if (profilePhoto == null) {
                        userRef.child(user.getKey()).child("fname").setValue(fname);
                        userRef.child(user.getKey()).child("lname").setValue(lname);
                        userRef.child(user.getKey()).child("gender").setValue(gender);
                    } else {
                        userRef.child(user.getKey()).child("fname").setValue(fname);
                        userRef.child(user.getKey()).child("lname").setValue(lname);
                        userRef.child(user.getKey()).child("gender").setValue(gender);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        profilePhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytedata = baos.toByteArray();
                        new UploadImageAsyncTask(EditProfileActivity.this, "Profile pictures/")
                                .execute(bytedata);
                    }

                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, getResources().getString(R.string.ProvideValidInput), Toast.LENGTH_SHORT).show();
                }


            }
        });

        findViewById(R.id.cancelbtnEP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    profilePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profilePic.setImageBitmap(profilePhoto);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void fetchEpisodes(String downloadUrl) {
        if (downloadUrl != null) {
            user.setProfilePicUrl(downloadUrl);
            final Map<String, User> userMap = new HashMap<String, User>();
            userMap.put(user.getKey(), user);
            userRef.child(user.getKey()).child("profilePicUrl").setValue
                    (downloadUrl);
        }
    }
}
