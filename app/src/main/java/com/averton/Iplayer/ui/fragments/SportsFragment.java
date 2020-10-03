package com.averton.Iplayer.ui.fragments;


import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.Adapters.SportAdapter;

import java.util.ArrayList;
import java.util.List;



public class SportsFragment extends Fragment {


    final String THRILLERVIDEO = "file://"+"/mnt/extSdCard/AVERTON/Thriller/sport.mp4";
    private View view;
    private RecyclerView recycler_view_sport_trending;
    private RecyclerView recycler_view_sport_pickOfTheWeek;
    private RecyclerView recycler_view_sport_africa;
    private RecyclerView recycler_view_sport_hollywood;
    private RecyclerView recycler_view_sport_bollywood;
    private RecyclerView recycler_view_sport_nollywood;
    private  ArrayList<Video> arrayVideo;
    private SportAdapter sportAdapter;
    private VideoHelper videoHelper;
    private VideoView thriller_video;



    public SportsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_sports, container, false);

        initView();


        initActon();




        return view;
    }



    private void initActon() {
        playThriller();
    }

    private void initView() {


        this.recycler_view_sport_trending = view.findViewById(R.id.recycler_view_sport_trending);
        this.recycler_view_sport_pickOfTheWeek = view.findViewById(R.id.recycler_view_sport_pickOfTheWeek);
        this.recycler_view_sport_africa = view.findViewById(R.id.recycler_view_sport_africa);
        this.recycler_view_sport_hollywood = view.findViewById(R.id.recycler_view_sport_hollywood);
        this.recycler_view_sport_bollywood = view.findViewById(R.id.recycler_view_sport_bollywood);
        this.recycler_view_sport_nollywood = view.findViewById(R.id.recycler_view_sport_nollywood);
        this.thriller_video = view.findViewById(R.id.video_Thriller);




        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{


           fetchVideoByPath(recycler_view_sport_trending, Paths.sportsBundesliga);
            fetchVideoByPath(recycler_view_sport_pickOfTheWeek,Paths.sportsEpl);
            fetchVideoByPath(recycler_view_sport_africa,Paths.sportsSeriaA);

            fetchVideoByPath(recycler_view_sport_bollywood,Paths.sportsLaliga);
            fetchVideoByPath(recycler_view_sport_nollywood,Paths.sportsleague1);
            fetchVideoByPath(recycler_view_sport_hollywood,Paths.sportsHightLights);


        }

        Log.v("Adapter","adapter showing");

    }


    private void fetchVideoByPath(RecyclerView recyclerView, String videoFolder){

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
        Cursor c = getActivity().getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Video videoModel = new Video();
                String path = c.getString(6);
                String videoDuration = c.getString(1);
                String videoSize = c.getString(2);


                String vDuration = VideoHelper.timeConverter(Integer.parseInt(videoDuration));
                String name = path.substring(path.lastIndexOf("/") + 1);

                videoModel.setVideoName(name);
                videoModel.setVideoPath(path);
                videoModel.setVideoDuration(vDuration);
                videoModel.setVideoGenre(videoSize);


                arrayVideo.add(videoModel);
                Log.v(" videoDuration :%s", videoDuration);

                Log.v(" Video path :%s", path);
                Log.v(" Name :%s", name);


            }

            c.close();
        }
        sportAdapter = new SportAdapter(getActivity(),arrayVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(sportAdapter);

    }









    private void requestStoragePermission( ){
        Dexter.withActivity(getActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){


                    fetchVideoByPath(recycler_view_sport_trending, Paths.sportsBundesliga);
                    fetchVideoByPath(recycler_view_sport_pickOfTheWeek,Paths.sportsEpl);
                    fetchVideoByPath(recycler_view_sport_africa,Paths.sportsSeriaA);

                    fetchVideoByPath(recycler_view_sport_bollywood,Paths.sportsLaliga);
                    fetchVideoByPath(recycler_view_sport_nollywood,Paths.sportsleague1);
                    fetchVideoByPath(recycler_view_sport_hollywood,Paths.sportsHightLights);



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

            thriller_video.setOnCompletionListener(mp -> thriller_video.start());
    }
    public void stopPlayer(){
        thriller_video.stopPlayback();

    }

    @Override
    public void onStart() {
        super.onStart();
        playThriller();
    }


}/* end of class */
