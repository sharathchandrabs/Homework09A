package com.example.surajgdesai.homework09a;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText fnameedt, lnameedt, emailedt, pwdedt, cnfpwdedt;
    DatabaseReference refDatabase;
    FirebaseDatabase refDb;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = preferences.edit();

        refDb = FirebaseDatabase.getInstance();
        refDatabase = refDb.getReference().child("Users");

        fnameedt = (EditText) findViewById(R.id.fnameedt);
        lnameedt = (EditText) findViewById(R.id.lnameedt);
        emailedt = (EditText) findViewById(R.id.emailedt);
        pwdedt = (EditText) findViewById(R.id.pwdedtsignup);
        cnfpwdedt = (EditText) findViewById(R.id.cnfpwdedtsignup);


        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        final String boyAvatar = "Avatars/Boy avatar.PNG";
        final String girlAvatar = "Avatars/Girl avatar.PNG";
        findViewById(R.id.signupbtnsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String fname, lname, email, pwd, cnfedt, gender = "Male";
                fname = fnameedt.getText().toString().trim();
                lname = lnameedt.getText().toString().trim();
                email = emailedt.getText().toString().trim();
                pwd = pwdedt.getText().toString().trim();
                cnfedt = cnfpwdedt.getText().toString().trim();
                firebaseAuth = FirebaseAuth.getInstance();
                if (!(fname.equals("") || lname.equals("") || email.equals("") || pwd.equals("") || cnfedt.equals(""))) {
                    if (pwd.equals(cnfedt)) {
                        firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.NotSignedIn),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                                    progressDialog.setCancelable(false);
                                    final FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();
                                    String uid = firebaseuser.getUid();
                                    final User user = new User();
                                    user.setDisplayName(fname + " " + lname);
                                    user.setFname(fname);
                                    user.setLname(lname);
                                    user.setGender(gender);
                                    user.setKey(uid);
                                    user.setEmail(email);
                                    if (firebaseuser != null) {
                                        final StorageReference imageRef = storageRef.child(gender.equals("Male") ? boyAvatar : girlAvatar);
                                        progressDialog.show();
                                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    try {
                                                        progressDialog.dismiss();
                                                        user.setProfilePicUrl(task.getResult().toString());
                                                        final Map<String, User> userMap = new HashMap<String, User>();
                                                        userMap.put(user.getKey(), user);
                                                        refDatabase.setValue(userMap);
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
                                                } else {
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
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
}
