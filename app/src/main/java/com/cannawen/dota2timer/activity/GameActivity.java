package com.cannawen.dota2timer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader.ConfigurationLoaderListener;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGame;
import com.cannawen.dota2timer.game.interfaces.Game;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity {
    @BindView(R.id.activity_game_container_not_started)
    View gameNotStartedView;
    @BindView(R.id.activity_game_button_start)
    Button startButton;
    @BindView(R.id.activity_game_container_started)
    View gameStartedView;
    @BindView(R.id.activity_game_text_time)
    TextView timeText;
    @BindView(R.id.activity_game_button_play_or_pause)
    Button playPauseButton;
    @BindView(R.id.activity_game_button_end)
    Button resetButton;
    private Game game;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        tts = new TextToSpeech(getApplicationContext(), null);
        createNewGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_game_edit_events: {
                Intent intent = new Intent(this, EditConfigurationActivity.class);
                startActivity(intent);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @OnClick(R.id.activity_game_button_start)
    public void startGame(View view) {
        game.start();
    }

    private void endGame() {
        game.end();
        createNewGame();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.game_action_end_confirmation_message);
        alert.setPositiveButton(R.string.game_action_end_confirmation_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                endGame();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(R.string.game_action_end_confirmation_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void createNewGame() {
        LocalConfigurationLoader loader = new LocalConfigurationLoader(getApplicationContext());
        loader.getConfiguration(new ConfigurationLoaderListener() {
            @Override
            public void onSuccess(Configuration configuration) {
                game = new DotaGame(configuration, new GameActivityViewModel(new DotaGamePresenter()));
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    class DotaGamePresenter implements GameActivityViewModel.GamePresenter {
        @Override
        public void showUnstartedGameView() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeText.setText("");
                    gameNotStartedView.setVisibility(View.VISIBLE);
                    startButton.setText(R.string.game_action_start);
                    gameStartedView.setVisibility(View.INVISIBLE);
                }
            });
        }

        @Override
        public void showPlayingGameView(final String time, final List<String> events) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (String eventString : events) {
                        tts.speak(eventString, QUEUE_ADD, null, eventString);
                    }

                    configureStartedGameView(time, false);
                }
            });
        }

        @Override
        public void showPausedGameView(final String time) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    configureStartedGameView(time, true);
                }
            });
        }

        @Override
        public void showFinishedGameView() {
            showUnstartedGameView();
        }

        private void configureStartedGameView(String time, boolean paused) {
            gameNotStartedView.setVisibility(View.INVISIBLE);

            gameStartedView.setVisibility(View.VISIBLE);

            timeText.setText(time);
            @StringRes int buttonStringRes = paused ? R.string.game_action_resume : R.string.game_action_pause;
            playPauseButton.setText(buttonStringRes);
            resetButton.setText(R.string.game_action_end);
        }
    }
}
