package com.example.scrabble;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Tile extends RelativeLayout {

    private final TextView letter;
    private final TextView score;

    public boolean w3Moded = false;
    public boolean w3ModedTwice = false;

    public boolean wasW3ModedTwice = false;

    public Tile(Context context) {
        super(context);
        ImageView tile = new ImageView(context);
        tile.setBackgroundResource(R.drawable.tile_image);
        RelativeLayout.LayoutParams tileParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        addView(tile, tileParams);

        // Создаем TextView для текста посередине
        letter = new TextView(context);
        setTextSizeInDp(letter, 25);
        letter.setTextColor(Color.BLACK);
        letter.setText("");
        letter.setGravity(Gravity.CENTER); // Выравнивание по центру
        RelativeLayout.LayoutParams centerTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        centerTextParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        addView(letter, centerTextParams); // Добавляем TextView в layout

        // Создаем TextView для текста в правом верхнем углу
        score = new TextView(context);
        setTextSizeInDp(score, 12);
        score.setTextColor(Color.BLACK);
        score.setText("");
        RelativeLayout.LayoutParams topRightParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        topRightParams.rightMargin = pxToDp(context, 10);
        topRightParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        topRightParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);

        addView(score, topRightParams); // Добавляем TextView в layout
    }


    public void setTextSizeInDp(TextView textView, float sizeInDp) {
        float scale = textView.getContext().getResources().getDisplayMetrics().density;
        float sizeInPx = sizeInDp * scale;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInPx);
    }

    public static int pxToDp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    // Методы для настройки или получения данных c TextView могут быть добавлены здесь
    public char getLetter() {
        return letter.getText().charAt(0);
    }

    public void setLetter(String c) {
        letter.setText(c);
    }

    public String getScore() {
        return score.getText().toString();
    }

    public void setScore(String num) {
        score.setText(num);
    }

    public void setDefault() {
        letter.setTextColor(Color.BLACK);
        score.setTextColor(Color.BLACK);
    }

    public void setL2() {
        letter.setTextColor(Color.YELLOW);
        score.setTextColor(Color.YELLOW);
    }

    public void setL3() {
        letter.setTextColor(Color.GREEN);
        score.setTextColor(Color.GREEN);
    }

    public void setW3() {
        letter.setTextColor(Color.RED);
        score.setTextColor(Color.RED);
        w3Moded = true;
    }

    public boolean isModed() {
        return w3Moded;
    }

    public void setPrevious() {
        if(w3ModedTwice) {
            w3ModedTwice = false;
            return;
        }
        if(w3Moded) {
            setDefault();
            w3Moded = false;
        }
    }
}
