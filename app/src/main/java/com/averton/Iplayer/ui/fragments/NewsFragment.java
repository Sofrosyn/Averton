package com.averton.Iplayer.ui.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.averton.Iplayer.R;
import com.averton.Iplayer.Utils.Paths;
import com.averton.Iplayer.entity.News;
import com.averton.Iplayer.ui.Adapters.NewsAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    private View view;
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsArrayList;
    private RecyclerView recycler_view_news_international;
    private RecyclerView recycler_view_news_local;
    private RecyclerView recycler_view_news_fashion_magazine;
    private RecyclerView recycler_view_news_business_weekly;
    private RecyclerView recycler_view_news_books;




    public NewsFragment() {
        // Required empty public constructor
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


        recycler_view_news_international = view.findViewById(R.id.recycler_view_news_international);
        recycler_view_news_local = view.findViewById(R.id.recycler_view_news_local);
        recycler_view_news_books = view.findViewById(R.id.recycler_view_news_books);
        recycler_view_news_business_weekly = view.findViewById(R.id.recycler_view_news_business_weekly);
        recycler_view_news_fashion_magazine = view.findViewById(R.id.recycler_view_news_fashion_magazine);



        //            loadNews();

        loadNewsPath(recycler_view_news_books, Paths.newsBooks);
        loadNewsPath(recycler_view_news_business_weekly,Paths.newsBusinessWeek);
        loadNewsPath(recycler_view_news_fashion_magazine,Paths.newsFashion);
        loadNewsPath(recycler_view_news_international,Paths.newsInternational);
        loadNewsPath(recycler_view_news_local,Paths.newsLocal);


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

        newsAdapter = new NewsAdapter(getActivity(),newsArrayList);
        recycler_view_news_international.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recycler_view_news_international.setItemAnimator(new DefaultItemAnimator());
        recycler_view_news_international.setHasFixedSize(true);
        recycler_view_news_international.setVerticalScrollBarEnabled(true);
        recycler_view_news_international.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
        recycler_view_news_international.setVerticalFadingEdgeEnabled(true);



        recycler_view_news_international.setAdapter(newsAdapter);


        Log.v("Adapter","adapter showing");


    }

    private void loadNewsPath(RecyclerView recyclerView ,String newsPath) {



        newsArrayList = new ArrayList<>();

        String pdf = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String selection = MediaStore.Files.FileColumns.DATA +" like ? ";
        String[] selectionArgs =new String [] {"%"+newsPath+"%"};
        Uri table = MediaStore.Files.getContentUri("external");

        String[] projection = {MediaStore.Files.FileColumns.DATA,
        };

        Cursor c = getActivity().getContentResolver().query(table, projection,selection, selectionArgs, null);

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

        newsAdapter = new NewsAdapter(getActivity(),newsArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
        recyclerView.setVerticalFadingEdgeEnabled(true);



        recyclerView.setAdapter(newsAdapter);


        Log.v("Adapter","adapter showing");




    }


}
