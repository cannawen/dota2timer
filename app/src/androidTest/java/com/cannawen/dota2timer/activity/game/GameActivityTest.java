package com.cannawen.dota2timer.activity.game;

import android.support.annotation.IdRes;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.cannawen.dota2timer.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
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
        assertTrue(isVisible(R.id.activity_game_container_not_started));
        assertTrue(isVisible(R.id.activity_game_button_start));

        assertFalse(isVisible(R.id.activity_game_container_started));
    }

    @Test
    @UiThreadTest
    public void onPressStart_verifyViewInState_started() throws Exception {
        activity.findViewById(R.id.activity_game_button_start).callOnClick();

        assertFalse(isVisible(R.id.activity_game_container_not_started));

        assertTrue(isVisible(R.id.activity_game_button_time_increase));
        assertTrue(isVisible(R.id.activity_game_text_time));
        assertTrue(isVisible(R.id.activity_game_button_time_decrease));
        assertTrue(isVisible(R.id.activity_game_button_play_or_pause));
        assertTrue(isVisible(R.id.activity_game_button_end));
    }

    private Boolean isVisible(@IdRes int id) {
        return activity.findViewById(id).getVisibility() == View.VISIBLE;
    }
}
