package com.cannawen.dota2timer.activity.game;

import android.support.annotation.IdRes;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.activity.ActivityTestHelper;

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
    ActivityTestHelper helper;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
        helper = new ActivityTestHelper(activity);
    }

    @Test
    public void onCreate_verifyViewInState_notStarted() throws Exception {
        assertTrue(helper.isVisible(R.id.activity_game_container_not_started));
        assertTrue(helper.isVisible(R.id.activity_game_button_start));

        assertFalse(helper.isVisible(R.id.activity_game_container_started));
    }

    @Test
    @UiThreadTest
    public void onPressStart_verifyViewInState_started() throws Exception {
        helper.click(R.id.activity_game_button_start);

        assertFalse(helper.isVisible(R.id.activity_game_container_not_started));

        assertTrue(helper.isVisible(R.id.activity_game_button_time_increase));
        assertTrue(helper.isVisible(R.id.activity_game_text_time));
        assertTrue(helper.isVisible(R.id.activity_game_button_time_decrease));
        assertTrue(helper.isVisible(R.id.activity_game_button_play_or_pause));
        assertTrue(helper.isVisible(R.id.activity_game_button_end));
    }
}
