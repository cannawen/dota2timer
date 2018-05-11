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
        assertEquals("0", utility.parseTimeSecondsToString(0));
    }

    @Test
    public void timeFormatter_shouldHandleNegativeTime() {
        assertEquals("-1:01", utility.parseTimeSecondsToString(-61));
    }

    @Test
    public void timeFormatter_shouldHandlePositiveTime() {
        assertEquals("1:01:01", utility.parseTimeSecondsToString(3661));
    }
}
