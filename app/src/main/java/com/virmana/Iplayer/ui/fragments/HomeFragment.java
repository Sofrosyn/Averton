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
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.orhanobut.hawk.Hawk;
import com.synnapps.carouselview.CarouselView;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.Utils.VideoHelper;
import com.virmana.Iplayer.entity.*;
import com.virmana.Iplayer.ui.Adapters.MusicAdapter;
import com.virmana.Iplayer.ui.Adapters.MovieAdapter;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private View view;

    private RecyclerView recycler_view_home_music;
    private RecyclerView recycler_view_home_video;
    private RecyclerView recycler_view_home_news_local;
    private RecyclerView recycler_view_home_news_international;
    private RecyclerView recycler_view_home_music_hotVibes;
    private RecyclerView recycler_view_home_video_pickOfTheWeek;



    VideoHelper videoHelper;

    private CarouselView carouselView;

    private MusicAdapter musicAdapter;

    private MovieAdapter movieAdapter;
    private  ArrayList<Music> arrayMusic;
    private  ArrayList<Video> arrayVideo;
    private ArrayList<String> carouselArray;

int [] ThrillerImages;


    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view=  inflater.inflate(R.layout.fragment_home, container, false);



       initViews();
        initActions();
      //  thrillerImages();

        return view;
    }


    private void initActions() {


        String recommendationsVideoPath = VideoHelper.makeDirs(getActivity(),"Recommendations");
        String comedyVideoPath = VideoHelper.makeDirs(getActivity(),"Comedy");
        String actionVideoPath = VideoHelper.makeDirs(getActivity(),"Action");
        String tragedyVideoPath = VideoHelper.makeDirs(getActivity(),"Tragedy");

        Hawk.put("recommendations",recommendationsVideoPath);
        Hawk.put("comedy",comedyVideoPath);
        Hawk.put("action",actionVideoPath);
        Hawk.put("tragedy",tragedyVideoPath);


        ThrillerImages = new int[]{R.drawable.terminator,R.drawable.see,R.drawable.money_heist};

     carouselView.setPageCount(ThrillerImages.length);

        carouselView.setImageListener((position, imageView) -> {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(ThrillerImages[position]);
        });


        Log.v("Hawk",Hawk.get("recommendations"));
        Log.v("Hawk",Hawk.get("comedy"));





    }

    private void initViews() {
        this.recycler_view_home_music = view.findViewById(R.id.recycler_view_home_music);
        this.recycler_view_home_video = view.findViewById(R.id.recycler_view_home_video);
        this.recycler_view_home_news_local = view.findViewById(R.id.recycler_view_home_news);
        this.carouselView = view.findViewById(R.id.carousel_home_fragment);

        this.recycler_view_home_music_hotVibes = view.findViewById(R.id.recycler_view_home_music_hottestVibes);
        this.recycler_view_home_video_pickOfTheWeek = view.findViewById(R.id.recycler_view_home_video_pickOfTheWeek);
        this.recycler_view_home_news_international = view.findViewById(R.id.recycler_view_home_news_international);






        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            requestStoragePermission();

        }else{fetchMusic(); fetchVideo();
        fetchMusicByPath(recycler_view_home_music_hotVibes,"/storage/emulated/0/AVERTON/Music/Alessia_Cara");
         fetchVideoByPath(recycler_view_home_video_pickOfTheWeek,"/storage/emulated/0/Averton/Movies/DOW S1/");
        }





    }

    private void fetchMusic(){
        arrayMusic = new ArrayList<>();

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
//               String track =c.getString(3);
                 String viewedName;
                 String name = path.substring(path.lastIndexOf("/") + 1);
                 String truePath =  path.substring(0,path.lastIndexOf("/"));




                audioModel.setArtistName("artist name");
                audioModel.setArtistSong("song");
                audioModel.setArtistPath(path);
//
                arrayMusic.add(audioModel);

                Log.v(" Album :%s", album);
                Log.v(" Artist :%s", artist);
                Log.v(" path :%s", path);
                Log.v(" truePath :%s", truePath);
                //             Log.v(" Track :%s", track);


            }

            c.close();
        }

        final int columns = getResources().getInteger(R.integer.grid_column);
        musicAdapter = new MusicAdapter(getActivity(),arrayMusic,R.drawable.music_bilie);

        recycler_view_home_music.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recycler_view_home_music.setItemAnimator(new DefaultItemAnimator());
        recycler_view_home_music.setHasFixedSize(true);
        recycler_view_home_music.setAdapter(musicAdapter);


        Log.v("Adapter","adapter showing");
        // recycler_view_series_fragment.setLayoutManager(gridLayoutManager);     }

    }

    private void fetchVideo(){
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
        Cursor c = getActivity().getContentResolver().query(uri, projection,null, null, null);

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
        recycler_view_home_video.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
       // recycler_view_home_video.addItemDecoration(new HomeFragment.GridSpacingItemDecoration(4, dpToPx(10), true));
        recycler_view_home_video.setItemAnimator(new DefaultItemAnimator());



        recycler_view_home_video.setHasFixedSize(true);


        recycler_view_home_video.setAdapter(movieAdapter);

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
        recyclerView.addItemDecoration(new HomeFragment.GridSpacingItemDecoration(4, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(movieAdapter);

    }

    private void fetchMusicByPath(RecyclerView recyclerView, String folderPath){

        String selection = MediaStore.Audio.AudioColumns.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+folderPath+"%"};
        arrayMusic = new ArrayList<>();
        int column_index_data, thumbnail;
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

                String name = path.substring(path.lastIndexOf("/") + 1);
                String albumArt =  path.substring(0,path.lastIndexOf("/"));

                audioModel.setArtistName("artist name");
                audioModel.setArtistSong("song");
                audioModel.setArtistPath(path);
                audioModel.setArtistThumbnail(albumArt);
//                audioModel.setArtistSong(album);
                //  audioModel.setArtistPath(path);
                arrayMusic.add(audioModel);

                Log.v(" Album :%s", album);
                Log.v(" Artist :%s", artist);
                Log.v(" path :%s", path);
                Log.v(" truePath :%s", albumArt);
                //             Log.v(" Track :%s", track);


            }

            c.close();
        }

        final int columns = getResources().getInteger(R.integer.grid_column);
        musicAdapter = new MusicAdapter(getActivity(),arrayMusic,R.drawable.music_cara);

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
                    fetchMusic();
                    fetchVideo();
                    fetchMusicByPath(recycler_view_home_music_hotVibes,"/storage/emulated/0/AVERTON/Music/Alessia_Cara");
                    fetchVideoByPath(recycler_view_home_video_pickOfTheWeek,"/storage/emulated/0/Averton/Movies/DOW S1");

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
    }//[end request permissions]

    private void thrillerImages() {

     carouselArray = new ArrayList<>();
        String selection = MediaStore.Images.Media.DATA + " like ? ";
        String[] selectionArgs =new String [] {"%"+"/storage/emulated/0/AVERTON/thumbnail/"+"%"};


        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.Media.DATA
        };
        Cursor c = getActivity().getContentResolver().query(uri, projection,selection, selectionArgs,   MediaStore.Files.FileColumns.DATE_ADDED + " ASC");

        if (c != null) {
            while (c.moveToNext()) {


                String path = c.getString(0);


               carouselArray.add(path);

            }

            c.close();


        }

        carouselView.setPageCount(carouselArray.size());

        carouselView.setImageListener((position, imageView) -> {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageURI(Uri.parse(carouselArray.get(position)));

            Log.v("thriller Image",carouselArray.get(position));
        });

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

}// end of fragment


