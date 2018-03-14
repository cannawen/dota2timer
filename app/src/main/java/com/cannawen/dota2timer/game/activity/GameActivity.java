package com.cannawen.dota2timer.game.activity;

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
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.activity.ConfigurationActivity;
import com.cannawen.dota2timer.configuration.adapter.ConfigurationAdapter;
import com.cannawen.dota2timer.configuration.creation.ConfigurationLoader;
import com.cannawen.dota2timer.configuration.creation.ConfigurationLoader.ConfigurationLoaderStatusListener;
import com.cannawen.dota2timer.configuration.creation.LocalConfigurationLoader;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.game.GameManager;
import com.cannawen.dota2timer.game.activity.viewmodel.GameActivityViewModel;
import com.cannawen.dota2timer.game.model.DotaGame;
import com.cannawen.dota2timer.game.model.interfaces.Game;
import com.cannawen.dota2timer.timer.AbstractTimer;
import com.cannawen.dota2timer.timer.SecondTimer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity implements ConfigurationLoaderStatusListener {
    private static final int EDIT_CONFIGURATION_ACTIVITY_RESULT = 0;
    private GameManager gameManager = GameManager.getInstance();
    private AbstractTimer timer;
    private Configuration configuration; //TODO not ideal to have this state saved here as well as in GameState
    private TextToSpeech tts;
    ConfigurationAdapter adapter;

    @BindView(R.id.activity_game_recycler_view_settings)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        adapter = new ConfigurationAdapter(getApplicationContext(), false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(adapter);

        setupExternalDependencies();

        new DotaGameInteractionHandler(this);
    }

    private void setupExternalDependencies() {
        tts = new TextToSpeech(getApplicationContext(), null);
        ConfigurationLoader configurationLoader = new LocalConfigurationLoader(getApplicationContext());
        AbstractTimer timer = new SecondTimer();
        Game game = new DotaGame(new GameActivityViewModel(new DotaGamePresenter(this)));
        GameManager.getInstance().setGame(game);

        initWithDependencies(configurationLoader, timer);
    }

    void initWithDependencies(ConfigurationLoader loader, AbstractTimer timer) {
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
            gameManager.getGame().setConfiguration(configuration);

            adapter.setConfiguration(configuration);
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
        configuration.removeTransitoryEvents();

        gameManager.resetGame(configuration);
        adapter.setConfiguration(configuration);
        timer.setListener(gameManager.getGame());
    }

    class DotaGameInteractionHandler {
        final private Context context;

        public DotaGameInteractionHandler(Activity activity) {
            context = activity;
            ButterKnife.bind(this, activity);
        }

        @OnClick(R.id.activity_game_text_time)
        public void editTime() {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);

            View dialogEditTimeView = getLayoutInflater().inflate(R.layout.dialog_edit_time_view, null);
            alert.setView(dialogEditTimeView);

            alert.setNegativeButton("Set (pre-horn)", (dialog, which) -> {
                Integer parsedTime = getInputTime(dialogEditTimeView);
                if (parsedTime != null) {
                    gameManager.getGame().updateTime(-parsedTime);
                    timer.syncSecond();
                }
                dialog.dismiss();
            });

            alert.setPositiveButton("Set", (dialog, which) -> {
                Integer parsedTime = getInputTime(dialogEditTimeView);
                if (parsedTime != null) {
                    gameManager.getGame().updateTime(parsedTime);
                    timer.syncSecond();
                }
                dialog.dismiss();
            });

            alert.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
            alert.show();
        }

        private Integer getInputTime(View view) {
            try {
                EditText minuteEditText = view.findViewById(R.id.dialog_edit_time_minute_input);
                String minuteString = minuteEditText.getText().toString();
                int minutes = 0;
                if (minuteString.length() > 0) {
                    minutes = Integer.valueOf(minuteString);
                }

                EditText secondEditText = view.findViewById(R.id.dialog_edit_time_second_input);
                String secondString = secondEditText.getText().toString();
                int seconds = 0;
                if (secondString.length() > 0) {
                    seconds = Integer.valueOf(secondString);
                }

                return minutes * 60 + seconds;
            } catch (Exception e) {
                return null;
            }
        }


        @OnClick(R.id.activity_game_button_time_increase)
        public void increaseTime() {
            gameManager.getGame().increaseTime();
            timer.syncSecond();
        }

        @OnClick(R.id.activity_game_button_time_decrease)
        public void decreaseTime() {
            gameManager.getGame().decreaseTime();
            timer.syncSecond();
        }

        @OnClick(R.id.activity_game_button_play_or_pause)
        public void pauseOrResume() {
            gameManager.getGame().pauseOrResume();
        }

        @OnClick(R.id.activity_game_button_start_or_end)
        public void startOrEndGame() {
            if (gameManager.getGame().hasStarted()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.game_action_end_confirmation_message);
                alert.setPositiveButton(R.string.game_action_end_confirmation_button_positive, (dialog, which) -> {
                    createNewGame(configuration);
                    dialog.dismiss();
                });
                alert.setNegativeButton(R.string.game_action_end_confirmation_button_negative, (dialog, which) -> dialog.dismiss());
                alert.show();
            } else {
                gameManager.getGame().start();
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

            adapter.notifyDataSetChanged();
        }

        @Override
        public void showPlayingGameView(final String time, final List<String> eventStrings) {
            Stream.of(eventStrings).forEach(eventString -> tts.speak(eventString, QUEUE_ADD, null, eventString));
            configureStartedGameView(time, false);

            adapter.notifyDataSetChanged();
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

            adapter.notifyDataSetChanged();
        }
    }
}
