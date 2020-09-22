package com.virmana.Iplayer.ui.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.Utils.MetadataExtractor;
import com.virmana.Iplayer.entity.Music;
import com.virmana.Iplayer.ui.activities.MusicPlayerActivity;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

        private Context mContext;
        private int bitmap;
        public ArrayList<Music> albumList;

        private MetadataExtractor extractor;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, artist;
            public ImageView thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                artist = view.findViewById(R.id.artist);
                thumbnail = view.findViewById(R.id.musicThumbnail);
//                overflow = (ImageView) view.findViewById(R.id.overflow);
            }
        }


        public MusicAdapter(Context mContext, ArrayList<Music> albumList, int bitmap) {
            this.mContext = mContext;
            this.albumList = albumList;
            this.bitmap = bitmap;
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
//            File thumbnail = new File(Environment.getExternalStorageDirectory()+album.getArtistThumbnail()+"/Albumart.jpg");
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.album_placeholder)
                                                .error(R.drawable.album_placeholder).fitCenter();




            Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(extractor.getCoverArt(album.getArtistPath())).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(v -> {

                        //added new
                    Intent intent = new Intent(mContext, MusicPlayerActivity.class );

                    intent.putExtra("musicPath",album.getArtistPath());

                    intent.putExtra("Activity",1);
                    mContext.startActivity(intent);

            });

            // loading album cover using picasso library
         //   Picasso.with(mContext).load(album.getArtistThumbnail()).into(holder.thumbnail);
/*
            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow);
                }
            });*/
        }

        /**
         * Showing popup menu when tapping on 3 dots
         */
      /*
        private void showPopupMenu(View view) {
            // inflate menu
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_album, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
            popup.show();
        }

   */     /**
         * Click listener for popup menu items
         */
       /* class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            public MyMenuItemClickListener() {
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_add_favourite:
                        Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_play_next:
                        Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        }*/



    @Override
        public int getItemCount() {
            return albumList.size();
        }
    }

