package com.example.scrabble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(v -> startGame());

        ImageButton statsButton = findViewById(R.id.button_stats);
        statsButton.setOnClickListener(v -> showStats());

        ImageButton exitButton = findViewById(R.id.button_exit);
        exitButton.setOnClickListener(v -> finish());
    }

    private void startGame() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    private void showStats() {
        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
        startActivity(intent);
    }
}