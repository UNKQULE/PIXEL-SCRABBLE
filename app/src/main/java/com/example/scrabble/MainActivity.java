package com.example.scrabble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(v -> startGame());

        Button statsButton = findViewById(R.id.button_stats);
        statsButton.setOnClickListener(v -> showStats());

        Button exitButton = findViewById(R.id.button_exit);
        exitButton.setOnClickListener(v -> finish());
    }

    private void startGame() {
        Intent intent1 = new Intent(MainActivity.this, LevelActivity.class);
        startActivity(intent1);
    }

    private void showStats() {
        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
        startActivity(intent);
    }
}