package com.averton.Iplayer.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.averton.Iplayer.R;
import com.averton.Iplayer.entity.Music;
import com.averton.Iplayer.ui.activities.ExoPlayerActivity;

import java.util.ArrayList;

public class PlaylistAdapter  extends RecyclerView.Adapter<PlaylistAdapter.myViewHolder> {
    Context context;
    ArrayList<Music> playlist;




    public PlaylistAdapter(Context context, ArrayList<Music> playlist) {
        this.context = context;
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_music_playlist,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Music playlistItem = playlist.get(position);


        holder.songName.setText(playlistItem.getArtistSong());
        holder.artist.setText(playlistItem.getArtistName());
        holder.playItem.setOnClickListener( v -> {
            Toast.makeText(context.getApplicationContext(),playlistItem.getArtistName(),Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, ExoPlayerActivity.class );
            intent.putExtra("musicPath",playlistItem.getArtistPath());
            intent.putExtra("Activity",2);

            context.startActivity(intent);});



    }
/*item count*/
    @Override
    public int getItemCount() {

        return playlist.size();
    }

   /*inner class*/

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView songName, artist;
        public LinearLayout playItem;



        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.playlist_artist_name);
            songName = itemView.findViewById(R.id.playlist_song_name);
            playItem =itemView.findViewById(R.id.playlist_container);
        }
    }
}
