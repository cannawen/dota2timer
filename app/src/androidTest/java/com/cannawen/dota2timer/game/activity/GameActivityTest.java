package com.cannawen.dota2timer.game.activity;

import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.FlakyTest;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.creation.ConfigurationLoader;
import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.timer.AbstractTimer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.cannawen.dota2timer.ActivityTestHelper.isVisible;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FlakyTest
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> rule = new ActivityTestRule<>(GameActivity.class);

    ConfigurationLoader configurationLoader;
    AbstractTimer timer;

    GameActivity activity;

    @Before
    @UiThreadTest
    public void setUp() throws Exception {
        activity = rule.getActivity();

        configurationLoader = mock(ConfigurationLoader.class);
        timer = mock(AbstractTimer.class);

        activity.initWithDependencies(configurationLoader, timer);

        activity.onLoadConfigurationSuccess(mock(Configuration.class));
    }

    @Test
    public void onInitialize_showStartGame() {
        onView(withText(R.string.game_action_start)).check(isVisible());
    }

    @Test
    public void onClickStartGame_showEndGame_showPauseGame() {
        onView(withText(R.string.game_action_start)).perform(click());

        onView(withText(R.string.game_action_end)).check(matches(isDisplayed()));
        onView(withText(R.string.game_action_pause)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickPauseGame_showEndGame_showResumeGame() {
        onView(withText(R.string.game_action_start)).perform(click());
        onView(withText(R.string.game_action_pause)).perform(click());

        onView(withText(R.string.game_action_end)).check(matches(isDisplayed()));
        onView(withText(R.string.game_action_resume)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickResumeGame_showEndGame_showPauseGame() {
        onView(withText(R.string.game_action_start)).perform(click());
        onView(withText(R.string.game_action_pause)).perform(click());
        onView(withText(R.string.game_action_resume)).perform(click());

        onView(withText(R.string.game_action_end)).check(matches(isDisplayed()));
        onView(withText(R.string.game_action_pause)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickEndGame_showConfirmDialog() {
        onView(withText(R.string.game_action_start)).perform(click());
        onView(withText(R.string.game_action_end)).perform(click());

        onView(withText(R.string.game_action_end_confirmation_message)).check(matches(isDisplayed()));
        onView(withText(R.string.game_action_end_confirmation_button_positive)).check(matches(isDisplayed()));
        onView(withText(R.string.game_action_end_confirmation_button_negative)).check(matches(isDisplayed()));
    }

    @Test
    public void onClickEndGame_onConfirm_showStartGame() {
        onView(withText(R.string.game_action_start)).perform(click());
        onView(withText(R.string.game_action_end)).perform(click());
        onView(withText(R.string.game_action_end_confirmation_button_positive)).perform(click());

        onView(withText(R.string.game_action_start)).check(isVisible());
    }

    @Test
    public void onClickEndGame_onCancel_showEndGameStill() {
        onView(withText(R.string.game_action_start)).perform(click());
        onView(withText(R.string.game_action_end)).perform(click());
        onView(withText(R.string.game_action_end_confirmation_button_negative)).perform(click());

        onView(withText(R.string.game_action_end)).check(isVisible());
    }
}
