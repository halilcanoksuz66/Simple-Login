package com.example.vizeproje;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Anasayfa extends AppCompatActivity {
    ImageButton imageButtonAnasayfa;
    Button btn, browseVideoAnasayfa, uploadVideoAnasayfa;
    VideoView videoViewAnasayfa;
    Uri videoUri; // *
    MediaController mediaController; //*
    ProgressBar progressBarHorizantal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        btn = findViewById(R.id.button);
        browseVideoAnasayfa = findViewById(R.id.browseVideoAnasayfa);
        uploadVideoAnasayfa = findViewById(R.id.uploadVideoAnasayfa);
        progressBarHorizantal = findViewById(R.id.progressBarHorizantal);
        imageButtonAnasayfa = findViewById(R.id.imageButtonAnasayfa);
        videoViewAnasayfa = findViewById(R.id.videoViewAnasayfa);
        mediaController = new MediaController(this);
        videoViewAnasayfa.setMediaController(mediaController);
        videoViewAnasayfa.start();

        browseVideoAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_MEDIA_VIDEO)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent myIntent = new Intent();
                                myIntent.setType("video/*");
                                myIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(myIntent, "Select Video"), 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                // İzinler reddedilirse, kullanıcıya bir mesaj gösterin
                                Toast.makeText(Anasayfa.this, "İzin reddedildi", Toast.LENGTH_LONG).show();
                            }


                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                // İzinler reddedilirse, kullanıcıya bir açıklama gösterin
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarHorizantal.incrementProgressBy(10);
            }
        });


        imageButtonAnasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myI = new Intent(Anasayfa.this, Profile.class);
                startActivity(myI);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            videoUri = data.getData();
            videoViewAnasayfa.setVideoURI(videoUri);
        }
    }
}