package com.averton.Iplayer.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.VideoHelper;

import static com.averton.Iplayer.R.string.*;

public class ExoPlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;

    private String data = "";
    private String tag = "";
    private ImageView backButton;
    private ImageView forward;
    private ImageView backward;
    private TextView movieDuration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exo_movies_player);

        playerView = findViewById(R.id.exoplayer_view);
        backButton = findViewById(R.id.image_view_exo_player_back);
        forward = findViewById(R.id.image_view_exo_player_forward_10);
        backward =findViewById(R.id.image_view_exo_player_replay_10);
        movieDuration = findViewById(R.id.exo_duration);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initAction();
        initControls();
    }

    private void initAction() {
        String videoPath = "";

        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getStringExtra("videoPath");

            Log.d("video", data);
        }

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());

        playerView.setPlayer(exoPlayer);



        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, String.valueOf(app_name)));

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(data));
        concatenatingMediaSource.addMediaSource(mediaSource);
        exoPlayer.prepare(concatenatingMediaSource);
        exoPlayer.setPlayWhenReady(true);




    }
    private void initControls(){

        backButton.setOnClickListener(v -> onBackPressed());

        forward.setOnClickListener(v -> exoPlayer.seekTo(exoPlayer.getCurrentPosition()+10000));
        backward.setOnClickListener(v -> exoPlayer.seekTo(exoPlayer.getCurrentPosition()-10000));
        movieDuration.setText(VideoHelper.timeConverter((int) exoPlayer.getDuration()));

}




    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.release();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.release();
    }

}