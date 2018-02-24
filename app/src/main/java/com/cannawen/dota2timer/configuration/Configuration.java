package com.cannawen.dota2timer.configuration;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Configuration implements Serializable {
    List<Event> events;
}
