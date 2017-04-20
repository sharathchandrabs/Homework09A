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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateTripActivity extends AppCompatActivity implements UploadImageAsyncTask.IGetEpisodes{
    ImageView tripPhoto;
    EditText tripTitle;
    EditText tripLocation;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    Button tripDone;
    Button tripCancel;
    Bitmap globalTripPhoto = null;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    public int SELECT_IMAGE = 100;

    Trip addTrip = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        tripPhoto = (ImageView) findViewById(R.id.tripImageVw);
        tripTitle = (EditText) findViewById(R.id.tripTitle);
        tripLocation = (EditText) findViewById(R.id.tripLocation);
        tripDone = (Button) findViewById(R.id.tripDonebtn);
        tripCancel = (Button) findViewById(R.id.tripCanclebtn);

        addTrip = new Trip();
        tripPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        tripDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tripTitle.getText().toString().equals("") || tripLocation.getText().toString().equals("")) {
                    Toast.makeText(CreateTripActivity.this, "Title or Location missing!", Toast.LENGTH_LONG).show();
                } else {
                    if (globalTripPhoto == null) {
                        addTrip.setTitle(tripTitle.getText().toString());
                        addTrip.setLocation(tripLocation.getText().toString());
                    } else {
                        DateFormat aDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Date date = new Date();
                        addTrip.setTitle(tripTitle.getText().toString());
                        addTrip.setLocation(tripLocation.getText().toString());
                        addTrip.setCreatedDate(date);
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CreateTripActivity.this);
                        addTrip.setCreatedBy(sharedPreferences.getString("userKey", null));
                        final StorageReference storageRef = firebaseStorage.getInstance().getReference();
                        final String path = java.util.UUID.randomUUID() + ".png";
                        final StorageReference mountainImagesRef = storageRef.child("TripProfilePhoto/" + path);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        globalTripPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytedata = baos.toByteArray();
                        try{
                            new UploadImageAsyncTask(CreateTripActivity.this, "TripProfilePhoto/").execute(bytedata);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE && data.getData() != null) {
                Uri uri = data.getData();

                try {

                    globalTripPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    tripPhoto.setImageBitmap(globalTripPhoto);
                    // Log.d(TAG, String.valueOf(bitmap));
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
        addTrip.setTripProfilePhotoUrl(downloadUrl);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
        String tripsKey = databaseReference.push().getKey();
        addTrip.setTripKey(tripsKey);
        databaseReference.child(tripsKey).setValue(addTrip);
        Toast.makeText(this, "Trip Successfully created!", Toast.LENGTH_LONG);
        finish();
    }
}
