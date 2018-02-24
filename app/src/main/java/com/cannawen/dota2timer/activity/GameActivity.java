package com.cannawen.dota2timer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoaderListener;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGameController;
import com.cannawen.dota2timer.game.GameController;
import com.cannawen.dota2timer.game.GameDisplayer;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity {

    private GameController gameController;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tts = new TextToSpeech(getApplicationContext(), null);
        createNewGame();
        syncUIWithState();
    }

    public void startGame(View view) {
        gameController.start();
        syncUIWithState();
    }

    private void stopGame() {
        gameController.stop();
        createNewGame();
        syncUIWithState();
    }

    public void playOrPauseGame(View view) {
        if (gameController.getState() == GameController.State.PAUSED) {
            gameController.resume();
        } else {
            gameController.pause();
        }
        syncUIWithState();
    }

    public void increaseTime(View view) {
        gameController.increaseTime();
    }

    public void decreaseTime(View view) {
        gameController.decreaseTime();
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
                gameController = new DotaGameController(configuration, new MainGameDisplayer());
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        syncUIWithState();
    }

    private void syncUIWithState() {
        switch (gameController.getState()) {
            case GameController.State.PLAYING: {
                showGameStartedView();
                ((TextView) findViewById(R.id.play_pause_button)).setText(R.string.game_action_pause);
                break;
            }
            case GameController.State.PAUSED: {
                showGameStartedView();
                ((Button)findViewById(R.id.play_pause_button)).setText(R.string.game_action_resume);
                break;
            }
            case GameController.State.UNSTARTED:
            case GameController.State.FINISHED:
            default: {
                ((TextView) findViewById(R.id.time_text)).setText("");
                showGameNotStartedView();
                break;
            }
        }
    }

    private void showGameNotStartedView() {
        findViewById(R.id.game_not_started_view).setVisibility(View.VISIBLE);
        ((Button) findViewById(R.id.game_not_started_view)).setText(R.string.game_action_start);
        findViewById(R.id.game_started_view).setVisibility(View.INVISIBLE);
    }

    private void showGameStartedView() {
        findViewById(R.id.game_not_started_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.game_started_view).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.reset_button)).setText(R.string.game_action_reset);
    }

    class MainGameDisplayer implements GameDisplayer {
        @Override
        @SuppressLint("DefaultLocale")
        public void showTime(int secondsElapsed) {
            String signString = "";
            if (secondsElapsed < 0) {
                signString = "-";
                secondsElapsed = secondsElapsed * -1;
            }

            int hours = secondsElapsed / 3600;
            final int minutes = (secondsElapsed % 3600) / 60;
            int seconds = secondsElapsed % 60;

            String timeSeparator = getResources().getString(R.string.game_time_separator);
            final String timeString = String.format("%s%02d%s%02d%s%02d", signString, hours, timeSeparator, minutes, timeSeparator, seconds);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.time_text)).setText(timeString);
                }
            });
        }

        @Override
        public void notify(String eventString) {
            tts.speak(eventString, QUEUE_ADD, null, eventString);
        }
    }
}
