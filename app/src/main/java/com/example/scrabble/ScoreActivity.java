package com.example.scrabble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.score_text_view);
        ImageButton playAgainButton = findViewById(R.id.play_again_button);

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        scoreTextView.setText(String.format("Score: %d", finalScore));

        playAgainButton.setOnClickListener(v -> {
            // Здесь реализуйте логику для начала новой игры
            // Например, перейти обратно к GameActivity
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        });
    }
}