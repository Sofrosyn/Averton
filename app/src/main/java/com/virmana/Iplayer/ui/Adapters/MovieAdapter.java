package com.virmana.Iplayer.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.entity.Video;
import com.virmana.Iplayer.ui.activities.MoviePlayerActivity;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.myViewHolder> {

    private Context mContext;
    private ArrayList<Video> videoList;

    private final int maxItems = 10;

    @Override
    public MovieAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fragment_video, parent, false);
        return  new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.myViewHolder holder, int position) {
        Video video = videoList.get(position);
     //   holder.videoTitle.setText(video.getVideoName());
        holder.videoGenre.setText(video.getVideoDuration());

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder).fitCenter();

        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(R.drawable.video_art).thumbnail(0.5f).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(v -> {
            //Toasty.info(mContext,"clicked",Toasty.LENGTH_SHORT).show();

            Intent intent = new Intent(mContext, MoviePlayerActivity.class );
            intent.putExtra("videoPath",video.getVideoPath());
            intent.putExtra("Activity",2);
            mContext.startActivity(intent);});

    }

    @Override
    public int getItemCount() {

        if(videoList.size()< maxItems){return videoList.size();}
        else {return maxItems;}


    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView videoTitle, videoGenre;
        public ImageView thumbnail;


        public myViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoGenre = itemView.findViewById(R.id.video_Genre);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
        }
    }

    public MovieAdapter(Context mContext, ArrayList<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }


}