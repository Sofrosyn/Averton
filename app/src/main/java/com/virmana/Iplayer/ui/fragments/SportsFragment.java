package com.virmana.Iplayer.ui.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;
import com.virmana.Iplayer.Utils.Method;
import com.virmana.Iplayer.Utils.StorageUtil;
import com.virmana.Iplayer.Utils.VideoHelper;
import com.virmana.Iplayer.entity.Music;
import com.virmana.Iplayer.entity.Video;
import com.virmana.Iplayer.ui.Adapters.MusicAdapter;
import com.virmana.Iplayer.ui.Adapters.VideoAdapter;
import com.virmana.Iplayer.ui.activities.HomeActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.virmana.Iplayer.Provider.PrefManager;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.api.apiClient;
import com.virmana.Iplayer.api.apiRest;
import com.virmana.Iplayer.entity.Genre;
import com.virmana.Iplayer.entity.Poster;
import com.virmana.Iplayer.ui.Adapters.PosterAdapter;
import com.virmana.Iplayer.ui.activities.PlayerActivity;
import es.dmoral.toasty.Toasty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {


    private View view;

    private List<Genre> genreList =  new ArrayList<>();

    private RecyclerView recycler_view_movies_trending;
    private RecyclerView recycler_view_movies_pickOfTheWeek;
    private RecyclerView recycler_view_movies_africa;
    private RecyclerView recycler_view_movies_hollywood;
    private RecyclerView recycler_view_movies_bollywood;
    private RecyclerView recycler_view_movies_nollywood;

    private ImageButton playButton;

    private final String TRENDING = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String PICKOFTHEWEEK = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String AFRICA = "/storage/emulated/0/Averton/Movies/Money Heist/";
    private final String HOLLYWOOD = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String BOLLYWOOD = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String NOLLYWOOD = "/storage/emulated/0/Averton/Movies/Money Heist/";

    private  ArrayList<Video> arrayVideo;
    private  VideoAdapter videoAdapter;

    private VideoHelper videoHelper;
    private VideoView thriller_video;
    private int number;

    final String THRILLERVIDEO = "file://"+"/storage/emulated/0/AVERTON/Movies/DOW S1/A Discovery of Witches - S01E01.mp4";



    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_movies, container, false);
            ;

        initView();
        initActon();

        return view;
    }



    private void initActon() {
        playThriller();
    }

    private void initView() {


        this.recycler_view_movies_trending = view.findViewById(R.id.recycler_view_movies_trending);
        this.recycler_view_movies_pickOfTheWeek = view.findViewById(R.id.recycler_view_movies_pickOfTheWeek);
        this.recycler_view_movies_africa = view.findViewById(R.id.recycler_view_movies_africa);
        this.recycler_view_movies_hollywood = view.findViewById(R.id.recycler_view_movies_hollywood);
        this.recycler_view_movies_bollywood = view.findViewById(R.id.recycler_view_movies_bollywood);
        this.recycler_view_movies_nollywood = view.findViewById(R.id.recycler_view_movies_nollywood);
        this.thriller_video = view.findViewById(R.id.video_Thriller);

        this.playButton = view.findViewById(R.id.button_thriller);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{

            fetchVideoByPath(recycler_view_movies_trending,TRENDING);
            fetchVideoByPath(recycler_view_movies_pickOfTheWeek,PICKOFTHEWEEK);
            fetchVideoByPath(recycler_view_movies_africa,AFRICA);
            fetchVideoByPath(recycler_view_movies_bollywood,BOLLYWOOD);
            fetchVideoByPath(recycler_view_movies_nollywood,NOLLYWOOD);
            fetchVideoByPath(recycler_view_movies_hollywood,HOLLYWOOD);
        }

        Log.v("Adapter","adapter showing");

    }


    private void fetchVideoByPath(RecyclerView recyclerView, String videoFolder){

        String selection = MediaStore.Video.Media.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+videoFolder+"%"};

        arrayVideo = new ArrayList<>();
        int column_index_data, thumnail;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.TITLE
                ,MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Cursor c = getActivity().getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Video videoModel = new Video();
                String path = c.getString(6);
                String videoDuration = c.getString(1);
                String videoSize = c.getString(2);
                String videoTitle = c.getString(5);

                String vDuration = VideoHelper.timeConverter(Integer.parseInt(videoDuration));
                String name = path.substring(path.lastIndexOf("/") + 1);

                videoModel.setVideoName(name);
                videoModel.setVideoPath(path);
                videoModel.setVideoDuration(vDuration);
                videoModel.setVideoGenre(videoSize);
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
        videoAdapter = new VideoAdapter(getActivity(),arrayVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(videoAdapter);

    }




    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



    private void requestStoragePermission( ){
        Dexter.withActivity(getActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){

                    fetchVideoByPath(recycler_view_movies_trending,TRENDING);
                    fetchVideoByPath(recycler_view_movies_pickOfTheWeek,PICKOFTHEWEEK);
                    fetchVideoByPath(recycler_view_movies_africa,AFRICA);
                    fetchVideoByPath(recycler_view_movies_bollywood,BOLLYWOOD);
                    fetchVideoByPath(recycler_view_movies_nollywood,NOLLYWOOD);
                    fetchVideoByPath(recycler_view_movies_hollywood,HOLLYWOOD);


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
playThriller();
        /*playButton.setOnClickListener(v -> {
    playThriller();
});*/
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
        thriller_video.setVideoPath(THRILLERVIDEO);
        thriller_video.setOnPreparedListener(mp -> {
            thriller_video.start();
        });

    }
    public void stopPlayer(){
        thriller_video.stopPlayback();

    }
}//end of class
