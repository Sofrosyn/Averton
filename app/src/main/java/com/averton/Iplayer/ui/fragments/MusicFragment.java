package com.averton.Iplayer.ui.fragments;


import android.Manifest;

import android.database.Cursor;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;

import com.karumi.dexter.listener.PermissionRequest;

import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.averton.Iplayer.Utils.Tag;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.Utils.VideoHelper;
import com.averton.Iplayer.entity.Music;
import com.averton.Iplayer.ui.Adapters.MusicAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.averton.Iplayer.R;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {


    private View view;
    private Tag constant;
    private RecyclerView recycler_view_music_afroBeats;
    private RecyclerView recycler_view_music_Trending;
    private RecyclerView recycler_view_music_topRated;
    private RecyclerView recycler_view_music_RnB;
    private RecyclerView recycler_view_music_billboards;
    private RecyclerView recycler_view_music_hipHop;
    private RecyclerView recycler_view_music_soul;



    public ArrayList<Music> arrayMusic;
    private MusicAdapter musicAdapter;


    private VideoHelper videoHelper;



    public MusicFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_music, container, false);


        initView();
        initActon();

        return view;
    }

    private void initActon() {



    }



    private void initView() {


        videoHelper = new VideoHelper();


        this.recycler_view_music_afroBeats = view.findViewById(R.id.recycler_view_music_afroBeats);
        this.recycler_view_music_Trending = view.findViewById(R.id.recycler_view_music_trending);
        this.recycler_view_music_topRated = view.findViewById(R.id.recycler_view_music_topRated);
        this.recycler_view_music_RnB = view.findViewById(R.id.recycler_view_music_RnB);
        this.recycler_view_music_billboards = view.findViewById(R.id.recycler_view_music_billboards);
        this.recycler_view_music_hipHop = view.findViewById(R.id.recycler_view_music_hipHop);
        this.recycler_view_music_soul = view.findViewById(R.id.recycler_view_music_soul);















    }


    private void fetchMusicByPath(RecyclerView recyclerView, String folderPath,String tag){

        String selection = MediaStore.Audio.AudioColumns.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+folderPath+"%"};
        arrayMusic = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST
        };
        Cursor c = getActivity().getContentResolver().query(uri, projection,selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {

                Music audioModel = new Music();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
//                String track =c.getString(3);

                String ext = path.substring(path.lastIndexOf("/") + 1);
                String songName = ext.substring(0,ext.lastIndexOf("."));
                //  String albumArt =  path.substring(0,path.lastIndexOf("/"));

                audioModel.setArtistName(artist);
                audioModel.setArtistSong(songName);
                audioModel.setArtistPath(path);

//                audioModel.setArtistSong(album);
                //  audioModel.setArtistPath(path);
                arrayMusic.add(audioModel);

                Log.v(" Album :%s", album);
                Log.v(" Artist :%s", artist);
                Log.v(" path :%s", path);

            }

            c.close();
        }


        musicAdapter = new MusicAdapter(getActivity(),arrayMusic,tag);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(musicAdapter);


        Log.v("Adapter","adapter showing");
        // recycler_view_series_fragment.setLayoutManager(gridLayoutManager);     }

    }



    private void requestStoragePermission( ){
        Dexter.withActivity(getActivity()).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (report.areAllPermissionsGranted()){

                fetchMusicByPath(recycler_view_music_afroBeats, Paths.musicAfrobeats,"afrobeats");
                fetchMusicByPath(recycler_view_music_billboards, Paths.musicBillBoard,"billboards");
                fetchMusicByPath(recycler_view_music_hipHop, Paths.musichipHop,"hiphop");
                fetchMusicByPath(recycler_view_music_RnB, Paths.musicRnB,"rnb");
                fetchMusicByPath(recycler_view_music_soul, Paths.musicSouls,"soul");
                fetchMusicByPath(recycler_view_music_Trending, Paths.musicTrending,"trending");
                fetchMusicByPath(recycler_view_music_topRated, Paths.musicAfricaTopRated,"toprated");

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
    }


    @Override
    public void onStart() {
        super.onStart();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{

            fetchMusicByPath(recycler_view_music_afroBeats, Paths.musicAfrobeats,Tag.Afrobeats);
            fetchMusicByPath(recycler_view_music_billboards, Paths.musicBillBoard,Tag.Billboards);
            fetchMusicByPath(recycler_view_music_hipHop, Paths.musichipHop,Tag.Hiphop);
            fetchMusicByPath(recycler_view_music_RnB, Paths.musicRnB,Tag.RnB);
            fetchMusicByPath(recycler_view_music_soul, Paths.musicSouls,Tag.Soul);
            fetchMusicByPath(recycler_view_music_Trending, Paths.musicTrending,Tag.Trending);
            fetchMusicByPath(recycler_view_music_topRated, Paths.musicAfricaTopRated,Tag.TopRated);

        }

    }
}