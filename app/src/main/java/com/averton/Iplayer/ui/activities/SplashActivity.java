package com.averton.Iplayer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.averton.Iplayer.R;

public class SplashActivity extends AppCompatActivity {


    private CardView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);




        setContentView(R.layout.activity_splash);

        enter = findViewById(R.id.enterButton);


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


































