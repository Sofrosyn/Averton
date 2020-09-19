package com.virmana.Iplayer.ui.activities;

import android.Manifest;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;
import com.virmana.Iplayer.Utils.Method;
import com.virmana.Iplayer.Utils.StorageUtil;
import com.virmana.Iplayer.Utils.VideoHelper;
import es.dmoral.toasty.Toasty;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.greenfrvr.rubberloader.RubberLoaderView;
import com.virmana.Iplayer.Provider.PrefManager;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.api.apiClient;
import com.virmana.Iplayer.api.apiRest;
import com.virmana.Iplayer.config.Global;
import com.virmana.Iplayer.entity.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
            Intent intent = new Intent(SplashActivity.this,HomeActivitys.class);
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
