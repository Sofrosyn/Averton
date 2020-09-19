package com.virmana.Iplayer.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.virmana.Iplayer.entity.Channel;
import com.virmana.Iplayer.entity.Poster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoadActivity extends AppCompatActivity {

    private PrefManager prf;
    IInAppBillingService mService;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";
    private static final String MERCHANT_ID=null;

    private  Integer id;
    private String type;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int time = 3000;
        Uri data = this.getIntent().getData();
        if (data==null){
            Bundle bundle = getIntent().getExtras() ;
            if (bundle!=null) {
                this.id = bundle.getInt("id");
                this.type = bundle.getString("type");
                time = 2000;
            }
        }else{
            if (data.getPath().contains("/c/share/")){
                this.id=Integer.parseInt(data.getPath().replace("/c/share/","").replace(".html",""));
                this.type = "channel";
                time = 2000;
            }else{
                this.id=Integer.parseInt(data.getPath().replace("/share/","").replace(".html",""));
                this.type = "poster";
                time = 2000;
            }
        }



        prf= new PrefManager(getApplicationContext());
        initBuy();
        ( (RubberLoaderView) findViewById(R.id.loader1)).startLoading();
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                LoadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        checkAccount();

                    }
                });
            }
        }, time);

        prf.setString("ADMIN_REWARDED_ADMOB_ID","");

        prf.setString("ADMIN_INTERSTITIAL_ADMOB_ID","");
        prf.setString("ADMIN_INTERSTITIAL_FACEBOOK_ID","");
        prf.setString("ADMIN_INTERSTITIAL_TYPE","FALSE");
        prf.setInt("ADMIN_INTERSTITIAL_CLICKS",3);

        prf.setString("ADMIN_BANNER_ADMOB_ID","");
        prf.setString("ADMIN_BANNER_FACEBOOK_ID","");
        prf.setString("ADMIN_BANNER_TYPE","FALSE");

        prf.setString("ADMIN_NATIVE_FACEBOOK_ID","");
        prf.setString("ADMIN_NATIVE_ADMOB_ID","");
        prf.setString("ADMIN_NATIVE_LINES","6");
        prf.setString("ADMIN_NATIVE_TYPE","FALSE");
    }


    private void checkAccount() {

        Integer version = -1;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (version!=-1){
            Integer id_user = 0;

            if (prf.getString("LOGGED").equals("TRUE")) {
                id_user = Integer.parseInt(prf.getString("ID_USER"));
            }
          /*  Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.check(version,id_user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    updateTextViews();
                    if (response.isSuccessful()){
                        for (int i = 0; i < response.body().getValues().size(); i++) {
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_REWARDED_ADMOB_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_REWARDED_ADMOB_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_INTERSTITIAL_ADMOB_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_INTERSTITIAL_ADMOB_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_INTERSTITIAL_FACEBOOK_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_INTERSTITIAL_FACEBOOK_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_INTERSTITIAL_TYPE") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_INTERSTITIAL_TYPE",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_INTERSTITIAL_CLICKS") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setInt("ADMIN_INTERSTITIAL_CLICKS",Integer.parseInt(response.body().getValues().get(i).getValue()));
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_BANNER_ADMOB_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_BANNER_ADMOB_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_BANNER_FACEBOOK_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_BANNER_FACEBOOK_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_BANNER_TYPE") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_BANNER_TYPE",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_NATIVE_FACEBOOK_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_NATIVE_FACEBOOK_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_NATIVE_ADMOB_ID") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_NATIVE_ADMOB_ID",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_NATIVE_LINES") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_NATIVE_LINES",response.body().getValues().get(i).getValue());
                            }
                            if ( response.body().getValues().get(i).getName().equals("ADMIN_NATIVE_TYPE") ){
                                if (response.body().getValues().get(i).getValue()!=null)
                                    prf.setString("ADMIN_NATIVE_TYPE",response.body().getValues().get(i).getValue());
                            }
                        }
                        if (response.body().getValues().get(1).getValue().equals("403")){
                            prf.remove("ID_USER");
                            prf.remove("SALT_USER");
                            prf.remove("TOKEN_USER");
                            prf.remove("NAME_USER");
                            prf.remove("TYPE_USER");
                            prf.remove("USERN_USER");
                            prf.remove("IMAGE_USER");
                            prf.remove("LOGGED");
                            Toasty.error(getApplicationContext(),getResources().getString(R.string.account_disabled), Toast.LENGTH_SHORT, true).show();
                        }
                        if (id!=null && type !=null){
                            if (type.equals("poster"))
                                getPoster();
                            if (type.equals("channel"))
                                getChannel();
                        }else{
                            if (response.body().getCode().equals(200)) {
                                if (!prf.getString("first").equals("true")){
                                    Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                    prf.setString("first","true");
                                }else{
                                    Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }
                            }else if (response.body().getCode().equals(202)) {
                                String title_update=response.body().getValues().get(0).getValue();
                                String featurs_update=response.body().getMessage();
                                View v = (View)  getLayoutInflater().inflate(R.layout.update_message,null);
                                TextView update_text_view_title=(TextView) v.findViewById(R.id.update_text_view_title);
                                TextView update_text_view_updates=(TextView) v.findViewById(R.id.update_text_view_updates);
                                update_text_view_title.setText(title_update);
                                update_text_view_updates.setText(featurs_update);
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(LoadActivity.this);
                                builder.setTitle("New Update")
                                        //.setMessage(response.body().getValue())
                                        .setView(v)
                                        .setPositiveButton(getResources().getString(R.string.update_now), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                final String appPackageName=getApplication().getPackageName();
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                                finish();
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.skip), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (!prf.getString("first").equals("true")){
                                                    Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                                    finish();
                                                    prf.setString("first","true");
                                                }else{
                                                    Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                                    finish();
                                                }
                                            }
                                        })
                                        .setCancelable(false)
                                        .setIcon(R.drawable.ic_update)
                                        .show();
                            } else {
                                if (!prf.getString("first").equals("true")){
                                    Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                    prf.setString("first","true");
                                }else{
                                    Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                }
                            }
                        }
                    }else {

                        if (id!=null && type !=null){
                            if (type.equals("poster"))
                                getPoster();
                            if (type.equals("channel"))
                                getChannel();
                        }else{
                            if (!prf.getString("first").equals("true")){
                                Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                prf.setString("first","true");
                            }else{
                                Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    updateTextViews();

                    if (id!=null && type !=null){
                        if (type.equals("poster"))
                            getPoster();
                        if (type.equals("channel"))
                            getChannel();
                    }else{
                        if (!prf.getString("first").equals("true")){
                            Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                            prf.setString("first","true");
                        }else{
                            Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }
                }
            });
*/
        }else{
            updateTextViews();

            if (id!=null && type !=null){
                if (type.equals("poster"))
                    getPoster();
                if (type.equals("channel"))
                    getChannel();
            }else{
                if (!prf.getString("first").equals("true")){
                    Intent intent = new Intent(LoadActivity.this,IntroActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                    prf.setString("first","true");
                }else{
                    Intent intent = new Intent(LoadActivity.this,HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }
        }

    }
    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if(!BillingProcessor.isIabServiceAvailable(this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, Global.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                updateTextViews();
            }
            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
                updateTextViews();
            }
            @Override
            public void onPurchaseHistoryRestored() {
                for(String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }

    private void updateTextViews() {
        PrefManager prf= new PrefManager(getApplicationContext());
        bp.loadOwnedPurchasesFromGoogle();
        if(isSubscribe(Global.SUBSCRIPTION_ID)){
            prf.setString("SUBSCRIBED","TRUE");
        }
        else{
            prf.setString("SUBSCRIBED","FALSE");
        }
    }
    public Boolean isSubscribe(String SUBSCRIPTION_ID_CHECK){

        if (!bp.isSubscribed(Global.SUBSCRIPTION_ID))
            return false;


        Bundle b =  getPurchases();
        if (b==null)
            return  false;
        if( b.getInt("RESPONSE_CODE") == 0){
            ArrayList<String> ownedSkus =
                    b.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String>  purchaseDataList =
                    b.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String>  signatureList =
                    b.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken =
                    b.getString("INAPP_CONTINUATION_TOKEN");


            if(purchaseDataList == null){
                return  false;

            }
            if(purchaseDataList.size()==0){
                return  false;
            }
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku_1 = ownedSkus.get(i);
                //Long tsLong = System.currentTimeMillis()/1000;

                try {
                    JSONObject rowOne = new JSONObject(purchaseData);
                    String  productId =  rowOne.getString("productId") ;

                    if (productId.equals(SUBSCRIPTION_ID_CHECK)){

                        Boolean  autoRenewing =  rowOne.getBoolean("autoRenewing");
                        if (autoRenewing){
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            return  true;
                        }else{
                            // Toast.makeText(this, "is not autoRenewing ", Toast.LENGTH_SHORT).show();
                            Long tsLong = System.currentTimeMillis()/1000;
                            Long  purchaseTime =  rowOne.getLong("purchaseTime")/1000;
                            //   Toast.makeText(this, "is Expired ", Toast.LENGTH_SHORT).show();
                            return tsLong <= (purchaseTime + (Global.SUBSCRIPTION_DURATION * 86400));
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }else{
            return false;
        }

        return  false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    public Bundle getPurchases(){
        if (!bp.isInitialized()) {
            return null;
        }
        try{

            return  mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        }catch (Exception e) {
            // Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return null;
    }

    public void getPoster(){

      /*  Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Poster> call = service.getPosterById(id);
        call.enqueue(new retrofit2.Callback<Poster>() {
            @Override
            public void onResponse(Call<Poster> call, Response<Poster> response) {
                if(response.isSuccessful()) {
                    if (response.body().getType().equals("serie")) {
                        Intent in = new Intent(LoadActivity.this, MusicActivity.class);
                        in.putExtra("poster", response.body());
                        in.putExtra("from", "true");
                        startActivity(in);
                        finish();
                    }if (response.body().getType().equals("movie")){
                        Intent in = new Intent(LoadActivity.this,MovieActivity.class);
                        in.putExtra("poster", response.body());
                        in.putExtra("from", "true");
                        startActivity(in);
                        finish();
                    }
                }
            }
            @Override
            public void onFailure(Call<Poster> call, Throwable t) {

            }
        });*/
    }
    public void getChannel(){

       /* Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Channel> call = service.geChannelById(id);
        call.enqueue(new retrofit2.Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                if(response.isSuccessful()) {
                    Intent in = new Intent(LoadActivity.this,ChannelActivity.class);
                    in.putExtra("channel", response.body());
                    in.putExtra("from", "true");
                    startActivity(in);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Channel> call, Throwable t) {

            }
        });*/
    }

}
