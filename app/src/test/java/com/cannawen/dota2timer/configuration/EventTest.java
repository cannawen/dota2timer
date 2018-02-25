package com.cannawen.dota2timer.configuration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {
    @Test
    public void setting_triggeredAt_basic() throws Exception {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(0)
                .time_advance_notice(0)
                .time_expire(Event.NO_EXPIRY)
                .build();
        assertEquals(event.triggeredAt(9), false);
        assertEquals(event.triggeredAt(10), true);
        assertEquals(event.triggeredAt(20), true);
        assertEquals(event.triggeredAt(21), false);
    }

    @Test
    public void setting_triggeredAt_startNonZero() throws Exception {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(100)
                .build();

        assertEquals(event.triggeredAt(10), false);
        assertEquals(event.triggeredAt(100), true);
        assertEquals(event.triggeredAt(130), true);
        assertEquals(event.triggeredAt(142), false);
    }

    @Test
    public void setting_triggeredAt_advanceNotice() throws Exception {
        Event event = Event.builder()
                .time_repeat(10)
                .time_advance_notice(1)
                .build();

        assertEquals(event.triggeredAt(9), true);
        assertEquals(event.triggeredAt(10), false);
        assertEquals(event.triggeredAt(99), true);
    }

    @Test
    public void setting_triggeredAt_expiry() throws Exception {
        Event event = Event.builder()
                .time_repeat(10)
                .time_expire(100)
                .build();
        assertEquals(event.triggeredAt(9), false);
        assertEquals(event.triggeredAt(10), true);
        assertEquals(event.triggeredAt(60), true);
        assertEquals(event.triggeredAt(100), false);
        assertEquals(event.triggeredAt(110), false);
    }

    @Test
    public void setting_triggeredAt_all_options() throws Exception {
        Event event = Event.builder()
                .time_repeat(10)
                .time_initial(100)
                .time_advance_notice(1)
                .time_expire(200)
                .build();

        assertEquals(event.triggeredAt(89), false);
        assertEquals(event.triggeredAt(99), true);
        assertEquals(event.triggeredAt(199), true);
        assertEquals(event.triggeredAt(209), false);
    }

    @Test
    public void setting_pull_expiry() {
        Event event = Event.builder()
                .time_initial(71)
                .time_repeat(30)
                .time_expire(600)
                .time_advance_notice(7)
                .build();
        assertEquals(event.triggeredAt(574), true);
        assertEquals(event.triggeredAt(604), false);
    }
}
