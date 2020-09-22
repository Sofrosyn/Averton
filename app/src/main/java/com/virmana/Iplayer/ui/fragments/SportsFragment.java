package com.virmana.Iplayer.ui.fragments;


import android.Manifest;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
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
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.Utils.VideoHelper;
import com.virmana.Iplayer.entity.Genre;
import com.virmana.Iplayer.entity.Video;
import com.virmana.Iplayer.ui.Adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SportsFragment extends Fragment {


    private View view;

    private List<Genre> genreList =  new ArrayList<>();

    private RecyclerView recycler_view_sport_trending;
    private RecyclerView recycler_view_sport_pickOfTheWeek;
    private RecyclerView recycler_view_sport_africa;
    private RecyclerView recycler_view_sport_hollywood;
    private RecyclerView recycler_view_sport_bollywood;
    private RecyclerView recycler_view_sport_nollywood;



    private final String TRENDING = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String PICKOFTHEWEEK = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String AFRICA = "/storage/emulated/0/Averton/Movies/Money Heist/";
    private final String HOLLYWOOD = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String BOLLYWOOD = "/storage/emulated/0/Averton/Movies/DOW S1/";
    private final String NOLLYWOOD = "/storage/emulated/0/Averton/Movies/Money Heist/";

    private  ArrayList<Video> arrayVideo;
    private MovieAdapter movieAdapter;

    private VideoHelper videoHelper;
    private VideoView thriller_video;
    private int number;

    final String THRILLERVIDEO = "file://"+"/storage/emulated/0/AVERTON/Movies/Trailer.mp4";



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


           fetchVideoByPath(recycler_view_sport_trending,TRENDING);

            fetchVideoByPath(recycler_view_sport_pickOfTheWeek,PICKOFTHEWEEK);
            fetchVideoByPath(recycler_view_sport_africa,AFRICA);
            fetchVideoByPath(recycler_view_sport_bollywood,BOLLYWOOD);
            fetchVideoByPath(recycler_view_sport_nollywood,NOLLYWOOD);
            fetchVideoByPath(recycler_view_sport_hollywood,HOLLYWOOD);


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
        movieAdapter = new MovieAdapter(getActivity(),arrayVideo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(movieAdapter);

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


                    fetchVideoByPath(recycler_view_sport_trending,TRENDING);

                    fetchVideoByPath(recycler_view_sport_pickOfTheWeek,PICKOFTHEWEEK);
                    fetchVideoByPath(recycler_view_sport_africa,AFRICA);
                    fetchVideoByPath(recycler_view_sport_bollywood,BOLLYWOOD);
                    fetchVideoByPath(recycler_view_sport_nollywood,NOLLYWOOD);
                    fetchVideoByPath(recycler_view_sport_hollywood,HOLLYWOOD);



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

    public void pausPlayer(){
        thriller_video.pause();
    }
}//end of class
