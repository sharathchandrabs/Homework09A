package com.example.surajgdesai.homework09a;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Suraj G Desai on 4/19/2017.
 */

public class UploadImageAsyncTask extends AsyncTask<byte[], Void, HashMap<String,String>> {
    IGetEpisodes mainActivity;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    HashMap<String,String> keyAndPhotoUrl = null;

    public UploadImageAsyncTask(IGetEpisodes mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static interface IGetEpisodes {
        public void fetchEpisodes(HashMap<String,String> gameList);
    }

    @Override
    protected HashMap<String,String> doInBackground(byte[]... params) {
        BufferedReader reader = null;
        try {
            final StorageReference storageRef = firebaseStorage.getInstance().getReference();
            final String path = java.util.UUID.randomUUID() + ".png";
            final StorageReference mountainImagesRef = storageRef.child("TripProfilePhoto/" + path);

            byte[] bytedata = params[0];
            UploadTask uploadTask = mountainImagesRef.putBytes(bytedata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("fail",e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisualForTests") Uri uri1 = taskSnapshot.getDownloadUrl();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Trips");
                    String tripsKey = databaseReference.push().getKey();
                    keyAndPhotoUrl = new HashMap<String, String>();
                    keyAndPhotoUrl.put("photokey", tripsKey);
                    keyAndPhotoUrl.put("photourl",uri1.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return keyAndPhotoUrl;
    }

    @Override
    protected void onPostExecute(HashMap<String,String> gameList) {
        mainActivity.fetchEpisodes(gameList);
    }
}
