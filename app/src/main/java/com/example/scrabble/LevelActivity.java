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
        if(seconds == 300) {
            User.difficulty = "easy";
        }
        if(seconds == 180) {
            User.difficulty = "medium";
        }
        if(seconds == 90) {
            User.difficulty = "hard";
        }
        Intent intent = new Intent(LevelActivity.this, GameActivity.class);
        intent.putExtra("TIME_LIMIT", seconds);
        startActivity(intent);
    }
}
