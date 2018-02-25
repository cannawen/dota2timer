package com.cannawen.dota2timer.activity.configuration;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.configuration.Configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.cannawen.dota2timer.activity.ActivityTestHelper.isVisible;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ConfigurationActivityTest {

    @Rule
    public ActivityTestRule<ConfigurationActivity> rule = new ActivityTestRule<ConfigurationActivity>(ConfigurationActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Configuration configuration = new Configuration();
            configuration.setEvents(Collections.emptyList());

            InstrumentationRegistry.getTargetContext();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra(ConfigurationActivity.INTENT_CONFIGURATION_KEY, configuration);
            return intent;
        }
    };

    ConfigurationActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = rule.getActivity();
    }

    @Test
    public void onCreate_shouldShowAllUIElements() throws Exception {
        onView(withId(R.id.activity_configuration_description_event)).check(isVisible());
        onView(withId(R.id.activity_configuration_description_enabled)).check(isVisible());
        onView(withId(R.id.activity_configuration_recycler_view)).check(isVisible());
        onView(withId(R.id.activity_configuration_cancel)).check(isVisible());
        onView(withId(R.id.activity_configuration_save)).check(isVisible());
    }
}
