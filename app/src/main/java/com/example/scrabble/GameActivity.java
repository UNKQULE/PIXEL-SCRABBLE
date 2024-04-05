package com.example.scrabble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.Arrays;
import java.util.List;


public class GameActivity extends AppCompatActivity {

    private final char[][] desk = new char[9][9];

    private final List<Pair<Integer, Integer>> w3 = Arrays.asList(
            new Pair<>(0, 0),
            new Pair<>(0, 4),
            new Pair<>(0, 8),
            new Pair<>(4, 0),
            new Pair<>(4, 8),
            new Pair<>(8, 0),
            new Pair<>(8, 4),
            new Pair<>(8, 8)
    );

    private final List<Pair<Integer, Integer>> l3 = Arrays.asList(
            new Pair<>(1, 1),
            new Pair<>(1, 7),
            new Pair<>(2, 2),
            new Pair<>(2, 6),
            new Pair<>(6, 2),
            new Pair<>(6, 6),
            new Pair<>(7, 1),
            new Pair<>(7, 7)
    );

    private final List<Pair<Integer, Integer>> l2 = Arrays.asList(
            new Pair<>(3, 3),
            new Pair<>(3, 5),
            new Pair<>(5, 3),
            new Pair<>(5, 5)
    );

    int[] buttonIds = new int[7];

    private char value = '0';
    int count = 0;
    boolean firstWordFlag = true;
    int prevBtn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        //отрисовываем карту

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button button = getButton(row, col);

                // Параметры разметки для кнопки
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0); // Можно настроить отступы

                // Добавление кнопки в GridLayout
                gridLayout.addView(button, params);
            }
        }

        ConstraintLayout constraintLayout = findViewById(R.id.botConstraint);

        Button[] buttons = new Button[7];

        for (int i = 0; i < buttons.length; i++) {
            // Создаем кнопку
            buttons[i] = new Button(new ContextThemeWrapper(this, R.style.UserCard), null, 0);
            buttons[i].setId(View.generateViewId());
            int finalI = i;
            char text = Game.getRandomChar();
            buttons[finalI].setText(String.valueOf(text));
            buttons[i].setOnClickListener(v -> {

                if(prevBtn != 0 & prevBtn != buttons[finalI].getId()) {
                    Button prevButton = findViewById(prevBtn);
                    ViewGroup.LayoutParams prevLayoutParams = prevButton.getLayoutParams();
                    prevLayoutParams.width = (int) (prevButton.getWidth() / 1.2);
                    prevLayoutParams.height = (int) (prevButton.getHeight() / 1.2);
                    prevButton.setLayoutParams(prevLayoutParams);
                }
                value = buttons[finalI].getText().charAt(0);
                if(prevBtn != buttons[finalI].getId()) {
                    // Вычисление новых размеров
                    int newWidth = (int) (buttons[finalI].getWidth() * 1.2);
                    int newHeight = (int) (buttons[finalI].getHeight() * 1.2);

                    // Получение параметров макета и установка новых размеров
                    ViewGroup.LayoutParams layoutParams = buttons[finalI].getLayoutParams();
                    layoutParams.width = newWidth;
                    layoutParams.height = newHeight;

                    // Применение новых параметров к кнопке
                    buttons[finalI].setLayoutParams(layoutParams);
                    prevBtn = buttons[finalI].getId();
                }


            });

            // Устанавливаем параметры разметки
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            layoutParams.matchConstraintPercentWidth = 0.10f;
            layoutParams.matchConstraintPercentHeight = 0.30f;

            // Добавляем кнопку в ConstraintLayout
            constraintLayout.addView(buttons[i], layoutParams);
        }


        // Создаем горизонтальный chain
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // Вспомогательный массив для ID кнопок
        for (int i = 0; i < buttons.length; i++) {
            buttonIds[i] = buttons[i].getId();
            // Установка обработчиков событий для перетаскивания
        }


        // Создаем горизонтальный chain
        constraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                buttonIds, null, ConstraintSet.CHAIN_SPREAD);

        // Применяем ограничения к ConstraintLayout
        constraintSet.applyTo(constraintLayout);

        //кнопки ENTER и RETRUN
        ImageButton enterBtn = findViewById(R.id.enter_button_image);
        ImageButton returnBtn = findViewById(R.id.return_button_image);

        returnBtn.setOnClickListener(v -> {
            if(count != 0) {
                for(int i = 0; i < 7; ++i) {
                    Button handDice = findViewById(buttonIds[i]);
                    handDice.setVisibility(View.VISIBLE);
                }
                for(int i = 0; i < count; ++i) {
                    Button deskDice = findViewById(Game.returnDiceId());
                    Drawable background = Game.returnDiceBackground();
                    Pair<Integer, Integer> pos = Game.returnDicePosition();
                    Game.addChar(deskDice.getText().charAt(0));
                    deskDice.setText("");
                    deskDice.setBackground(background);
                    desk[pos.first][pos.second] = 0;
                }
                count = 0;

            }
        });

        enterBtn.setOnClickListener(v -> {
            if(!firstWordFlag) {
                Game.nextTurn();
                count = 0;
                //enterBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.grey));
                for(int i = 0; i < 7; ++i) {
                    Button handDice = findViewById(buttonIds[i]);
                    if (handDice.getVisibility() == View.GONE) {
                        char text = Game.getRandomChar();
                        handDice.setText(String.valueOf(text));
                        handDice.setVisibility(View.VISIBLE);
                    }
                }
            }

        });

    }

    @NonNull
    private Button getButton(int row, int col) {
        Pair<Integer, Integer> pos = new Pair<>(row, col);
        boolean flag = false;
        if(w3.contains(pos)) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.W3Place), null, 0);
            button.setId(View.generateViewId());
            setClickListener(button, row, col);
            return button;
        }
        if(l3.contains(pos)) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.L3Place), null, 0);
            button.setId(View.generateViewId());
            setClickListener(button, row, col);
            return button;
        }
        if(l2.contains(pos)) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.L2Place), null, 0);
            button.setId(View.generateViewId());
            setClickListener(button, row, col);
            return button;
        }
        if(pos.equals(new Pair<>(4, 4))) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.StartPlace), null, 0);
            button.setId(View.generateViewId());
            setClickListener(button, row, col);
            return button;
        }
        if(!flag) {
            Button button = new Button(new ContextThemeWrapper(this, R.style.DefaultPlace), null, 0);
            button.setId(View.generateViewId());
            setClickListener(button, row, col);
            return button;
        }
        return null;
    }

    private void setClickListener(Button button, int row, int col) {
        button.setOnClickListener(v -> {
            // Обработчик нажатий для кнопки
            if (value != '0') {
                if(count != 0) {
                    if(Game.hasNeighbours(desk, row, col) && button.getText() == "") {
                        Game.addDice(button, value, row, col);
                        desk[row][col] = value;
                        Button prevButton = findViewById(prevBtn);
                        prevButton.setVisibility(View.GONE);
                        if(firstWordFlag && row == 4 && col == 4) {
                            ImageButton enterBtn = findViewById(R.id.enter_button_image);
                            enterBtn.setBackgroundColor(Color.GREEN);
                            firstWordFlag = false;
                        }
                        value = '0';
                        count++;
                    }
                } else {
                    Game.addDice(button, value, row, col);
                    desk[row][col] = value;
                    value = '0';
                    Button prevButton = findViewById(prevBtn);
                    prevButton.setVisibility(View.GONE);
                    if(firstWordFlag && row == 4 && col == 4) {
                        ImageButton enterBtn = findViewById(R.id.enter_button_image);
                        enterBtn.setBackgroundColor(Color.GREEN);
                        firstWordFlag = false;
                    }
                    count++;
                }

            }
        });
    }
}