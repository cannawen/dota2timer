package com.cannawen.dota2timer.utility;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

public class TimeFormattingUtility {
    public static String parseTimeSecondsToString(int totalSeconds) {
        String signString = "";
        if (totalSeconds < 0) {
            signString = "-";
            totalSeconds = totalSeconds * -1;
        }

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%s%02d:%02d:%02d", signString, hours, minutes, seconds);
    }

    public static int parseTimeStringToSeconds(String string) {
        boolean negative = false;
        if (string.startsWith("-")) {
            negative = true;
            string = string.substring(1);
        }
        int totalSeconds = 0;
        List<Integer> timeChunks =
                Stream.of(string.split(":"))
                        .map(s -> Integer.valueOf(s))
                        .collect(Collectors.toList());
        for (int i = 0; i < timeChunks.size(); i++) {
            totalSeconds += timeChunks.get(timeChunks.size() - 1 - i) * Math.pow(60, i);
        }
        return negative ? totalSeconds * -1 : totalSeconds;
    }
}
