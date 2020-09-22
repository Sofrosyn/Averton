package com.virmana.Iplayer.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class MetadataExtractor {

    private MediaMetadataRetriever metadataRetriever;
    private byte[] albumCover;
    private Bitmap image;

    public byte[] getCoverArt(String path){
        metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(path);

        try{

            albumCover = metadataRetriever.getEmbeddedPicture();
 //           image = BitmapFactory.decodeByteArray(albumCover,0,albumCover.length);


        }catch(Exception e){

            e.printStackTrace();

        }
        return albumCover;
    }

}
