package com.cannawen.dota2timer.configuration;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(false, event.triggeredAt(9));
        assertEquals(true, event.triggeredAt(10));
        assertEquals(true, event.triggeredAt(20));
        assertEquals(false, event.triggeredAt(21));
    }

    @Test
    public void event_triggeredAt_startNonZero() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(100)
                .time_expire(Event.NO_EXPIRY)
                .enabled(true)
                .build();

        assertEquals(false, event.triggeredAt(10));
        assertEquals(true, event.triggeredAt(100));
        assertEquals(true, event.triggeredAt(130));
        assertEquals(false, event.triggeredAt(142));
    }

    @Test
    public void event_triggeredAt_advanceNotice() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_advance_notice(1)
                .time_expire(Event.NO_EXPIRY)
                .enabled(true)
                .build();

        assertEquals(true, event.triggeredAt(9));
        assertEquals(false, event.triggeredAt(10));
        assertEquals(true, event.triggeredAt(99));
    }

    @Test
    public void event_triggeredAt_expiry() {
        Event event = Event.builder()
                .time_repeat(10)
                .time_expire(100)
                .enabled(true)
                .build();
        assertEquals(false, event.triggeredAt(9));
        assertEquals(true, event.triggeredAt(10));
        assertEquals(true, event.triggeredAt(60));
        assertEquals(false, event.triggeredAt(100));
        assertEquals(false, event.triggeredAt(110));
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
        assertEquals(true, event.triggeredAt(574));
        assertEquals(false, event.triggeredAt(604));
    }
}
