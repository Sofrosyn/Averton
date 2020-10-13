package com.averton.Iplayer.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.averton.Iplayer.R;

public class GamesFragment extends Fragment {

    private View view;
    private CardView playChess;
    private CardView playFruitNinja;
    private CardView playCandyCrush;
    private CardView playWhot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

            view = inflater.inflate(R.layout.fragment_game,container,false);

            initView();
            initAction();
        return view;

    }

    private void initView() {


        this.playCandyCrush = view.findViewById(R.id.play_candyCrush);
        this.playChess = view.findViewById(R.id.play_Chess);
        this.playFruitNinja = view.findViewById(R.id.play_fruitNinja);
        this.playWhot = view.findViewById(R.id.play_whot);

    }

    private void initAction(){

        playWhot.setOnClickListener(v -> {launchGames("com.tonierlro.naijawhotfree");});
        playFruitNinja.setOnClickListener(v -> {launchGames("com.halfbrick.fruitninjafree");});
        playChess.setOnClickListener(v -> {launchGames("uk.co.aifactory.chessfree");});
        playCandyCrush.setOnClickListener(v -> {launchGames("com.king.candycrushsaga");});

    }


    private void launchGames(String appName){
        Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(appName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(getActivity(), "There is no package available in android", Toast.LENGTH_LONG).show();
        }

    }

}
