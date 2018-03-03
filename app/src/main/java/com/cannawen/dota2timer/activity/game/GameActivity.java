package com.cannawen.dota2timer.activity.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.activity.configuration.ConfigurationActivity;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.adapter.ConfigurationAdapter;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader.ConfigurationLoaderStatusListener;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGame;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.timer.AbstractTimer;
import com.cannawen.dota2timer.timer.SecondTimer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity implements ConfigurationLoaderStatusListener {
    private static final int EDIT_CONFIGURATION_ACTIVITY_RESULT = 0;
    DotaGamePresenter presenter;
    private Game game;
    private AbstractTimer timer;
    private Configuration configuration; //TODO not ideal to have this state saved here as well as in GameState
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupExternalDependencies();

        new DotaGameInteractionHandler(this);
    }

    private void setupExternalDependencies() {
        tts = new TextToSpeech(getApplicationContext(), null);
        ConfigurationLoader configurationLoader = new LocalConfigurationLoader(getApplicationContext());
        presenter = new DotaGamePresenter(this);
        Game game = new DotaGame(new GameActivityViewModel(presenter));
        AbstractTimer timer = new SecondTimer();

        initWithDependencies(configurationLoader, game, timer);
    }

    void initWithDependencies(ConfigurationLoader loader, Game game, AbstractTimer timer) {
        this.game = game;
        this.timer = timer;

        loader.getConfiguration(this);
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
            game.reset();
        }

        RecyclerView recyclerView = findViewById(R.id.activity_game_recycler_view_settings);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new ConfigurationAdapter(getApplicationContext(), configuration, false));

        game.setConfiguration(configuration);
        timer.setListener(game);
    }

    class DotaGameInteractionHandler {
        final private Context context;

        public DotaGameInteractionHandler(Activity activity) {
            context = activity;
            ButterKnife.bind(this, activity);
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

        @OnClick(R.id.activity_game_button_start_or_end)
        public void confirmEndGame() {
            if (game.hasStarted()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.game_action_end_confirmation_message);
                alert.setPositiveButton(R.string.game_action_end_confirmation_button_positive, (dialog, which) -> {
                    endGame();
                    dialog.dismiss();
                });
                alert.setNegativeButton(R.string.game_action_end_confirmation_button_negative, (dialog, which) -> dialog.dismiss());
                alert.show();
            } else {
                game.start();
            }
        }
    }

    class DotaGamePresenter implements GameActivityViewModel.GamePresenter {
        @BindView(R.id.activity_game_text_time)
        TextView timeText;
        @BindView(R.id.activity_game_button_play_or_pause)
        Button playPauseButton;
        @BindView(R.id.activity_game_button_start_or_end)
        Button startEndButton;

        public DotaGamePresenter(Activity activity) {
            ButterKnife.bind(this, activity);
        }

        @Override
        public void showUnstartedGameView(String time) {
            timeText.setText(time);
            playPauseButton.setVisibility(View.INVISIBLE);
            startEndButton.setText(R.string.game_action_start);
        }

        @Override
        public void showPlayingGameView(final String time, final List<String> eventStrings) {
            Stream.of(eventStrings).forEach(eventString -> tts.speak(eventString, QUEUE_ADD, null, eventString));
            configureStartedGameView(time, false);
        }

        @Override
        public void showPausedGameView(final String time) {
            configureStartedGameView(time, true);
        }

        @Override
        public void showFinishedGameView(String time) {
            showUnstartedGameView(time);
        }

        private void configureStartedGameView(String time, boolean paused) {
            timeText.setText(time);
            @StringRes int buttonStringRes = paused ? R.string.game_action_resume : R.string.game_action_pause;
            playPauseButton.setText(buttonStringRes);
            startEndButton.setText(R.string.game_action_end);
            playPauseButton.setVisibility(View.VISIBLE);
        }
    }
}
