package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.model.Configuration;
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

    public void resetGame(Configuration configuration) {
        if (game != null) {
            game.end();
            game.reset();
        }

        game.setConfiguration(configuration);
    }
}
