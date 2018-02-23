package com.cannawen.dota2timer.model;

import com.cannawen.dota2timer.model.Setting;

import org.junit.Test;

import static org.junit.Assert.*;

public class SettingTest {
    @Test
    public void setting_triggeredAt_basic() throws Exception {
        Setting setting = Setting.builder()
                .time_repeat(10)
                .time_initial(0)
                .time_advance_notice(0)
                .time_expire(Setting.NO_EXPIRY)
                .build();
        assertEquals(setting.triggeredAt(9), false);
        assertEquals(setting.triggeredAt(10), true);
        assertEquals(setting.triggeredAt(20), true);
        assertEquals(setting.triggeredAt(21), false);
    }

    @Test
    public void setting_triggeredAt_startNonZero() throws Exception {
        Setting setting = Setting.builder()
                .time_repeat(10)
                .time_initial(100)
                .build();

        assertEquals(setting.triggeredAt(10), false);
        assertEquals(setting.triggeredAt(100), true);
        assertEquals(setting.triggeredAt(130), true);
        assertEquals(setting.triggeredAt(142), false);
    }

    @Test
    public void setting_triggeredAt_advanceNotice() throws Exception {
        Setting setting = Setting.builder()
                .time_repeat(10)
                .time_advance_notice(1)
                .build();

        assertEquals(setting.triggeredAt(9), true);
        assertEquals(setting.triggeredAt(10), false);
        assertEquals(setting.triggeredAt(99), true);
    }

    @Test
    public void setting_triggeredAt_expiry() throws Exception {
        Setting setting = Setting.builder()
                .time_repeat(10)
                .time_expire(100)
                .build();
        assertEquals(setting.triggeredAt(9), false);
        assertEquals(setting.triggeredAt(10), true);
        assertEquals(setting.triggeredAt(60), true);
        assertEquals(setting.triggeredAt(100), false);
        assertEquals(setting.triggeredAt(110), false);
    }

    @Test
    public void setting_triggeredAt_all_options() throws Exception {
        Setting setting = Setting.builder()
                .time_repeat(10)
                .time_initial(100)
                .time_advance_notice(1)
                .time_expire(200)
                .build();

        assertEquals(setting.triggeredAt(89), false);
        assertEquals(setting.triggeredAt(99), true);
        assertEquals(setting.triggeredAt(199), true);
        assertEquals(setting.triggeredAt(209), false);
    }
}