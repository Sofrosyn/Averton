package com.averton.Iplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.averton.Iplayer.ui.activities.HomeActivity;

public class ActivityRunOnStartUp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

        Intent intent1 = new Intent(context, HomeActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        }*/
    }
}
