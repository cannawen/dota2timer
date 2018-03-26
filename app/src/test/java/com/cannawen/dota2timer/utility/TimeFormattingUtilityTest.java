package com.cannawen.dota2timer.utility;


import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

@SmallTest
public class TimeFormattingUtilityTest {

    TimeFormattingUtility utility;

    @Before
    public void setUp() throws Exception {
        utility = new TimeFormattingUtility();
    }

    @Test
    public void timeFormatter_shouldTurnSecondsIntoHHMMSSFormat() {
        assertEquals("00:00:00", utility.timeString(0));
    }

    @Test
    public void timeFormatter_shouldHandleNegativeTime() {
        assertEquals("-00:01:01", utility.timeString(-61));
    }

    @Test
    public void timeFormatter_shouldHandlePositiveTime() {
        assertEquals("01:01:01", utility.timeString(3661));
    }
}
