package com.cannawen.dota2timer.utility;

public class TimeFormattingUtility {
    public String timeString(int secondsElapsed) {
        String signString = "";
        if (secondsElapsed < 0) {
            signString = "-";
            secondsElapsed = secondsElapsed * -1;
        }

        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        return String.format("%s%02d:%02d:%02d", signString, hours, minutes, seconds);
    }
}
