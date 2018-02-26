package com.cannawen.dota2timer.configuration;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@Builder
public class Event implements Serializable {
    static int NO_EXPIRY = 0; // TODO test snakeYAML serialization with event object default values

    String name;
    int time_initial;
    int time_repeat;
    int time_advance_notice;
    int time_expire;
    boolean enabled;

    public boolean triggeredAt(int secondsElapsed) {
        boolean withinAcceptableStartRange = secondsElapsed >= time_initial - time_advance_notice;
        boolean withinAcceptableEndRange = time_expire == NO_EXPIRY || secondsElapsed < time_expire;
        // A notice should be triggered when time is within acceptable range, and:
        // secondsElapsed == time_initial + time_repeat * N - time_advance_notice
        // where N is an integer number
        boolean isCorrectMultipleOfPeriod = (secondsElapsed + time_advance_notice - time_initial) % time_repeat == 0;
        return enabled
                && withinAcceptableStartRange
                && withinAcceptableEndRange
                && isCorrectMultipleOfPeriod;
    }

}
