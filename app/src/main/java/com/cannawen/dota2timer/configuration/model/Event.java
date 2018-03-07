package com.cannawen.dota2timer.configuration.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event implements Serializable {
    static int NO_EXPIRY = 0; // TODO test snakeYAML serialization with event object default values
    static int NO_REPEAT = 0; // TODO test snakeYAML serialization with event object default values

    String name;
    int time_initial;
    int time_repeat;
    int time_advance_notice;
    int time_expire;
    boolean enabled;

    public static Event defaultEvent() {
        return Event.builder()
                .name("Event")
                .time_initial(0)
                .time_repeat(NO_REPEAT)
                .time_advance_notice(0)
                .time_expire(NO_EXPIRY)
                .enabled(true)
                .build();
    }

    public boolean triggeredAt(int secondsElapsed) {
        if (time_repeat == NO_REPEAT) {
            return secondsElapsed == time_initial - time_advance_notice;
        }

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
