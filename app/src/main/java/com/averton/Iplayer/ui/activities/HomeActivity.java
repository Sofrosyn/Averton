package com.averton.Iplayer.ui.activities;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.averton.Iplayer.R;
import com.averton.Iplayer.ui.fragments.DocumentaryFragment;
import com.averton.Iplayer.ui.fragments.GamesFragment;
import com.averton.Iplayer.ui.fragments.MoviesFragment;
import com.averton.Iplayer.ui.fragments.MusicFragment;
import com.averton.Iplayer.ui.fragments.NewsFragment;
import com.averton.Iplayer.ui.fragments.SportsFragment;

public class HomeActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private FragmentTransaction transaction;
    private FrameLayout container;

    private ImageButton movies;
    private ImageButton documentary;
    private ImageButton music;
    private ImageButton sports;
    private ImageButton books;
    private ImageButton games;

    private String toolBarTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        container = findViewById(R.id.home_activity_framelayout);

        movies = findViewById(R.id.home_activity_movies);
        documentary = findViewById(R.id.home_activity_documetary);
        music = findViewById(R.id.home_activity_music);
        sports = findViewById(R.id.home_activity_sports);
        books = findViewById(R.id.home_activity_more);
        games = findViewById(R.id.home_activity_games);



        switchFragment(new MoviesFragment());
        movies.setBackgroundColor(getResources().getColor(R.color.image_button));
        initViews();


    }

    private void initViews(){



        movies.setOnClickListener(v -> {switchFragment(new MoviesFragment());

                movies.setBackgroundColor(getResources().getColor(R.color.image_button));
                music.setBackgroundColor(getResources().getColor(R.color.transparent));
                sports.setBackgroundColor(getResources().getColor(R.color.transparent));
                books.setBackgroundColor(getResources().getColor(R.color.transparent));
                documentary.setBackgroundColor(getResources().getColor(R.color.transparent));
                games.setBackgroundColor(getResources().getColor(R.color.transparent));

        });



        documentary.setOnClickListener(v -> {switchFragment(new DocumentaryFragment());

            movies.setBackgroundColor(getResources().getColor(R.color.transparent));
            music.setBackgroundColor(getResources().getColor(R.color.transparent));
            sports.setBackgroundColor(getResources().getColor(R.color.transparent));
            books.setBackgroundColor(getResources().getColor(R.color.transparent));
            documentary.setBackgroundColor(getResources().getColor(R.color.image_button));
            games.setBackgroundColor(getResources().getColor(R.color.transparent));

        });



        music.setOnClickListener( v ->{ switchFragment(new MusicFragment());

            movies.setBackgroundColor(getResources().getColor(R.color.transparent));
            music.setBackgroundColor(getResources().getColor(R.color.image_button));
            sports.setBackgroundColor(getResources().getColor(R.color.transparent));
            books.setBackgroundColor(getResources().getColor(R.color.transparent));
            documentary.setBackgroundColor(getResources().getColor(R.color.transparent));
            games.setBackgroundColor(getResources().getColor(R.color.transparent));



        });





        sports.setOnClickListener(v -> {switchFragment(new SportsFragment());

            movies.setBackgroundColor(getResources().getColor(R.color.transparent));
            music.setBackgroundColor(getResources().getColor(R.color.transparent));
            sports.setBackgroundColor(getResources().getColor(R.color.image_button));
            books.setBackgroundColor(getResources().getColor(R.color.transparent));
            documentary.setBackgroundColor(getResources().getColor(R.color.transparent));
            games.setBackgroundColor(getResources().getColor(R.color.transparent));


        });



        books.setOnClickListener(v ->{ switchFragment(new NewsFragment());

            movies.setBackgroundColor(getResources().getColor(R.color.transparent));
            music.setBackgroundColor(getResources().getColor(R.color.transparent));
            sports.setBackgroundColor(getResources().getColor(R.color.transparent));
            books.setBackgroundColor(getResources().getColor(R.color.image_button));
            documentary.setBackgroundColor(getResources().getColor(R.color.transparent));
            games.setBackgroundColor(getResources().getColor(R.color.transparent));


        });


        games.setOnClickListener(v -> {

            switchFragment(new GamesFragment());

            movies.setBackgroundColor(getResources().getColor(R.color.transparent));
            music.setBackgroundColor(getResources().getColor(R.color.transparent));
            sports.setBackgroundColor(getResources().getColor(R.color.transparent));
            games.setBackgroundColor(getResources().getColor(R.color.image_button));
            documentary.setBackgroundColor(getResources().getColor(R.color.transparent));
            books.setBackgroundColor(getResources().getColor(R.color.transparent));


        });



    }


    private void switchFragment(Fragment fragment){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_activity_framelayout,fragment);

        transaction.commit();

    }
}
