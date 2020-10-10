package com.averton.Iplayer.Utils;

import android.graphics.Bitmap;
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

            metadataRetriever.release();
        }catch(Exception e){
            metadataRetriever.release();
            e.printStackTrace();

        }
        return albumCover;

    }


    public String yearCreated(String path){
        String year = "";
       MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(path);
        return   metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);


    }


}
