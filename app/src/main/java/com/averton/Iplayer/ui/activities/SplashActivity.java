package com.averton.Iplayer.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.averton.Iplayer.Utils.Paths;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


import com.greenfrvr.rubberloader.RubberLoaderView;
import com.averton.Iplayer.Provider.PrefManager;
import com.averton.Iplayer.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    private CardView enter;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        enter = findViewById(R.id.enterButton);
        imageView = findViewById(R.id.splash_images);



        Glide.with(this).asGif().load(R.drawable.animate).into(imageView);


    }


    @Override
    protected void onStart() {
        super.onStart();

        enter.setOnClickListener(v -> {
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();

        });
    }
}


































