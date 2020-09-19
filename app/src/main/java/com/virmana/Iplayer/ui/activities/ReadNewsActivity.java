package com.virmana.Iplayer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.Utils.Log;
import timber.log.Timber;

import java.io.File;

public class ReadNewsActivity extends AppCompatActivity {

    private PDFView pdfView;
    private Intent intent;
    private String news;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        pdfView = findViewById(R.id.pdf_view_reader);
            intent = getIntent();
        if (intent!= null) {
                news = intent.getStringExtra("newsPath");
                Timber.v("read news %s", news);
            displayNews(news);
            }else{   Timber.v("read news %s", "nothing shown");}

    }

    private void displayNews(String news){
        pdfView.fromFile(new File(news))
                .enableSwipe(true)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }
}
