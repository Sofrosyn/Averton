package com.averton.Iplayer.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.MetadataExtractor;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.activities.MovieDescActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.myViewHolder> {

    private final Context mContext;
    private final ArrayList<Video> videoList;
    private int noClicks=0;
    private String tag="";
    private final int maxItems = 10;

    @NotNull
    @Override
    public MovieAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fragment_video, parent, false);
        return  new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.myViewHolder holder, int position) {
        Video video = videoList.get(position);

    //    holder.videoGenre.setText(video.getVideoDuration());
     MetadataExtractor extractor = new MetadataExtractor();
        String imageName = video.getVideoName()+".jpg";
        String imagePath = Paths.imagesMovieThumbnail+"/"+imageName;


        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.video_art)
                .error(R.drawable.video_art).fitCenter();

    Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(extractor.getCoverArt(video.getVideoPath())).thumbnail(0.5f).into(holder.thumbnail);


            holder.videoDate.setText(video.getVideoDate());

        holder.thumbnail.setOnClickListener(v -> {
            //Toasty.info(mContext,"clicked",Toasty.LENGTH_SHORT).show();



            noClicks++;




            Intent intent = new Intent(mContext, MovieDescActivity.class );
            intent.putExtra("videoPath",video.getVideoPath());
            intent.putExtra("tag",tag);
            intent.putExtra("videoDate",video.getVideoDate());
            intent.putExtra("videoName",video.getVideoName());
            intent.putExtra("Activity",2);
            intent.putExtra("noClicks",noClicks);

            mContext.startActivity(intent);});



    }

    @Override
    public int getItemCount() {

        if(videoList.size()< maxItems){return videoList.size();}
        else {return maxItems;}


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public final TextView videoDate;
        public TextView videoGenre;
        public final ImageView thumbnail;


        public myViewHolder(View itemView) {
            super(itemView);
            videoDate = itemView.findViewById(R.id.video_year);
//            videoGenre = itemView.findViewById(R.id.video_Genre);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
        }
    }

    public MovieAdapter(Context mContext, ArrayList<Video> videoList,String tag) {
        this.mContext = mContext;
        this.videoList = videoList;
        this.tag = tag;
    }


}