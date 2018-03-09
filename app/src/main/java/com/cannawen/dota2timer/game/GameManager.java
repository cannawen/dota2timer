package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.game.model.interfaces.Game;

import lombok.Getter;
import lombok.Setter;

public class GameManager {

    private static GameManager singleton = new GameManager();

    @Setter
    @Getter
    Game game;

    public static GameManager getInstance() {
        return singleton;
    }
}
