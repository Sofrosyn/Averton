package com.averton.Iplayer.ui.fragments;


import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.averton.Iplayer.Utils.Tag;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.Adapters.MovieAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.averton.Iplayer.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {


    private View view;


    private String thriller = "file://"+"/mnt/extSdCard/AVERTON/Thriller/movie.mp4";

    private RecyclerView recycler_view_movies_trending;
    private RecyclerView recycler_view_movies_pickOfTheWeek;
    private RecyclerView recycler_view_movies_documentary;
    private RecyclerView recycler_view_movies_hollywood;
    private RecyclerView recycler_view_movies_bollywood;
    private RecyclerView recycler_view_movies_nollywood;





    private  ArrayList<Video> arrayVideo;
    private MovieAdapter movieAdapter;

    private VideoHelper videoHelper;
    private VideoView thriller_video;
    private int number;





    public MoviesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_movies, container, false);

        initView();
        initActon();

        return view;
    }



    private void initActon() {

    }

    private void initView() {


        this.recycler_view_movies_trending = view.findViewById(R.id.recycler_view_movies_trending);
        this.recycler_view_movies_pickOfTheWeek = view.findViewById(R.id.recycler_view_movies_pickOfTheWeek);
        this.recycler_view_movies_documentary = view.findViewById(R.id.recycler_view_movies_documentary);
         this.recycler_view_movies_hollywood = view.findViewById(R.id.recycler_view_movies_hollywood);
        this.recycler_view_movies_bollywood = view.findViewById(R.id.recycler_view_movies_bollywood);
        this.recycler_view_movies_nollywood = view.findViewById(R.id.recycler_view_movies_nollywood);
        this.thriller_video = view.findViewById(R.id.video_Thriller);




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{

            fetchVideoByPath(recycler_view_movies_trending,Paths.moviesTrending,Tag.movie_trending);
            fetchVideoByPath(recycler_view_movies_pickOfTheWeek,Paths.moviesPickOfTheWeek,Tag.movies_pickOfTheWeek);
            fetchVideoByPath(recycler_view_movies_documentary,Paths.moviesDocumentary,Tag.movies_Documentary);
            fetchVideoByPath(recycler_view_movies_bollywood,Paths.moviesBollywoodHome,Tag.movies_Bollywood);
           fetchVideoByPath(recycler_view_movies_nollywood,Paths.moviesNollywoodHome,Tag.movies_Nollywood);
            fetchVideoByPath(recycler_view_movies_hollywood,Paths.moviesHollywoodHome,Tag.movies_Hollywood);
        }

        Log.v("Adapter","adapter showing");

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
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_TAKEN
        };
        Cursor c = getActivity().getContentResolver().query(uri, projection,selection, selectionArgs, null);

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
        movieAdapter = new MovieAdapter(getActivity(),arrayVideo, tag);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(movieAdapter);

    }







    private void requestStoragePermission( ){
        Dexter.withActivity(getActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){

                    fetchVideoByPath(recycler_view_movies_trending,Paths.moviesTrending,Tag.movie_trending);
                    fetchVideoByPath(recycler_view_movies_pickOfTheWeek,Paths.moviesPickOfTheWeek,Tag.movies_pickOfTheWeek);
                    fetchVideoByPath(recycler_view_movies_documentary,Paths.moviesDocumentary,Tag.movies_Documentary);
                    fetchVideoByPath(recycler_view_movies_bollywood,Paths.moviesBollywoodHome,Tag.movies_Bollywood);
                    fetchVideoByPath(recycler_view_movies_nollywood,Paths.moviesNollywoodHome,Tag.movies_Nollywood);
                    fetchVideoByPath(recycler_view_movies_hollywood,Paths.moviesHollywoodHome,Tag.movies_Hollywood);

                }
                if(report.isAnyPermissionPermanentlyDenied()){

                    videoHelper.showDialog("Iplayer",getActivity());

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(error -> {

        }).onSameThread().check();
    }


    @Override
    public void onStart() {
        super.onStart();
        stopPlayer();

        playThriller();

        Toasty.info(getActivity(),"Scroll left for more movies",Toasty.LENGTH_LONG).show();
        }

    @Override
    public void onResume() {
        super.onResume();
  playThriller();
    }

    @Override
    public void onStop() {
        super.onStop();
stopPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopPlayer();
    }



    public void playThriller(){
       thriller_video.setVideoPath(thriller);

        thriller_video.setOnPreparedListener(mp -> {

            thriller_video.start();
        });


        thriller_video.setOnCompletionListener(mp -> {
            thriller_video.start();
        });


    }
    public void stopPlayer(){
        thriller_video.stopPlayback();

    }


}//end of class
