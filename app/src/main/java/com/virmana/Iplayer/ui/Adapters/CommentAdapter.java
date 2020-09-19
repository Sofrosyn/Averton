package com.virmana.Iplayer.ui.Adapters;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.virmana.Iplayer.R;
import com.virmana.Iplayer.entity.Comment;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tamim on 16/01/2018.
 */


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private List<Comment> commentList= new ArrayList<>();
    private Context context;
    public CommentAdapter(List<Comment> commentList, Context context){
        this.context=context;
        this.commentList=commentList;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, null, false);
        viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new CommentAdapter.CommentHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder,final int position) {
        holder.text_view_time_item_comment.setText(commentList.get(position).getCreated());

        byte[] data = Base64.decode(commentList.get(position).getContent(), Base64.DEFAULT);
        String Comment_text = "";
        Comment_text = new String(data, StandardCharsets.UTF_8);

        holder.text_view_name_item_comment.setText(commentList.get(position).getUser());
        Picasso.with(context).load(commentList.get(position).getImage()).error(R.drawable.placeholder_profile).placeholder(R.drawable.placeholder_profile).into(holder.image_view_comment_iten);
        if (!commentList.get(position).getEnabled()){
            holder.text_view_content_item_comment.setText(context.getResources().getString(R.string.comment_hidden));
            holder.text_view_content_item_comment.setTextColor(context.getResources().getColor(R.color.gray));
        }else{
            holder.text_view_content_item_comment.setText(Comment_text);
        }

    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentHolder extends RecyclerView.ViewHolder {
        private final TextView text_view_name_item_comment;
        private final TextView text_view_time_item_comment;
        private final TextView text_view_content_item_comment;
        private final CircleImageView image_view_comment_iten;
        public CommentHolder(View itemView) {
            super(itemView);
            this.image_view_comment_iten= itemView.findViewById(R.id.image_view_comment_iten);
            this.text_view_name_item_comment= itemView.findViewById(R.id.text_view_name_item_comment);
            this.text_view_time_item_comment= itemView.findViewById(R.id.text_view_time_item_comment);
            this.text_view_content_item_comment= itemView.findViewById(R.id.text_view_content_item_comment);
        }
    }
}
