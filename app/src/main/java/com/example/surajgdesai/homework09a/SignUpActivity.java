package com.example.surajgdesai.homework09a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements UploadImageAsyncTask.IGetEpisodes {

    FirebaseAuth firebaseAuth;
    EditText fnameedt, lnameedt, emailedt, pwdedt, cnfpwdedt;
    Spinner genderSpinner;
    ImageView pickProfileImgVw;
    DatabaseReference refDatabase;
    FirebaseDatabase refDb;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    StorageReference storageRef;
    Bitmap profilePhoto = null;
    String fname, lname, email, pwd, cnfedt, gender;
    final String boyAvatar = "Avatars/Boy avatar.PNG";
    final String girlAvatar = "Avatars/Girl avatar.PNG";
    public int SELECT_IMAGE = 100;
    FirebaseUser firebaseuser;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        genderSpinner = (Spinner) findViewById(R.id.genderedt);

        ArrayAdapter<String> gameKindArray = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout
                .simple_spinner_item, R.array.GenderArray);

        genderSpinner.setAdapter(gameKindArray);

        refDb = FirebaseDatabase.getInstance();
        refDatabase = refDb.getReference().child("Users");

        storageRef = FirebaseStorage.getInstance().getReference();

        fnameedt = (EditText) findViewById(R.id.fnameedt);
        lnameedt = (EditText) findViewById(R.id.lnameedt);
        emailedt = (EditText) findViewById(R.id.emailedt);
        pwdedt = (EditText) findViewById(R.id.pwdedtsignup);
        cnfpwdedt = (EditText) findViewById(R.id.cnfpwdedtsignup);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setCancelable(false);

        pickProfileImgVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        findViewById(R.id.signupbtnsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fname = fnameedt.getText().toString().trim();
                lname = lnameedt.getText().toString().trim();
                email = emailedt.getText().toString().trim();
                pwd = pwdedt.getText().toString().trim();
                cnfedt = cnfpwdedt.getText().toString().trim();
                gender = genderSpinner.getSelectedItem().toString();
                firebaseAuth = FirebaseAuth.getInstance();
                if (!(fname.equals("") || lname.equals("") || email.equals("") || pwd.equals("")
                        || cnfedt.equals("") || gender.equals("--Select--"))) {
                    if (pwd.equals(cnfedt)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.NotSignedIn),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseuser = firebaseAuth.getCurrentUser();
                                    try {
                                        String uid = firebaseuser.getUid();
                                        user = new User();
                                        user.setDisplayName(fname + " " + lname);
                                        user.setFname(fname);
                                        user.setLname(lname);
                                        user.setGender(gender);
                                        user.setKey(uid);
                                        user.setEmail(email);

                                        if (profilePhoto == null) {
                                            StorageReference imageRef = storageRef.child(gender.equals("Male") ? boyAvatar : girlAvatar);
                                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        try {
                                                            user.setProfilePicUrl(task.getResult().toString());
                                                            final Map<String, User> userMap = new HashMap<String, User>();
                                                            userMap.put(user.getKey(), user);
                                                            refDatabase.child(user.getKey()).setValue
                                                                    (user);
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(user.getDisplayName())
                                                                    .setPhotoUri(Uri.parse(user.getProfilePicUrl()))
                                                                    .build();
                                                            firebaseuser.updateProfile(profileUpdates);
                                                            prefEditor.putString("userKey", user.getKey());
                                                            prefEditor.commit();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
                                        } else {
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            profilePhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                            byte[] bytedata = baos.toByteArray();
                                            new UploadImageAsyncTask(SignUpActivity.this, "TripProfilePhoto/")
                                                    .execute(bytedata);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Signup exception", e.getMessage().toString());
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.PasswordCnfShouldbesame), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.ProvideValidInput), Toast.LENGTH_SHORT).show();
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
                    profilePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    pickProfileImgVw.setImageBitmap(profilePhoto);
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
        user.setProfilePicUrl(downloadUrl);
        final Map<String, User> userMap = new HashMap<String, User>();
        userMap.put(user.getKey(), user);
        refDatabase.child(user.getKey()).setValue
                (user);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getDisplayName())
                .setPhotoUri(Uri.parse(user.getProfilePicUrl()))
                .build();
        firebaseuser.updateProfile(profileUpdates);
        prefEditor.putString("userKey", user.getKey());
        prefEditor.commit();

    }
}
