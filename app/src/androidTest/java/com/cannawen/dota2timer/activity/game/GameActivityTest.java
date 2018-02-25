package com.cannawen.dota2timer.activity.game;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cannawen.dota2timer.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.cannawen.dota2timer.activity.ActivityTestHelper.isGone;
import static com.cannawen.dota2timer.activity.ActivityTestHelper.isVisible;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> rule = new ActivityTestRule<>(GameActivity.class);

    GameActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @Test
    public void onCreate_verifyViewInState_notStarted() throws Exception {
        onView(withId(R.id.activity_game_container_not_started)).check(isVisible());
        onView(withId(R.id.activity_game_button_start)).check(isVisible());

        onView(withId(R.id.activity_game_container_started)).check(isGone());
    }

    @Test
    public void onPressStart_verifyViewInState_started() throws Exception {
        onView(withId(R.id.activity_game_button_start)).perform(click());

        onView(withId(R.id.activity_game_container_not_started)).check(isGone());

        onView(withId(R.id.activity_game_button_time_increase)).check(isVisible());
        onView(withId(R.id.activity_game_text_time)).check(isVisible());
        onView(withId(R.id.activity_game_button_time_decrease)).check(isVisible());
        onView(withId(R.id.activity_game_button_play_or_pause)).check(isVisible());
        onView(withId(R.id.activity_game_button_end)).check(isVisible());
    }
}
