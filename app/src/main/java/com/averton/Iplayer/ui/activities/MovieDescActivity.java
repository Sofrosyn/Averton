package com.averton.Iplayer.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.MetadataExtractor;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.Utils.Tag;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.Adapters.MovieAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MovieDescActivity extends AppCompatActivity {

    private Button playButton;
    private TextView descriptions;
    private ImageView descImage;
    private RecyclerView moviesComedy;
    private RecyclerView moviesAction;
    private RecyclerView moviesTragedy;
    private RecyclerView moviesRomance;
    private Toolbar toolbar;
    private Intent intent;
    private ArrayList<Video> arrayVideo;
    private  String videoPath;
    int noClicks = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        toolbar = findViewById(R.id.movies_description_toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();

        initViews();
        initAction();
    }
    private void initViews(){
        playButton = findViewById(R.id.movieDescPlayButton);
        descriptions = findViewById(R.id.movie_description_text);
        descImage = findViewById(R.id.movie_description_image);
        moviesComedy = findViewById(R.id.recycler_view_movies_desc_comedy);
        moviesTragedy = findViewById(R.id.recycler_view_movies_desc_Tragedy);
        moviesAction = findViewById(R.id.recycler_view_movies_desc_action);
        moviesRomance = findViewById(R.id.recycler_view_movies_desc_romance);

    }

    private void initAction() {
        if (intent != null) {
                videoPath = intent.getStringExtra("videoPath");
            String videoName = intent.getStringExtra("videoName");
 //           int noClick = intent.getIntExtra("noClicks",0);
            String tag = intent.getStringExtra("tag");
            MetadataExtractor extractor = new MetadataExtractor();


            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.video_art)
                    .error(R.drawable.video_art).fitCenter();

            Glide.with(this).setDefaultRequestOptions(requestOptions).load(extractor.getCoverArt(videoPath)).thumbnail(0.5f).into(descImage);

            assert tag != null;
            switch (tag){
                case "nollywood":
                    fetchVideoByPath(moviesAction, Paths.moviesNollywoodAction,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesNollywoodComedy,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesNollywoodTragedy,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;

                case "trending":
                    fetchVideoByPath(moviesAction, Paths.moviesHollywoodAction,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesHollywoodComedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesBollywoodTragedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesBollywoodRomance,Tag.movies_Bollywood);
                     break;

                case "pick-of-the-week":
                    fetchVideoByPath(moviesAction, Paths.moviesBollywoodAction,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesBollywoodComedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesNollywoodTragedy,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "hollywood":
                    fetchVideoByPath(moviesAction, Paths.moviesHollywoodAction,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesHollywoodComedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesHollywoodTragedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesHollywoodRomance,Tag.movies_Hollywood);
                        break;

                case "bollywood":
                    fetchVideoByPath(moviesAction, Paths.moviesBollywoodAction,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesBollywoodComedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesBollywoodTragedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesBollywoodRomance,Tag.movies_Bollywood);
                        break;
                case "home-trending":
                    fetchVideoByPath(moviesAction, Paths.moviesNollywoodAction,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesHollywoodComedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesBollywoodTragedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "home-billboard":
                    fetchVideoByPath(moviesAction, Paths.moviesHollywoodAction,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesHollywoodComedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesBollywoodTragedy,Tag.movies_Bollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "documentary":
                    fetchVideoByPath(moviesAction, Paths.moviesNollywoodAction,Tag.movies_Nollywood);
                    fetchVideoByPath(moviesComedy, Paths.moviesHollywoodComedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesTragedy, Paths.moviesHollywoodTragedy,Tag.movies_Hollywood);
                    fetchVideoByPath(moviesRomance, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;


            }




        }

        playButton.setOnClickListener(v -> {

             noClicks++ ;
            Intent intent2 = new Intent(this, ExoPlayerActivity.class );
            intent2.putExtra("videoPath",videoPath);
            intent2.putExtra("noClicks",noClicks);
            startActivity(intent2);

        });

    }




    private void fetchVideoByPath(RecyclerView recyclerView, String videoFolder,String tag){

        String selection = MediaStore.Video.Media.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+videoFolder+"%"};

        arrayVideo = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.TITLE
                ,MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Cursor c = this.getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Video videoModel = new Video();
                String path = c.getString(6);
                String videoDuration = c.getString(1);
                String videoSize = c.getString(2);
                String videoTitle = c.getString(5);

                String vDuration = VideoHelper.timeConverter(Integer.parseInt(videoDuration));
                String name = path.substring(path.lastIndexOf("/") + 1);
                String thumbnails = path.substring(0,path.lastIndexOf("/"));

                videoModel.setVideoName(name);
                videoModel.setVideoPath(path);
                videoModel.setVideoDuration(vDuration);
                videoModel.setVideoGenre(videoSize);
                videoModel.setVideoThumbNail(thumbnails);

//                audioModel.setArtistSong(album);
                //  audioModel.setArtistPath(path);


                arrayVideo.add(videoModel);
                Log.v(" videoDuration :%s", videoDuration);
//                Log.v(" videoTitle :%s", videoTitle);
                Log.v(" Video path :%s", path);
                Log.v(" Name :%s", name);


            }

            c.close();
        }

        MovieAdapter movieAdapter = new MovieAdapter(this,arrayVideo,tag);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(movieAdapter);

    }


}
