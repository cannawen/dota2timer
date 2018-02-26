package com.cannawen.dota2timer.activity.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.activity.configuration.ConfigurationActivity;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader.ConfigurationLoaderStatusListener;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGame;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.timer.SecondTimer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity implements ConfigurationLoaderStatusListener {
    private static final int EDIT_CONFIGURATION_ACTIVITY_RESULT = 0;

    private Game game;
    private SecondTimer timer;
    private Configuration configuration; //TODO not ideal to have this state saved here as well as in GameState
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tts = new TextToSpeech(getApplicationContext(), null);

        new LocalConfigurationLoader(getApplicationContext()).getConfiguration(this);
        new DotaGameInteractionHandler(this);
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
                Intent intent = new Intent(this, ConfigurationActivity.class);
                ConfigurationActivity.configureIntent(intent, configuration);
                startActivityForResult(intent, EDIT_CONFIGURATION_ACTIVITY_RESULT);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == EDIT_CONFIGURATION_ACTIVITY_RESULT) {
            configuration = ConfigurationActivity.deserializeConfigurationFromIntent(data);
            game.setConfiguration(configuration);
        }
    }

    @Override
    public void onLoadConfigurationSuccess(Configuration configuration) {
        GameActivity.this.configuration = configuration;
        createNewGame(configuration);
    }

    @Override
    public void onLoadConfigurationFailure(Exception e) {
        e.printStackTrace();
    }

    private void createNewGame(Configuration configuration) {
        if (game != null) {
            game.end();
        }
        if (timer != null) {
            timer.cancel();
        }
        game = new DotaGame(configuration, new GameActivityViewModel(new DotaGamePresenter(this)));
        timer = new SecondTimer(game);
    }

    class DotaGameInteractionHandler {
        final private Context context;

        public DotaGameInteractionHandler(Activity activity) {
            context = activity;
            ButterKnife.bind(this, activity);
        }

        @OnClick(R.id.activity_game_button_start)
        public void startGame() {
            game.start();
        }

        private void endGame() {
            createNewGame(configuration);
        }

        @OnClick(R.id.activity_game_button_play_or_pause)
        public void pauseOrResume() {
            game.pauseOrResume();
        }

        @OnClick(R.id.activity_game_button_time_increase)
        public void increaseTime() {
            game.increaseTime();
        }

        @OnClick(R.id.activity_game_button_time_decrease)
        public void decreaseTime() {
            game.decreaseTime();
        }

        @OnClick(R.id.activity_game_button_end)
        public void confirmEndGame() {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage(R.string.game_action_end_confirmation_message);
            alert.setPositiveButton(R.string.game_action_end_confirmation_button_positive, (dialog, which) -> {
                endGame();
                dialog.dismiss();
            });
            alert.setNegativeButton(R.string.game_action_end_confirmation_button_negative, (dialog, which) -> dialog.dismiss());
            alert.show();
        }
    }

    class DotaGamePresenter implements GameActivityViewModel.GamePresenter {
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

        public DotaGamePresenter(Activity activity) {
            ButterKnife.bind(this, activity);
        }

        @Override
        public void showUnstartedGameView() {
            runOnUiThread(() -> {
                timeText.setText("");
                gameNotStartedView.setVisibility(View.VISIBLE);
                startButton.setText(R.string.game_action_start);
                gameStartedView.setVisibility(View.GONE);
            });
        }

        @Override
        public void showPlayingGameView(final String time, final List<String> eventStrings) {
            runOnUiThread(() -> {
                Stream.of(eventStrings).forEach(eventString -> tts.speak(eventString, QUEUE_ADD, null, eventString));
                configureStartedGameView(time, false);
            });
        }

        @Override
        public void showPausedGameView(final String time) {
            runOnUiThread(() -> configureStartedGameView(time, true));
        }

        @Override
        public void showFinishedGameView() {
            showUnstartedGameView();
        }

        private void configureStartedGameView(String time, boolean paused) {
            gameNotStartedView.setVisibility(View.GONE);

            gameStartedView.setVisibility(View.VISIBLE);

            timeText.setText(time);
            @StringRes int buttonStringRes = paused ? R.string.game_action_resume : R.string.game_action_pause;
            playPauseButton.setText(buttonStringRes);
            resetButton.setText(R.string.game_action_end);
        }
    }
}
