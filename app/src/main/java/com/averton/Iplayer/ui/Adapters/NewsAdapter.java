package com.averton.Iplayer.ui.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.MetadataExtractor;
import com.averton.Iplayer.entity.News;
import com.averton.Iplayer.ui.activities.ReadNewsActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.viewHolder> {

    private final Context mContext;
    private final ArrayList<News> newsArrayList;
    private MetadataExtractor extractor;


    public NewsAdapter(Context mContext, ArrayList<News> newsArrayList) {
        this.mContext = mContext;
        this.newsArrayList = newsArrayList;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
            public final TextView headline;
            public final TextView description;
            public final ImageView circleImageView;
            public final CardView newsCard;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.news_headlines);
            description = itemView.findViewById(R.id.news_description);
            circleImageView = itemView.findViewById(R.id.news_images);
            newsCard = itemView.findViewById(R.id.news_card);
        }
    }

    @NonNull
    @Override
    public NewsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news_fragment,parent,false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.viewHolder holder, int position) {

        News news  = newsArrayList.get(position);
        extractor = new MetadataExtractor();
        holder.headline.setText(news.getNewsHeadline());

/*
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.book_cover).dontAnimate().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.book_cover).fitCenter();

        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(Paths.imagesNewsThumbnail+news.getNewsHeadline()).thumbnail(0.5f).into(holder.circleImageView);
*/

/*TODO: Implement Background task for bitmap loading  */


        try {
            extractor.generatePdfThumbnail(holder.circleImageView,mContext, Uri.fromFile(new File(news.getNewsPath())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        holder.newsCard.setOnClickListener(v -> {
        //    Toast.makeText(mContext,news.getNewsPath(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, ReadNewsActivity.class);
            intent.putExtra("newsPath",news.getNewsPath());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }


}
