package com.averton.Iplayer.ui.Adapters;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.Holder> {



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
