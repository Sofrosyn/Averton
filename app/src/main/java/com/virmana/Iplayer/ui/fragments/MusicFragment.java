package com.virmana.Iplayer.ui.fragments;


import android.Manifest;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.TypedValue;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.virmana.Iplayer.Utils.VideoHelper;
import com.virmana.Iplayer.entity.Music;
import com.virmana.Iplayer.ui.Adapters.MusicAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.virmana.Iplayer.Provider.PrefManager;
import com.virmana.Iplayer.R;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {


    private View view;

    private RecyclerView recycler_view_music_fragment;

    private SwipeRefreshLayout swipe_refresh_layout_series_fragment;


    private GridLayoutManager gridLayoutManager;

    public ArrayList<Music> arrayMusic;
    private MusicAdapter musicAdapter;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private boolean firstLoadOrder = true;
    private boolean loaded = false;
    private VideoHelper videoHelper;
    private int screenOrientation;

    private PrefManager prefManager;
    RecyclerView.LayoutManager mLayoutManager;

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_music, container, false);

        prefManager = new PrefManager(getActivity());
        initView();
        initActon();

        return view;
    }

    private void initActon() {


        recycler_view_music_fragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {

                } else {

                }
            }
        });
    }


    private void initView() {


        videoHelper = new VideoHelper();


        this.recycler_view_music_fragment = view.findViewById(R.id.recycler_view_series_fragment);



        if (screenOrientation== Configuration.ORIENTATION_PORTRAIT) {
             mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
             mLayoutManager = new GridLayoutManager(getActivity(), 4);

        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
        requestStoragePermission();

        }else{fetchMusic();}



        // test


    }

    private void fetchMusic(){
        arrayMusic = new ArrayList<>();
        int column_index_data, thumbnail;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.ArtistColumns.ARTIST

        };
        Cursor c = getActivity().getContentResolver().query(uri, projection,null, null, null);

        if (c != null) {
            while (c.moveToNext()) {

                Music audioModel = new Music();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
//                String track =c.getString(3);

                String name = path.substring(path.lastIndexOf("/") + 1);
                String thumbnails = path.substring(0,path.lastIndexOf("/"));
                audioModel.setArtistName("artist name");
                audioModel.setArtistSong("song");
                audioModel.setArtistPath(path);
                audioModel.setArtistThumbnail(thumbnails);
//                audioModel.setArtistSong(album);
                //  audioModel.setArtistPath(path);
                arrayMusic.add(audioModel);

                Log.v(" Album :%s", album);
                Log.v(" Artist :%s", artist);
                Log.v(" path :%s", path);
                Log.v(" thumbnail :%s", thumbnails);

  //             Log.v(" Track :%s", track);


              }

            c.close();
        }
        final int columns = getResources().getInteger(R.integer.grid_column);
        musicAdapter = new MusicAdapter(getActivity(),arrayMusic,R.drawable.album_placeholder);
        recycler_view_music_fragment.setLayoutManager(new GridLayoutManager(getActivity(),columns));
        recycler_view_music_fragment.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recycler_view_music_fragment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_music_fragment.setHasFixedSize(true);
        recycler_view_music_fragment.setVerticalScrollBarEnabled(true);

        recycler_view_music_fragment.setAdapter(musicAdapter);


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
                fetchMusic();


            }
            if(report.isAnyPermissionPermanentlyDenied()){

            videoHelper.showDialog("Iplayer",getActivity());

            }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toasty.info(getActivity(),"Something went wrong",Toasty.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
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


}