package com.averton.Iplayer.ui.activities;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.averton.Iplayer.R;
import com.averton.Iplayer.ui.fragments.*;

public class HomeActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private FragmentTransaction transaction;
    private FrameLayout container;
    private BottomNavigationView bottomNavigationView;

    private String toolBarTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottom_nav_home);
        container = findViewById(R.id.home_activity_framelayout);
        switchFragment(new HomeFragment());
        initViews();
    }

    private void initViews(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    switchFragment(new HomeFragment());
                    return true;

                case R.id.nav_movies:
                    switchFragment(new MoviesFragment());
                    return true;
                case R.id.nav_music:
                    switchFragment(new MusicFragment());
                    return true;

                case R.id.nav_sport:
                    switchFragment(new SportsFragment());
                    return true;
                case R.id.nav_news:
                    switchFragment(new NewsFragment());
                    return true;

            }
            return false;
        });
    }


    private void switchFragment(Fragment fragment){
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_activity_framelayout,fragment);

        transaction.commit();

    }
}
