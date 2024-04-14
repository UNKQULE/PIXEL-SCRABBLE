package com.example.scrabble;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
        playAgainButton.setOnClickListener(v -> {

            Intent intent = new Intent(ScoreActivity.this, LevelActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        scoreTextView.setText(String.format("Score: %d", finalScore));


    }
}