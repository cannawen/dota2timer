package com.cannawen.dota2timer.game.activity;

import android.app.Activity;
import android.content.Intent;

import com.annimon.stream.Stream;
import com.cannawen.dota2timer.game.GameManager;
import com.cannawen.dota2timer.game.model.interfaces.Game;


public class VoiceTimerActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        Game game = GameManager.getInstance().getGame();
        Stream.of(game.voiceCommandKeywords())
                .filter((keyword) -> text.toLowerCase().contains(keyword))
                .forEach((keyword) -> game.note(keyword));

        finish();
    }
}
