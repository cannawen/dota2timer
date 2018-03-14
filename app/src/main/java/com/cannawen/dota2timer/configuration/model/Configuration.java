package com.cannawen.dota2timer.configuration.model;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Configuration implements Serializable {
    List<Event> events;

    public void createNewEvent() {
        createNewEvent(Event.defaultEvent());
    }

    public void createNewEvent(Event event) {
        events.add(event);
    }

    public void removeTransitoryEvents() {
        events = Stream.of(events).filterNot(Event::isTransitory).collect(Collectors.toList());
    }
}
