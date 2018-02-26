package com.cannawen.dota2timer.configuration;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@SmallTest
public class EventTest {
    @Test
    public void event_triggeredAt_basicTest() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(0)
                .time_advance_notice(0)
                .time_expire(Event.NO_EXPIRY)
                .enabled(true)
                .build();
        assertFalse(event.triggeredAt(9));
        assertTrue(event.triggeredAt(10));
        assertTrue(event.triggeredAt(20));
        assertFalse(event.triggeredAt(21));
    }

    @Test
    public void event_triggeredAt_startNonZero() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(100)
                .time_expire(Event.NO_EXPIRY)
                .enabled(true)
                .build();

        assertFalse(event.triggeredAt(10));
        assertTrue(event.triggeredAt(100));
        assertTrue(event.triggeredAt(130));
        assertFalse(event.triggeredAt(142));
    }

    @Test
    public void event_triggeredAt_advanceNotice() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_advance_notice(1)
                .time_expire(Event.NO_EXPIRY)
                .enabled(true)
                .build();

        assertTrue(event.triggeredAt(9));
        assertFalse(event.triggeredAt(10));
        assertTrue(event.triggeredAt(99));
    }

    @Test
    public void event_triggeredAt_expiry() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_expire(100)
                .enabled(true)
                .build();
        assertFalse(event.triggeredAt(9));
        assertTrue(event.triggeredAt(10));
        assertTrue(event.triggeredAt(60));
        assertFalse(event.triggeredAt(100));
        assertFalse(event.triggeredAt(110));
    }

    @Test
    public void event_triggeredAt_allOptions() {
        Event event = Event.builder()
                .time_initial(71)
                .time_repeat(30)
                .time_expire(600)
                .time_advance_notice(7)
                .enabled(true)
                .build();
        assertTrue(event.triggeredAt(574));
        assertFalse(event.triggeredAt(604));
    }
}
