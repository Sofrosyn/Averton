package com.virmana.Iplayer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.virmana.Iplayer.R;

public class ExoPlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView ;

    private String data = "";
    private String tag ="";
private Intent intent= null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exo_movies_player);



    }

    private void initView(){
if(intent != null) {

}


                exoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView= findViewById(R.id.exoplayer_view);

        playerView.setPlayer(exoPlayer);

    }

    private void initializePlayer()


    {



    }




}
