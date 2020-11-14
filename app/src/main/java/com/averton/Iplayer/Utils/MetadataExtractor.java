package com.averton.Iplayer.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MetadataExtractor {

    private MediaMetadataRetriever metadataRetriever;
    private byte[] albumCover;
    private Bitmap bitmap;

    public byte[] getCoverArt(String path){
        metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(path);

        try{

            albumCover = metadataRetriever.getEmbeddedPicture();

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







}//end of line
