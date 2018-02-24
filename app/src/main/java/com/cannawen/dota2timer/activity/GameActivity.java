package com.cannawen.dota2timer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoaderListener;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGame;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import java.util.List;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity  {

    private Game game;

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tts = new TextToSpeech(getApplicationContext(), null);
        createNewGame();
    }

    public void startGame(View view) {
        game.start();
    }

    private void stopGame() {
        game.stop();
        createNewGame();
    }

    public void pauseOrResume(View view) {
        game.pauseOrResume();
    }

    public void increaseTime(View view) {
        game.increaseTime();
    }

    public void decreaseTime(View view) {
        game.decreaseTime();
    }

    public void confirmIfShouldStopGame(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm Reset Game?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopGame();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                game = new DotaGame(configuration, new GameActivityViewModel());
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void configurePlayingGameView(String time, List<String> events) {
        for (String eventString : events) {
            tts.speak(eventString, QUEUE_ADD, null, eventString);
        }

        configureStartedGameView(time, false);
    }

    private void configurePausedGameView(String time) {
        configureStartedGameView(time, true);
    }

    private void configureStartedGameView(String time, boolean paused) {
        findViewById(R.id.game_not_started_view).setVisibility(View.INVISIBLE);

        findViewById(R.id.game_started_view).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.time_text)).setText(time);
        @StringRes int buttonStringRes = paused ? R.string.game_action_resume : R.string.game_action_pause;
        ((TextView) findViewById(R.id.play_pause_button)).setText(buttonStringRes);
        ((TextView) findViewById(R.id.reset_button)).setText(R.string.game_action_reset);
    }

    private void showNonStartedGame() {
        ((TextView) findViewById(R.id.time_text)).setText("");
        findViewById(R.id.game_not_started_view).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.game_not_started_view)).setText(R.string.game_action_start);
        findViewById(R.id.game_started_view).setVisibility(View.INVISIBLE);
    }

    public class GameActivityViewModel implements GameStateChangeListener {
        @Override
        public void gameStateChanged(final GameState gameState) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (gameState.getState()) {
                        case GameState.State.PLAYING: {
                            configurePlayingGameView(timeString(gameState.elapsedTime()), gameState.events());
                            break;
                        }
                        case GameState.State.PAUSED: {
                            configurePausedGameView(timeString(gameState.elapsedTime()));
                            break;
                        }
                        case GameState.State.UNSTARTED:
                        case GameState.State.FINISHED:
                        default: {
                            showNonStartedGame();
                            break;
                        }
                    }
                }
            });
        }

        private String timeString(int secondsElapsed) {
            String signString = "";
            if (secondsElapsed < 0) {
                signString = "-";
                secondsElapsed = secondsElapsed * -1;
            }

            int hours = secondsElapsed / 3600;
            final int minutes = (secondsElapsed % 3600) / 60;
            int seconds = secondsElapsed % 60;

            String timeSeparator = getResources().getString(R.string.game_time_separator);
            return String.format("%s%02d%s%02d%s%02d", signString, hours, timeSeparator, minutes, timeSeparator, seconds);
        }
    }
}
