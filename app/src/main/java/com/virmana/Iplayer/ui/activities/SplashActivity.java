package com.virmana.Iplayer.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.orhanobut.hawk.Hawk;

import android.content.Intent;
import android.os.Bundle;


import com.greenfrvr.rubberloader.RubberLoaderView;
import com.virmana.Iplayer.Provider.PrefManager;
import com.virmana.Iplayer.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private PrefManager prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Hawk.init(this).build();

        prf= new PrefManager(getApplicationContext());

        ( (RubberLoaderView) findViewById(R.id.loader1)).startLoading();




        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {



        if (!prf.getString("first").equals("true")){
            Intent intent = new Intent(SplashActivity.this,IntroActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
            prf.setString("first","true");
        }else{
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }



                    }
                });
            }
        }, 300);
    }







}
