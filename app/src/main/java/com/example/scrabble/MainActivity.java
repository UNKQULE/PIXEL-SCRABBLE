package com.example.scrabble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.content.SharedPreferences;
import android.widget.EditText;

public class MainActivity extends Activity {

    private static final String PREFS_NAME = "ScrabblePrefs";
    private static final String NICKNAME_KEY = "nickname";
    private EditText editTextNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ImageButton playButton = findViewById(R.id.button_play);
        ImageButton statsButton = findViewById(R.id.button_stats);
        ImageButton exitButton = findViewById(R.id.button_exit);
        editTextNickname = findViewById(R.id.edittext_nickname);
        playButton.setOnClickListener(v -> startGame());
        statsButton.setOnClickListener(v -> showStats());
        exitButton.setOnClickListener(v -> finish());
        String nickname = prefs.getString(NICKNAME_KEY, "");
        editTextNickname.setText(nickname);
        editTextNickname.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveNickname(editTextNickname.getText().toString());
            }
        });
    }

    private void startGame() {
        Intent intent1 = new Intent(MainActivity.this, LevelActivity.class);
        startActivity(intent1);
    }

    private void showStats() {
        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
        startActivity(intent);
    }

    private void saveNickname(String nickname) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NICKNAME_KEY, nickname);
        editor.apply();
    }
}