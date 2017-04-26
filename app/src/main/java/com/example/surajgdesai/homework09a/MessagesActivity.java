package com.example.surajgdesai.homework09a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class MessagesActivity extends AppCompatActivity implements UploadImageAsyncTask.IGetEpisodes {

    RecyclerView messageRcyclr;
    ImageView sendText, sendImage;
    EditText messageText;
    DatabaseReference messagesRef;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefEditor;
    public int SELECT_IMAGE = 100;
    Bitmap messageImage;
    ProgressDialog progressDialog;
    String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Toolbar toolbar = (Toolbar) findViewById(R.id.msgtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.AppTitle);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefEditor = sharedPreferences.edit();

        messageRcyclr = (RecyclerView) findViewById(R.id.messagesRV);
        String tripKey = getIntent().getExtras().getString("TripKey");
        userKey = sharedPreferences.getString("userKey", null);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        RecyclerViewMessages adapter = new RecyclerViewMessages(this, R.layout.messages_layout, tripKey);
        messageRcyclr.setAdapter(adapter);
        messageRcyclr.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

        sendText = (ImageView) findViewById(R.id.msgSendIV);
        sendImage = (ImageView) findViewById(R.id.galleryIV);

        messageText = (EditText) findViewById(R.id.msgContentET);
        messagesRef = FirebaseDatabase.getInstance().getReference().child("Trips/" + tripKey + "/Messages");

        sendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = messageText.getText().toString().trim();
                if (!messageTxt.equals("")) {

                    Message message = new Message();
                    message.setTextOrPhoto(messageTxt);
                    message.setCreatedBy(userKey);
                    message.setPic(false);
                    message.setCreatedDate(new Date());
                    String messageKey = messagesRef.push().getKey();
                    message.setKey(messageKey);
                    messagesRef.child(messageKey).setValue(message);
                } else {
                    Toast.makeText(MessagesActivity.this, "Enter some text and then click on send button", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
                progressDialog.show();
            }
        });
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
                Intent editintent = new Intent(MessagesActivity.this, EditProfileActivity.class);
                startActivity(editintent);
                break;

            case R.id.signoutMenu:
                prefEditor.clear();
                prefEditor.commit();
                Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    messageImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    messageImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] bytedata = baos.toByteArray();
                    new UploadImageAsyncTask(MessagesActivity.this, "MessageImages/").execute(bytedata);
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
        progressDialog.dismiss();
        Message message = new Message();
        message.setTextOrPhoto(downloadUrl);
        message.setCreatedBy(userKey);
        message.setPic(true);
        message.setCreatedDate(new Date());
        String messageKey = messagesRef.push().getKey();
        messagesRef.child(messageKey).setValue(message);
    }
}
