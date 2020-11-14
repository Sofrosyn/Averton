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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.Utils.Tag;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.Adapters.MovieAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentaryFragment extends Fragment {


    private View view;
    private RecyclerView recycler_view_documentary;
    VideoHelper videoHelper;
    private MovieAdapter movieAdapter;
    private  ArrayList<Video> arrayVideo;


    public DocumentaryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view=  inflater.inflate(R.layout.fragment_documentary, container, false);



       initViews();

        return view;
    }


    private void initViews() {


        this.recycler_view_documentary = view.findViewById(R.id.fragment_documentary_recycler_view);






        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{
            fetchVideoByPath(recycler_view_documentary,Paths.moviesDocumentary,Tag.movies_Documentary);

        }





    }

    private void fetchVideoByPath(RecyclerView recyclerView, String videoFolder,String tag){

        String selection = MediaStore.Video.Media.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+ videoFolder +"%"};

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
                String videoName = name.substring(0,name.lastIndexOf("."));

                videoModel.setVideoName(videoName);
                videoModel.setVideoPath(path);
                videoModel.setVideoDuration(vDuration);
                videoModel.setVideoDate(VideoHelper.years(Long.parseLong(videoDate)));

                videoModel.setVideoGenre(videoSize);


                arrayVideo.add(videoModel);
                Log.v(" videoDuration :%s", videoDuration);
//                Log.v(" videoTitle :%s", videoTitle);
                Log.v(" Video path :%s", path);
                Log.v(" Name :%s", name);


            }

            c.close();
        }
        movieAdapter = new MovieAdapter(getActivity(),arrayVideo,tag);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),6));

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
                fetchVideoByPath(recycler_view_documentary,Paths.moviesDocumentary,Tag.movies_Documentary);


                }
                if(report.isAnyPermissionPermanentlyDenied()){

                    videoHelper.showDialog("Iplayer",getActivity());

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(error -> Toasty.info(getActivity(),"Something went wrong",Toasty.LENGTH_SHORT).show()).onSameThread().check();
    }//[end request permissions]






}// end of fragment


