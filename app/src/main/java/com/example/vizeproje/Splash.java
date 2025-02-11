package com.example.vizeproje;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.google.firebase.FirebaseApp;

public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressBar progressBarSplash = (ProgressBar) findViewById(R.id.progressBarSplash);
        progressBarSplash.setMax(SPLASH_TIME_OUT); // Progress bar'ın maksimum değeri, SPLASH_TIME_OUT kadar olmalıdır.

        VideoView videoView = (VideoView) findViewById(R.id.videoViewSplash);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo);
        videoView.setVideoURI(videoUri);
        videoView.start();

        // Splash ekranını kapat ve Login ekranını aç
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish(); // Aktiviteden Splash ekranını kaldır
            }
        }, SPLASH_TIME_OUT);
    }
}
