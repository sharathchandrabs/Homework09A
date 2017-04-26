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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Suraj G Desai on 4/19/2017.
 */

public class UploadImageAsyncTask extends AsyncTask<byte[], Void, String> {
    IGetEpisodes mainActivity;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    String keyAndPhotoUrl = null;
    String imageRef;

    public UploadImageAsyncTask(IGetEpisodes mainActivity, String imageRef) {
        this.mainActivity = mainActivity;
        this.imageRef = imageRef;
    }

    public static interface IGetEpisodes {
        public void fetchEpisodes(String downloadUrl);
    }

    @Override
    protected String doInBackground(byte[]... params) {
        BufferedReader reader = null;
        try {
            ExecutorService taskExecutor = Executors.newFixedThreadPool(4);

            final StorageReference storageRef = firebaseStorage.getInstance().getReference();
            final String path = java.util.UUID.randomUUID() + ".png";
            final StorageReference mountainImagesRef = storageRef.child(imageRef + path);

            byte[] bytedata = params[0];
            UploadTask uploadTask = mountainImagesRef.putBytes(bytedata);

//            taskExecutor.

/*
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("fail", e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisualForTests") Uri uri1 = taskSnapshot.getDownloadUrl();
                    keyAndPhotoUrl = uri1.toString();
                }
            });*/

            while (uploadTask.isInProgress()) {
                Thread.sleep(2000);
                continue;
            }
            Thread.sleep(5000);
            if (uploadTask.isComplete())
                keyAndPhotoUrl = uploadTask.getResult().getDownloadUrl().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyAndPhotoUrl;
    }

    @Override
    protected void onPostExecute(String downloadUrl) {
        mainActivity.fetchEpisodes(downloadUrl);
    }
}
