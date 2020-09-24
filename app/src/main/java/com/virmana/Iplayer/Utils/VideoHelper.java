package com.virmana.Iplayer.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.virmana.Iplayer.entity.Analytics;
import com.virmana.Iplayer.entity.Music;
import com.virmana.Iplayer.entity.Video;
import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;
import timber.log.Timber;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class VideoHelper {




    public static String makeDirs(Context context, String fileName){
        File videoDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),fileName);
        String path;
        path = videoDir.toString();
        if (!videoDir.exists()) {
            videoDir.mkdirs();
        }
        return path;
    }

    public static String timeConverter(int timeMs){

        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder, Locale.ENGLISH);
        int totalSeconds = timeMs/1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds/60) % 60;
        int hours = totalSeconds/3600;
        builder.setLength(0);

        if(hours > 0){

            return formatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else{return formatter.format("%02d:%02d",minutes,seconds).toString();}



    }





    public void showDialog(final String msg, final Context context
                           ) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton("Goto Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",context.getPackageName(),null);
            intent.setData(uri);
            context.startActivity(intent);
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
           dialog.cancel();
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public static void initPaperDb(Context context){
        Paper.init(context);

    }

    public static void writePaperDb(String key, ArrayList<Analytics> analytics ){
        Paper.book("analytics").write(key,analytics);

    }
public static void savePaperDb(){
        Paper.bookOn("/mnt/extSdCard/AVERTON/MovieDesc","analytics");
}


}
