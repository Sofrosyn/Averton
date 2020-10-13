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

import es.dmoral.toasty.Toasty;

public class MovieDescActivity extends AppCompatActivity {

    private Button playButton;
    private TextView descriptions;
    private ImageView descImage;
    private RecyclerView moviesRelatedVideos;
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

        moviesRelatedVideos = findViewById(R.id.recycler_view_movies_desc_related);



    }

    @Override
    protected void onStart() {
        super.onStart();
        Toasty.info(this,"Scroll left for more movies",Toasty.LENGTH_LONG).show();
    }

    private void initAction() {
        if (intent != null) {
                videoPath = intent.getStringExtra("videoPath");
            String videoName = intent.getStringExtra("videoName");
            String tag = intent.getStringExtra("tag");
            MetadataExtractor extractor = new MetadataExtractor();


            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.video_art)
                    .error(R.drawable.video_art).fitCenter();

            Glide.with(this).setDefaultRequestOptions(requestOptions).load(extractor.getCoverArt(videoPath)).thumbnail(0.5f).into(descImage);

            assert tag != null;
            switch (tag){
                case "nollywood":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;

                case "trending":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesBollywoodRomance,Tag.movies_Bollywood);
                     break;

                case "pick-of-the-week":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "hollywood":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesHollywoodRomance,Tag.movies_Hollywood);
                        break;

                case "bollywood":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesBollywoodRomance,Tag.movies_Bollywood);
                        break;
                case "home-trending":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "home-billboard":
                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesNollywoodRomance,Tag.movies_Nollywood);
                    break;
                case "documentary":

                    fetchVideoByPath(moviesRelatedVideos, Paths.moviesDocumentary,Tag.movies_Documentary);
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

    /**
     *
     * @param recyclerView "Recyclerview that should be populated"
     * @param videoFolder  "path to folder containing the videos"
     * @param tag "genre of the video folder"
     */


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
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_TAKEN
        };
        Cursor c = this.getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Video videoModel = new Video();
                String path = c.getString(6);
                String videoDuration = c.getString(1);
                String videoSize = c.getString(2);
                String videoTitle = c.getString(5);
                String videoDate = c.getString(7);

                String vDuration = VideoHelper.timeConverter(Integer.parseInt(videoDuration));
                String name = path.substring(path.lastIndexOf("/") + 1);
                String thumbnails = path.substring(0,path.lastIndexOf("/"));

                videoModel.setVideoName(name);
                videoModel.setVideoPath(path);
                videoModel.setVideoDuration(vDuration);
                videoModel.setVideoGenre(videoSize);
                videoModel.setVideoDate(VideoHelper.years(Long.parseLong(videoDate)));
                videoModel.setVideoThumbNail(thumbnails);


                arrayVideo.add(videoModel);
                Log.v(" videoDuration :%s", videoDuration);
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
