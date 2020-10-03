package com.averton.Iplayer.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.averton.Iplayer.ui.activities.ExoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.entity.Video;
import com.averton.Iplayer.ui.activities.MoviePlayerActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.myViewHolder> {

    private Context mContext;
    private ArrayList<Video> videoList;
    private int noClicks=0;

    private final int maxItems = 10;

    @NotNull
    @Override
    public SportAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fragment_video, parent, false);
        return  new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SportAdapter.myViewHolder holder, int position) {

        Video video = videoList.get(position);

        holder.videoGenre.setText(video.getVideoDuration());
        String imageName = video.getVideoName()+".jpg";
        String imagePath = Paths.imagesMovieThumbnail+"/"+imageName;
        final Bitmap b = BitmapFactory.decodeFile(imagePath);

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.video_art)
                .error(R.drawable.video_art).fitCenter();

        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(video.getVideoPath()).thumbnail(0.5f).into(holder.thumbnail);




        holder.thumbnail.setOnClickListener(v -> {
            //Toasty.info(mContext,"clicked",Toasty.LENGTH_SHORT).show();



            noClicks++;




            Intent intent = new Intent(mContext, ExoPlayerActivity.class );
            intent.putExtra("videoPath",video.getVideoPath());
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
        public TextView videoTitle, videoGenre;
        public ImageView thumbnail;


        public myViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoGenre = itemView.findViewById(R.id.video_Genre);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
        }
    }

    public SportAdapter(Context mContext, ArrayList<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }
}
