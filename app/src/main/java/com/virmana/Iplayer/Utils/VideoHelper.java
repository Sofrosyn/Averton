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
import com.virmana.Iplayer.entity.Music;
import com.virmana.Iplayer.entity.Video;
import es.dmoral.toasty.Toasty;
import timber.log.Timber;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class VideoHelper {

    public boolean isExternalStoragePresent() {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {


        }
        return (mExternalStorageAvailable) && (mExternalStorageWriteable);
    }



    public void makeDir(Context context, String fileName){
    File videoDir = new File(context.getFilesDir()+File.separator +"/Iplayer"+ fileName);

    if (!videoDir.exists()) {
    videoDir.mkdirs();
    }
             }

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

    public  List<Music> getAllAudioFromDevice(Context context) {

      List<Music>  musicList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,MediaStore.Audio.AudioColumns.TRACK
        };
        Cursor c = context.getContentResolver().query(uri, projection,null, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                Music audioModel = new Music();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
                String track =c.getString(3);

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setArtistName(name);
                audioModel.setArtistSong(track);
              //  audioModel.setArtistPath(path);

                Timber.v(" Album :%s", album);
                Timber.v(" Artist :%s", artist);
                Timber.v(" Track :%s", track);

                musicList.add(audioModel);
            }
            c.close();
        }

        return musicList;
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
}
/*
    Video videoModel = new Video();
    String path = c.getString(6);
    String videoDuration = c.getString(1);
    String videoSize = c.getString(2);
    String videoTitle = c.getString(5);

    String vDuration = VideoHelper.timeConverter(Integer.parseInt(videoDuration));
    String name = path.substring(path.lastIndexOf("/") + 1);

                videoModel.setVideoName(name);
                        videoModel.setVideoPath(path);
                        videoModel.setVideoDuration(vDuration);
                        videoModel.setVideoGenre(videoSize);
//                audioModel.setArtistSong(album);
                        //  audioModel.setArtistPath(path);


                        arrayVideo.add(videoModel);
*/
