package com.averton.Iplayer.ui.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.MetadataExtractor;
import com.averton.Iplayer.entity.Music;
import com.averton.Iplayer.ui.activities.MusicPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private final Context mContext;

    public final ArrayList<Music> albumList;

    private MetadataExtractor extractor;
    private final String tag;
    int noClicks = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView artist;
        public final ImageView thumbnail;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            artist = view.findViewById(R.id.artist);
            thumbnail = view.findViewById(R.id.musicThumbnail);

        }
    }


    public MusicAdapter(Context mContext, ArrayList<Music> albumList,String tag) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.tag =tag;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fragment_music, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        extractor = new MetadataExtractor();
        Music album = albumList.get(position);

        holder.title.setText(album.getArtistSong());
        holder.artist.setText(album.getArtistName());

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.album_placeholder).dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.album_placeholder).fitCenter();




        Glide.with(mContext).setDefaultRequestOptions(requestOptions).asBitmap().load(extractor.getCoverArt(album.getArtistPath())).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(v -> {
            noClicks++;
            //added new
            Intent intent = new Intent(mContext, MusicPlayerActivity.class );

            intent.putExtra("musicPath",album.getArtistPath());
            intent.putExtra("tag",tag);
            intent.putExtra("songName",album.getArtistSong());
            intent.putExtra("noClicks",noClicks);

            intent.putExtra("Activity",1);
            mContext.startActivity(intent);

        });


    }




    @Override
    public int getItemCount() {
        return albumList.size();
    }
}

