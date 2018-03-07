package com.cannawen.dota2timer.configuration.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Configuration implements Serializable {
    List<Event> events;

    public void createNewEvent() {
        events.add(Event.defaultEvent());
    }
}
