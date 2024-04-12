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

        ImageButton easy = findViewById(R.id.easy);
        easy.setOnClickListener(v -> startGame());

        ImageButton medium = findViewById(R.id.medium);
        medium.setOnClickListener(v -> startGame());

        ImageButton hard = findViewById(R.id.hard);
        hard.setOnClickListener(v -> startGame());
    }
    private void startGame() {
        Intent intent = new Intent(LevelActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
