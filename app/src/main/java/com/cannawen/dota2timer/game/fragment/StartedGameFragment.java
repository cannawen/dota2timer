package com.cannawen.dota2timer.game.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.adapter.ConfigurationAdapter;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.game.interfaces.Game;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;


public class StartedGameFragment extends Fragment {
    @BindView(R.id.activity_game_text_time)
    TextView timeText;
    @BindView(R.id.activity_game_button_play_or_pause)
    Button playPauseButton;
    @BindView(R.id.activity_game_recycler_view_settings)
    RecyclerView recyclerView;
    private String time;
    private List<String> eventStrings;
    private boolean paused;
    private Configuration configuration;
    private Game game;
    private FragmentListener listener;
    private TextToSpeech tts;

    public void configure(Configuration configuration, Game game, FragmentListener listener, String time, List<String> eventStrings, boolean paused) {
        this.configuration = configuration;
        this.game = game;
        this.listener = listener;
        this.time = time;
        this.eventStrings = eventStrings;
        this.paused = paused;

        show(time, eventStrings, paused);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_started, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.tts = new TextToSpeech(getActivity(), null);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new ConfigurationAdapter(getActivity(), configuration, false));

        show(time, eventStrings, paused);
    }

    public void updateConfiguration(Configuration configuration) {
        this.configuration = configuration;
        ((ConfigurationAdapter) recyclerView.getAdapter()).updateConfiguration(configuration);
    }

    private void endGame() {
        listener.createNewGame();
    }

    @OnClick(R.id.activity_game_button_play_or_pause)
    public void pauseOrResume(View view) {
        game.pauseOrResume();
    }

    @OnClick(R.id.activity_game_button_time_increase)
    public void increaseTime(View view) {
        game.increaseTime();
    }

    @OnClick(R.id.activity_game_button_time_decrease)
    public void decreaseTime(View view) {
        game.decreaseTime();
    }

    @OnClick(R.id.activity_game_button_end)
    public void confirmEndGame(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage(R.string.game_action_end_confirmation_message);
        alert.setPositiveButton(R.string.game_action_end_confirmation_button_positive, (dialog, which) -> {
            endGame();
            dialog.dismiss();
        });
        alert.setNegativeButton(R.string.game_action_end_confirmation_button_negative, (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    public void show(String time, List<String> eventStrings, boolean paused) {
        if (eventStrings != null && eventStrings.size() != 0) {
            Stream.of(eventStrings).forEach(eventString -> tts.speak(eventString, QUEUE_ADD, null, eventString));
        }

        if (timeText != null) {
            timeText.setText(time);
        }
        if (playPauseButton != null) {
            @StringRes int buttonStringRes = paused ? R.string.game_action_resume : R.string.game_action_pause;
            playPauseButton.setText(buttonStringRes);
        }
    }

    public interface FragmentListener {
        void createNewGame();
    }
}
