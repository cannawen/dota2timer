package com.cannawen.dota2timer.configuration;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event implements Serializable {
    static int NO_EXPIRY = 0;

    @Builder.Default
    String name = "";
    @Builder.Default
    int time_initial = 0;
    @Builder.Default
    int time_repeat = 0;
    @Builder.Default
    int time_advance_notice = 0;
    @Builder.Default
    int time_expire = NO_EXPIRY;

    public boolean triggeredAt(int secondsElapsed) {
        boolean withinAcceptableStartRange = secondsElapsed >= time_initial - time_advance_notice;
        boolean withinAcceptableEndRange = time_expire == NO_EXPIRY || secondsElapsed - time_advance_notice < time_expire;
        // A notice should be triggered when time is within acceptable range, and:
        // secondsElapsed == time_initial + time_repeat * N - time_advance_notice
        // where N is an integer number
        return withinAcceptableStartRange
                && withinAcceptableEndRange
                && (secondsElapsed + time_advance_notice - time_initial) % time_repeat == 0;
    }
}
