package com.cannawen.dota2timer.activity.game;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.loading.ConfigurationLoader;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.timer.AbstractTimer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.cannawen.dota2timer.activity.ActivityTestHelper.isGone;
import static com.cannawen.dota2timer.activity.ActivityTestHelper.isVisible;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> rule = new ActivityTestRule<>(GameActivity.class);

    ConfigurationLoader configurationLoader;
    Game game;
    AbstractTimer timer;

    GameActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();

        configurationLoader = mock(ConfigurationLoader.class);
        game = mock(Game.class);
        timer = mock(AbstractTimer.class);

        activity.initWithDependencies(configurationLoader, game, timer);

        activity.onLoadConfigurationSuccess(mock(Configuration.class));
    }

    @Test
    public void onCreate_verifyViewInState_notStarted() throws Exception {
        onView(withId(R.id.activity_game_container_not_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_start)).check(isVisible());

        onView(withId(R.id.activity_game_container_started)).check(isGone());
    }

    @Test
    @Ignore
    public void onPressStart_verifyGameNotified() {
        onView(withId(R.id.activity_game_button_start)).perform(click());
        verify(game).start();
    }

    @Test
    @Ignore
    public void onPressPauseOrResume_verifyGameNotified() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_button_play_or_pause)).perform(click());
        verify(game).pauseOrResume();
    }

    @Test
    @Ignore
    public void increaseTime_verifyGameNotified() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_button_time_increase)).perform(click());
        verify(game).increaseTime();
    }

    @Test
    @Ignore
    public void decreaseTime_verifyGameNotified() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_button_time_decrease)).perform(click());
        verify(game).decreaseTime();
    }

    @Test
    @Ignore
    public void endGame_confirmed_verifyGameNotified() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_button_end)).perform(click());
        onView(withText(R.string.game_action_end_confirmation_button_positive)).perform(click());
        verify(game).end();
    }

    @Test
    @Ignore
    public void endGame_cancelled_verifyGameNotified() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_button_end)).perform(click());
        onView(withText(R.string.game_action_end_confirmation_button_negative)).perform(click());
        verifyNoMoreInteractions(game);
    }

    @Test
    @Ignore
    public void showUnstartedGameView_verifyViewInState_started() {
        activity.presenter.showUnstartedGameView();

        onView(withId(R.id.activity_game_container_not_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_start)).check(isVisible());

        onView(withId(R.id.activity_game_container_started)).check(isGone());
    }

    @Test
    @Ignore
    public void showPlayingGameView_verifyViewInState_started() {
        showPlayingGameView_resetGame();

        onView(withId(R.id.activity_game_container_not_started)).check(isGone());

        onView(withId(R.id.activity_game_container_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_time_increase)).check(isVisible());
        onView(withId(R.id.activity_game_text_time)).check(isVisible());
        onView(withId(R.id.activity_game_button_time_decrease)).check(isVisible());
        onView(withId(R.id.activity_game_button_play_or_pause)).check(isVisible());
        onView(withId(R.id.activity_game_button_end)).check(isVisible());

        onView(withText("time string")).check(isVisible());
    }

    @Test
    @Ignore
    public void showPausedGameView_verifyViewInState_started() {
        activity.presenter.showPausedGameView("00:00:00");

        onView(withId(R.id.activity_game_container_not_started)).check(isGone());

        onView(withId(R.id.activity_game_container_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_time_increase)).check(isVisible());
        onView(withId(R.id.activity_game_text_time)).check(isVisible());
        onView(withId(R.id.activity_game_button_time_decrease)).check(isVisible());
        onView(withId(R.id.activity_game_button_play_or_pause)).check(isVisible());
        onView(withId(R.id.activity_game_button_end)).check(isVisible());

        onView(withText("00:00:00")).check(isVisible());
    }

    @Test
    @Ignore
    public void showFinishedGameView_verifyViewInState_started() {
        activity.presenter.showFinishedGameView();

        onView(withId(R.id.activity_game_container_not_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_start)).check(isVisible());

        onView(withId(R.id.activity_game_container_started)).check(isGone());
    }

    public void showPlayingGameView_resetGame() {
        activity.presenter.showPlayingGameView("time string", Collections.emptyList());
        reset(game);
    }
}
