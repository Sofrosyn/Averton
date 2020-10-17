package com.averton.Iplayer.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MetadataExtractor {

    private MediaMetadataRetriever metadataRetriever;
    private byte[] albumCover;


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

    public void generatePdfThumbnail(ImageView imageView, Context context, Uri pdfUri ) throws FileNotFoundException {
        ParcelFileDescriptor pd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
        int PageNum = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            PdfDocument pdfDocument = pdfiumCore.newDocument(pd);
            pdfiumCore.openPage(pdfDocument,PageNum);

            int width = pdfiumCore.getPageWidthPoint(pdfDocument,PageNum);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument,PageNum);
            Bitmap bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);

            pdfiumCore.renderPageBitmap(pdfDocument,bitmap,PageNum,0,0,width,height);


            Glide.with(context).asBitmap().load(bitmap).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);

            pdfiumCore.closeDocument(pdfDocument);

        }catch (IOException e){
            e.printStackTrace();

        }



    }


}
