package com.example.scrabble;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton playButton = (ImageButton) findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь код для запуска игры, например:
                startGame();
            }
        });

        ImageButton statsButton = (ImageButton) findViewById(R.id.button_stats);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь код для отображения статистики, например:
                showStats();
            }
        });

        ImageButton exitButton = (ImageButton) findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь код для выхода из приложения:
                finish();
            }
        });
    }

    private void startGame() {
        // Здесь код для запуска игры
    }

    private void showStats() {
        // Здесь код для отображения статистики
    }
}