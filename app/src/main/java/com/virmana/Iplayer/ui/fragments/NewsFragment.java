package com.virmana.Iplayer.ui.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.virmana.Iplayer.Provider.PrefManager;
import com.virmana.Iplayer.R;
import com.virmana.Iplayer.entity.*;
import com.virmana.Iplayer.ui.Adapters.ChannelAdapter;
import com.virmana.Iplayer.ui.Adapters.MusicAdapter;
import com.virmana.Iplayer.ui.Adapters.NewsAdapter;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private View view;
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsArrayList;
    private RecyclerView recycler_view_news_fragment;




    public NewsFragment() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_news, container, false);

        initView();
        return view;
    }


    private void initView() {


        recycler_view_news_fragment = view.findViewById(R.id.recycler_view_news_fragment);


loadNews();



    }

    private void loadNews(){

        newsArrayList = new ArrayList<>();

        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String where = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String[] args = new String[]{pdf};
        Uri table = MediaStore.Files.getContentUri("external");

        String[] projection = {MediaStore.Files.FileColumns.DATA
        };

        Cursor c = getActivity().getContentResolver().query(table, projection,where, args, null);

        if (c != null) {
            while (c.moveToNext()) {

                News news = new News();
                String path = c.getString(0);

                String name = path.substring(path.lastIndexOf("/") + 1);
                String dName = name.substring(0,name.lastIndexOf(".pdf")+1);
                news.setNewsHeadline(dName);
                news.setNewsPath(path);

                Log.v(" News Name :%s",dName);
                Log.v(" News path :%s",path);


                newsArrayList.add(news);
                //             Log.v(" Track :%s", track);


            }

            c.close();
        }
        final int columns = getResources().getInteger(R.integer.grid_column);
        newsAdapter = new NewsAdapter(getActivity(),newsArrayList);
        recycler_view_news_fragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view_news_fragment.setItemAnimator(new DefaultItemAnimator());
        recycler_view_news_fragment.setHasFixedSize(true);
        recycler_view_news_fragment.setVerticalScrollBarEnabled(true);
        recycler_view_news_fragment.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
        recycler_view_news_fragment.setVerticalFadingEdgeEnabled(true);



        recycler_view_news_fragment.setAdapter(newsAdapter);


        Log.v("Adapter","adapter showing");
        // recycler_view_series_fragment.setLayoutManager(gridLayoutManager);     }


    }


}
