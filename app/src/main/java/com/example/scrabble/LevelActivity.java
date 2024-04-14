package com.example.scrabble;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        ImageButton easyButton = findViewById(R.id.easy);
        easyButton.setOnClickListener(v -> startGame(300));

        ImageButton mediumButton = findViewById(R.id.medium);
        mediumButton.setOnClickListener(v -> startGame(180));

        ImageButton hardButton = findViewById(R.id.hard);
        hardButton.setOnClickListener(v -> startGame(90));
    }

    private void startGame(int seconds) {
        Intent intent = new Intent(LevelActivity.this, GameActivity.class);
        intent.putExtra("TIME_LIMIT", seconds);
        startActivity(intent);
    }
}
