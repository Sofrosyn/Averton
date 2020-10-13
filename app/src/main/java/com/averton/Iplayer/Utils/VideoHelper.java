package com.averton.Iplayer.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.averton.Iplayer.entity.Analytics;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    public static String years(long timeMs){

        long days = TimeUnit.MILLISECONDS.toDays(timeMs);

        long years = days/365;

        final long date = 1970 + years ;

        return String.valueOf(date) ;


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



/*
public void writeToFile(Analytics analytic,String data) {
    List<Analytics> analytics = List

    analytics.add(analytic);
    try {
        File file = new File(Paths.analytics);
        Path path = Files.write(file.getPath(),);
    }catch (IOException e){e.getMessage();}
}*/


        public void toastMessage(Context context, String message){

            Toast toast= Toast.makeText(context,
                    message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }



}
