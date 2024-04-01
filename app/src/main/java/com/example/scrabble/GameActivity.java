package com.example.scrabble;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Random;

public class GameActivity extends Activity {

    private final char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final Random random = new Random();
    private RelativeLayout gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameBoard = findViewById(R.id.gameboard_image);

        // Создание начального набора кубиков
        for (int i = 0; i < 7; i++) {
            generateNewDice();
        }

        // Кнопка возврата в меню
        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());
    }

    // Метод для генерации нового кубика
    private void generateNewDice() {
        ImageView newDiceImageView = new ImageView(this);
        TextView newLetterTextView = new TextView(this);

        // Установка случайной буквы
        setRandomLetter(newLetterTextView);

        // Установка параметров макета и добавление кубика и буквы на игровое поле
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        newDiceImageView.setLayoutParams(layoutParams);
        newLetterTextView.setLayoutParams(layoutParams);

        newDiceImageView.setImageResource(R.drawable.dice_image);
        gameBoard.addView(newDiceImageView);
        gameBoard.addView(newLetterTextView);

        // Установка обработчиков событий для перетаскивания
        setupDragAndDrop(newDiceImageView, newLetterTextView);
    }

    private void setRandomLetter(TextView letterTextView) {
        int index = random.nextInt(letters.length);
        char randomLetter = letters[index];
        letterTextView.setText(String.valueOf(randomLetter));
        letterTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }

    private void setupDragAndDrop(@NonNull ImageView diceImageView, TextView letterTextView) {
        diceImageView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(diceImageView);
                view.startDragAndDrop(clipData, dragShadowBuilder, view, 0);
                letterTextView.setVisibility(View.INVISIBLE); // Скрываем букву во время перетаскивания
                return true;
            }
            return false;
        });

        diceImageView.setOnDragListener((view, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Игнорировать это событие
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // Игнорировать это событие
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    // Игнорировать это событие
                    return true;
                case DragEvent.ACTION_DROP:
                    // Обрабатываем событие броска
                    handleDropEvent(event, diceImageView, letterTextView);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Возвращаем видимость, если перетаскивание завершилось без броска
                    if (!event.getResult()) {
                        letterTextView.setVisibility(View.VISIBLE);
                    }
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    private void handleDropEvent(DragEvent event, ImageView diceImageView, TextView letterTextView) {
        View draggedView = (View) event.getLocalState();
        RelativeLayout container = (RelativeLayout) draggedView.getParent();
        container.removeView(draggedView);
        container.addView(draggedView);
        draggedView.setX(event.getX() - draggedView.getWidth() / 2);
        draggedView.setY(event.getY() - draggedView.getHeight() / 2);
        draggedView.setVisibility(View.VISIBLE);
        letterTextView.setVisibility(View.VISIBLE);

        // Генерация нового кубика после успешного броска
        generateNewDice();
    }
}