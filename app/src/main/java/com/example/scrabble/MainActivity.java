package com.example.scrabble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.ImageButton;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;

public class MainActivity extends Activity {

    private static final String PREFS_NAME = "ScrabblePrefs";
    private static final String NICKNAME_KEY = "nickname";
    private EditText editTextNickname;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ImageButton playButton = findViewById(R.id.button_play);
        ImageButton statsButton = findViewById(R.id.button_stats);
        ImageButton exitButton = findViewById(R.id.button_exit);
        ImageButton rulesButton = findViewById(R.id.button_rules);
        editTextNickname = findViewById(R.id.edittext_nickname);

        playButton.setOnClickListener(v -> startGame());
        statsButton.setOnClickListener(v -> showStats());
        exitButton.setOnClickListener(v -> finish());
        rulesButton.setOnClickListener(v -> showRules());

        loadNickname();

        editTextNickname.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveNickname();
                closeKeyboardAndClearFocus(editTextNickname);
                return true;
            }
            return false;
        });
    }

    private void startGame() {
        editTextNickname = findViewById(R.id.edittext_nickname);
        User.username = String.valueOf(editTextNickname.getText());
        Log.i("MyAppTag", User.username);
        Intent intent = new Intent(MainActivity.this, LevelActivity.class);
        startActivity(intent);

    }

    private void showStats() {
        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
        startActivity(intent);
    }

    private void showRules() {
        Intent intent = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(intent);
    }

    private void saveNickname() {
        String nickname = editTextNickname.getText().toString().trim();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NICKNAME_KEY, nickname);
        editor.apply();
    }

    private void loadNickname() {
        String nickname = prefs.getString(NICKNAME_KEY, "");
        editTextNickname.setText(nickname);
    }

    private void closeKeyboardAndClearFocus(EditText editText) {
        // Скрытие клавиатуры и удаление фокуса
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }
}