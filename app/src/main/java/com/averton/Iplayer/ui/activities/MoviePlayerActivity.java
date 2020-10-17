package com.averton.Iplayer.ui.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Analytics;
import com.swipper.library.Swipper;

/*
*/

public class MoviePlayerActivity extends Swipper {



    private VideoView mVideoView;
    private TextView mBufferingTextView;
    private MediaPlayer musicPlayer;

    private Intent intent;
    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;
    private int activity;
    private Uri videoUri;
    private  String musicPath;
    private  String videoPath;
    private String videoName;
    private int noClicks;
    private Analytics analytics;
    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exo_player_place);
        VideoHelper.initPaperDb(this);


        intent = getIntent();
         musicPath = intent.getStringExtra("musicPath");
         videoPath = intent.getStringExtra("videoPath");
         activity = intent.getIntExtra("Activity",1);
         videoName = intent.getStringExtra("videoName");
         noClicks = intent.getIntExtra("noClick",0);

         set(this);
         Volume(Orientation.HORIZONTAL);
         Brightness(Orientation.VERTICAL);

        mVideoView = findViewById(R.id.videoView);
        mBufferingTextView = findViewById(R.id.buffering_textview);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // Set up the media controller widget and attach it to the video view.
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);



    }


    @Override
    protected void onStart() {
        super.onStart();
     initializePlayer(videoPath);
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();


        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private void initializePlayer(String path) {
        // Show the "Buffering..." message while the video loads.
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        // Buffer and decode the video sample.

         videoUri = getMedia(path);



        mVideoView.setVideoURI(videoUri);

        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView.setOnPreparedListener(
                mediaPlayer -> {

                    // Hide buffering message.
                    mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                    // Restore saved position, if available.
                    if (mCurrentPosition > 0) {
                        mVideoView.seekTo(mCurrentPosition);
                    } else {
                        // Skipping to 1 shows the first frame of the video.
                        mVideoView.seekTo(1);
                    }

                    // Start playing!
                    mVideoView.start();
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).
        mVideoView.setOnCompletionListener(
                mediaPlayer -> {
                    Toast.makeText(MoviePlayerActivity.this,
                            R.string.toast_message,
                            Toast.LENGTH_SHORT).show();

                    // Return the video position to the start.
                    mVideoView.seekTo(0);
                });
    }


    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();

    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private Uri getMedia(String mediaName) {
            // Media name is an external URL.
            return Uri.parse("file://"+mediaName);


    }
/*    private void analytics(){
        ArrayList<Analytics> analyticsArrayList = new ArrayList<>();

        analytics.setMediaType("Movies");
        analytics.setMediaName(videoName);
        analytics.setClickCount(noClicks);

        analyticsArrayList.add(analytics);

        VideoHelper.savePaperDb();
       // /data/data/com.virmana.flix/files/analytics/Movies.pt
        VideoHelper.writePaperDb("Movies",analyticsArrayList);

     //   Toasty.info(this,"Save to "+Paper.book().getPath("Movies"),Toasty.LENGTH_SHORT).show();
        Log.v("Book path",Paper.book().getPath("Movies"));

    }*/
}


