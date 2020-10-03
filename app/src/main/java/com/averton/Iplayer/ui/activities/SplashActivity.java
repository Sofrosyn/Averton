package com.averton.Iplayer.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.averton.Iplayer.Utils.Paths;
import com.orhanobut.hawk.Hawk;

import android.content.Intent;
import android.os.Bundle;


import com.greenfrvr.rubberloader.RubberLoaderView;
import com.averton.Iplayer.Provider.PrefManager;
import com.averton.Iplayer.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private PrefManager prf;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Hawk.init(this).build();


        prf= new PrefManager(getApplicationContext());
    try {
    if(!file.exists()){
        file = new File(Paths.analytics);
    }

        }catch(Exception e){
    e.getMessage();
            }



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
            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
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
