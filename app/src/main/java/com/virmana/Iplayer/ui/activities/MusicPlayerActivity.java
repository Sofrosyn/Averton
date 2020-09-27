package com.virmana.Iplayer.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;


import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

import com.google.android.exoplayer2.upstream.FileDataSource;

import com.virmana.Iplayer.R;
import com.virmana.Iplayer.Utils.MetadataExtractor;

import com.virmana.Iplayer.Utils.Paths;
import com.virmana.Iplayer.Utils.VideoHelper;
import com.virmana.Iplayer.entity.Music;

import com.virmana.Iplayer.ui.Adapters.PlaylistAdapter;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;



public class MusicPlayerActivity extends AppCompatActivity {



    private SimpleExoPlayer exoPlayer;
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.i(TAG,"onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i(TAG,"onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG,"onPlayerStateChanged: playWhenReady = "+ playWhenReady
                    +" playbackState = "+playbackState);
            switch (playbackState){
                case ExoPlayer.STATE_ENDED:
                    Log.i(TAG,"Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause(false);
                    exoPlayer.seekTo(0);
                    break;
                case ExoPlayer.STATE_READY:
                    Log.i(TAG,"ExoPlayerActivity ready! pos: "+exoPlayer.getCurrentPosition()
                            +" max: "+VideoHelper.timeConverter((int) exoPlayer.getDuration()));
                    setProgress();
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    Log.i(TAG,"Playback buffering!");
                    break;
                case ExoPlayer.STATE_IDLE:
                    Log.i(TAG,"ExoPlayerActivity idle!");
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i(TAG,"onPlaybackError: "+error.getMessage());
        }

    };

    private SeekBar seekPlayerProgress;
    private Handler handler;
    private ImageButton btnPlay;
    private TextView txtCurrentTime, txtEndTime;
    private boolean isPlaying = false;
    private Intent intent;

    private static final String TAG = "MainActivity";
    private String musicPath;
    private String tag;
    private ImageView thumbnail;
    private MetadataExtractor extractor;
    private ArrayList<Music> playlist;
    private PlaylistAdapter playlistAdapter;
    private RecyclerView playlistRecyclerView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_music);
        toolbar = findViewById(R.id.music_activity_toolBar);
        toolbar.setTitle("Music Player");
        setSupportActionBar(toolbar);

            initViews();
        initPlaylist();
          prepareExoPlayerFromFileUri(Uri.parse(musicPath));
        setPlayPause(!isPlaying);



    }


    private void prepareExoPlayerFromFileUri(Uri uri){
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        DataSpec dataSpec = new DataSpec(uri);
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = () -> fileDataSource;


        MediaSource audioSource = new ExtractorMediaSource(fileDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);


        exoPlayer.prepare(audioSource);
        initMediaControls();
    }


   private void initViews(){

       intent = getIntent();
       musicPath = intent.getStringExtra("musicPath");
       tag = intent.getStringExtra("tag");



       playlistRecyclerView = findViewById(R.id.music_activity_playlist);
        extractor = new MetadataExtractor();
       thumbnail = findViewById(R.id.musi_player_thumbnail);

       RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.video_art)
               .error(R.drawable.video_art).fitCenter();

       Glide.with(this).setDefaultRequestOptions(requestOptions).load(extractor.getCoverArt(musicPath)).into(thumbnail);

   }

    private void initPlaylist(){

        switch (tag){

            case "afrobeats":
                fetchMusicByPath(playlistRecyclerView, Paths.musicAfrobeats);
                break;
            case "soul":
                fetchMusicByPath(playlistRecyclerView,Paths.musicSouls);
                break;
            case "hiphop":
                fetchMusicByPath(playlistRecyclerView,Paths.musichipHop);
                break;
            case "billboards":
                fetchMusicByPath(playlistRecyclerView, Paths.musicBillBoard);
                break;
            case "trending":
                fetchMusicByPath(playlistRecyclerView,Paths.musicTrending);
                break;
            case "rnb":
                fetchMusicByPath(playlistRecyclerView,Paths.musicRnB);
                break;
            case "toprated":
                fetchMusicByPath(playlistRecyclerView,Paths.musicAfricaTopRated);
                break;
        }








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()){
             case R.id.nav_playlist_checked:
            //     Toasty.info(this,"itemclicked",Toasty.LENGTH_SHORT).show();
                  playlistRecyclerView.setVisibility(View.VISIBLE);

                  break;
             case R.id.nav_playlist_unchecked:
                 playlistRecyclerView.setVisibility(View.INVISIBLE);
                 break;
         }
         return true;
    }

    private void initMediaControls() {
        initPlayButton();
        initSeekBar();
        initTxtTime();
    }

    private void initPlayButton() {
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(view -> setPlayPause(!isPlaying));
    }

    /**
     * Starts or stops playback. Also takes care of the Play/Pause button toggling
     * @param play True if playback should be started
     */
    private void setPlayPause(boolean play){
        isPlaying = play;
        exoPlayer.setPlayWhenReady(play);
        if(!isPlaying){
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        }else{
            setProgress();
            btnPlay.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private void initTxtTime() {
        txtCurrentTime = findViewById(R.id.time_current);
        txtEndTime = findViewById(R.id.player_end_time);
    }
/*

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds =  timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
*/

    private void setProgress() {
        seekPlayerProgress.setProgress(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);
        txtCurrentTime.setText(VideoHelper.timeConverter((int) exoPlayer.getCurrentPosition()));
        txtEndTime.setText(VideoHelper.timeConverter((int) exoPlayer.getDuration()));

        if(handler == null)handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && isPlaying) {
                    seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
                    seekPlayerProgress.setProgress(mCurrentPosition);
                    txtCurrentTime.setText(VideoHelper.timeConverter((int) exoPlayer.getCurrentPosition()));
                    txtEndTime.setText(VideoHelper.timeConverter((int) exoPlayer.getDuration()));

                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    private void initSeekBar() {
        seekPlayerProgress = findViewById(R.id.mediacontroller_progress);
        seekPlayerProgress.requestFocus();

        seekPlayerProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                exoPlayer.seekTo(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekPlayerProgress.setMax(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.release();

    }


    private void fetchMusicByPath(RecyclerView recyclerView, String folderPath){

        String selection = MediaStore.Audio.AudioColumns.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+folderPath+"%"};
        playlist = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST
        };
        Cursor c = this.getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Music audioModel = new Music();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);


                String ext = path.substring(path.lastIndexOf("/") + 1);
                String songName = ext.substring(0,ext.lastIndexOf("."));


                audioModel.setArtistName(artist);
                audioModel.setArtistSong(songName);
                audioModel.setArtistPath(path);


                playlist.add(audioModel);

                Log.v(" Album :%s", album);
                Log.v(" Artist :%s", artist);
                Log.v(" path :%s", path);



            }

            c.close();
        }

        playlistAdapter = new PlaylistAdapter(this,playlist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playlistAdapter);


        Log.v("Adapter","adapter showing");
        // recycler_view_series_fragment.setLayoutManager(gridLayoutManager);     }

    }


}
