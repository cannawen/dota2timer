package com.cannawen.dota2timer.game.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.game.interfaces.Game;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UnstartedGameFragment extends Fragment {

    @BindView(R.id.activity_game_button_start)
    Button startButton;

    private Game game;

    public void configure(Game game) {
        this.game = game;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_unstarted, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.activity_game_button_start)
    public void startGame() {
        game.start();
    }
}
