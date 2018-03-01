package com.cannawen.dota2timer.activity.game;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.activity.configuration.ConfigurationActivity;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader;
import com.cannawen.dota2timer.configuration.loading.LocalConfigurationLoader;
import com.cannawen.dota2timer.game.DotaGame;
import com.cannawen.dota2timer.game.fragment.StartedGameFragment;
import com.cannawen.dota2timer.game.fragment.UnstartedGameFragment;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.timer.AbstractTimer;
import com.cannawen.dota2timer.timer.SecondTimer;

import java.util.List;

public class GameActivity extends Activity implements ConfigurationLoader.ConfigurationLoaderStatusListener, StartedGameFragment.FragmentListener {
    private static final int EDIT_CONFIGURATION_ACTIVITY_RESULT = 0;
    DotaGamePresenter presenter;
    private Game game;
    private Configuration configuration; //TODO not ideal to have this state saved here as well as in GameState

    private UnstartedGameFragment unstartedGameFragment;
    private StartedGameFragment startedGameFragment;
    private AbstractTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupExternalDependencies();
    }

    private void setupExternalDependencies() {
        ConfigurationLoader configurationLoader = new LocalConfigurationLoader(getApplicationContext());
        presenter = new DotaGamePresenter();
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
            startedGameFragment.updateConfiguration(configuration);
        }
    }

    @Override
    public void onLoadConfigurationSuccess(Configuration configuration) {
        GameActivity.this.configuration = configuration;
        createNewGame();
    }

    @Override
    public void createNewGame() {
        if (game != null) {
            game.end();
            game.reset();
        }

        game.setConfiguration(configuration);
        timer.setListener(game);
    }

    @Override
    public void onLoadConfigurationFailure(Exception e) {
        e.printStackTrace();
    }

    class DotaGamePresenter implements GameActivityViewModel.GamePresenter {
        @Override
        public void showUnstartedGameView() {
            if (unstartedGameFragment == null) {
                unstartedGameFragment = new UnstartedGameFragment();
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            unstartedGameFragment.configure(game);
            ft.replace(R.id.game_activity_fragment_container, unstartedGameFragment);
            ft.commitAllowingStateLoss();
        }

        @Override
        public void showPlayingGameView(final String time, final List<String> eventStrings) {
            if (startedGameFragment == null) {
                startedGameFragment = new StartedGameFragment();
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            startedGameFragment.configure(configuration, game, GameActivity.this,
                    time, eventStrings, false);
            ft.replace(R.id.game_activity_fragment_container, startedGameFragment);
            ft.commitAllowingStateLoss();
        }

        @Override
        public void showPausedGameView(final String time) {
            if (startedGameFragment == null) {
                startedGameFragment = new StartedGameFragment();
            }
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            startedGameFragment.configure(configuration, game, GameActivity.this, time, null, true);
            ft.replace(R.id.game_activity_fragment_container, startedGameFragment);
            ft.commitAllowingStateLoss();
        }

        @Override
        public void showFinishedGameView() {
            showUnstartedGameView();
        }
    }
}
