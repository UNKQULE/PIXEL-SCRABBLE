package com.example.scrabble;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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

    private final char[][] gameBoard = new char[9][9];

    private static final List<Pair<Integer, Integer>> w3 = Arrays.asList(
            new Pair<>(0, 0),
            new Pair<>(0, 4),
            new Pair<>(0, 8),
            new Pair<>(4, 0),
            new Pair<>(4, 8),
            new Pair<>(8, 0),
            new Pair<>(8, 4),
            new Pair<>(8, 8)
    );

    private static final List<Pair<Integer, Integer>> l3 = Arrays.asList(
            new Pair<>(1, 1),
            new Pair<>(1, 7),
            new Pair<>(2, 2),
            new Pair<>(2, 6),
            new Pair<>(6, 2),
            new Pair<>(6, 6),
            new Pair<>(7, 1),
            new Pair<>(7, 7)
    );

    private static final List<Pair<Integer, Integer>> l2 = Arrays.asList(
            new Pair<>(3, 3),
            new Pair<>(3, 5),
            new Pair<>(5, 3),
            new Pair<>(5, 5)
    );

    private Button[] handTiles;
    private int prevSelectedHandTileId = -1;
    private char selectedChar = '0';
    private int placedTilesCount = 0;
    public static boolean isFirstWord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        ConstraintLayout constraintLayout = findViewById(R.id.botConstraint);

        initializeBoard(gridLayout);
        initializeHand(constraintLayout);
        initializeControlButtons();
    }

    private void initializeBoard(GridLayout gridLayout) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button boardCell = getBoardCell(row, col);
                boardCell.setId(View.generateViewId());
                int finalRow = row;
                int finalCol = col;
                boardCell.setOnClickListener(v -> boardCellClick(boardCell, finalRow, finalCol));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0);

                gridLayout.addView(boardCell, params);
            }
        }
    }

    private void initializeHand(ConstraintLayout constraintLayout) {
        handTiles = new Button[7];
        for (int i = 0; i < 7; i++) {
            Button handTile = new Button(new ContextThemeWrapper(this, R.style.HandTile), null, 0);
            handTile.setId(View.generateViewId());
            handTiles[i] = handTile;
            char letter = Game.getRandomChar();
            handTile.setText(String.valueOf(letter));
            handTile.setOnClickListener(v -> handTileClick(handTile));

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                    ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
            layoutParams.matchConstraintPercentWidth = 0.10f;
            layoutParams.matchConstraintPercentHeight = 0.30f;

            constraintLayout.addView(handTile, layoutParams);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        int[] buttonIds = new int[handTiles.length];
        for (int i = 0; i < handTiles.length; i++) {
            buttonIds[i] = handTiles[i].getId();
        }

        constraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                buttonIds, null, ConstraintSet.CHAIN_SPREAD);

        constraintSet.applyTo(constraintLayout);
    }

    private void initializeControlButtons() {
        ImageButton enterBtn = findViewById(R.id.enter_button_image);
        enterBtn.setVisibility(View.INVISIBLE);
        ImageButton returnBtn = findViewById(R.id.return_button_image);
        returnBtn.setVisibility(View.INVISIBLE);
        ImageButton swapBtn = findViewById(R.id.swap_button_image);

        returnBtn.setOnClickListener(v -> {
            if (placedTilesCount != 0) {
                for(int i = 0; i < placedTilesCount; ++i) {
                    Button returnTile = findViewById(Game.returnTileId());
                    Game.undoLastMove(returnTile, gameBoard);
                }
                for (Button handTile : handTiles) {
                    if (!handTile.isShown()) {
                        handTile.setVisibility(View.VISIBLE);
                    }
                }
                placedTilesCount = 0;
                enterBtn.setVisibility(View.INVISIBLE);
                swapBtn.setVisibility(View.VISIBLE);
                returnBtn.setVisibility(View.INVISIBLE);
            }
        });

        enterBtn.setOnClickListener(v -> {
            if (enterBtn.getVisibility() == View.VISIBLE) {
                Game.endTurn();
                placedTilesCount = 0;
                if(Game.charList.size() >= 7) {
                    for (Button handTile : handTiles) {
                        if (!handTile.isShown()) {
                            char letter = Game.getRandomChar();
                            handTile.setText(String.valueOf(letter));
                            handTile.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    finish();
                }

                isFirstWord = false;
                enterBtn.setVisibility(View.INVISIBLE);
                swapBtn.setVisibility(View.VISIBLE);
                returnBtn.setVisibility(View.INVISIBLE);
            }
        });

        swapBtn.setOnClickListener(v -> {
            if(Game.charList.size() >= 7) {
                for (Button handTile : handTiles) {
                    char letter = Game.getRandomChar();
                    Game.addChar(handTile.getText().charAt(0));
                    handTile.setText(String.valueOf(letter));
                }
            }
        });
    }

    private void boardCellClick(Button boardCell, int row, int col) {
        if (selectedChar != '0' && boardCell.getText().toString().isEmpty()) {
            Game.addTile(boardCell, selectedChar, row, col, gameBoard);
            Game.hasNeighbours(gameBoard, row, col);
            findViewById(R.id.return_button_image).setVisibility(View.VISIBLE);
            findViewById(R.id.swap_button_image).setVisibility(View.INVISIBLE);

            if (prevSelectedHandTileId != -1) {
                findViewById(prevSelectedHandTileId).setVisibility(View.GONE);
            }
            selectedChar = '0';
            placedTilesCount++;
            turn();
        }
    }

    private void handTileClick(Button button) {
        if (prevSelectedHandTileId != -1 && prevSelectedHandTileId != button.getId()) {
            Button prevSelectedButton = findViewById(prevSelectedHandTileId);
            ViewGroup.LayoutParams prevLayoutParams = prevSelectedButton.getLayoutParams();
            prevLayoutParams.width = (int) (prevSelectedButton.getWidth() / 1.2);
            prevLayoutParams.height = (int) (prevSelectedButton.getHeight() / 1.2);
            prevSelectedButton.setLayoutParams(prevLayoutParams);
        }

        selectedChar = button.getText().charAt(0);
        if (prevSelectedHandTileId != button.getId()) {
            int newWidth = (int) (button.getWidth() * 1.2);
            int newHeight = (int) (button.getHeight() * 1.2);

            ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
            layoutParams.width = newWidth;
            layoutParams.height = newHeight;

            button.setLayoutParams(layoutParams);
            prevSelectedHandTileId = button.getId();
        }
    }

    private Button getBoardCell(int row, int col) {
        Pair<Integer, Integer> pos = new Pair<>(row, col);
        if(w3.contains(pos)) {
            return new Button(new android.view.ContextThemeWrapper(this, R.style.W3Place), null, 0);
        }
        if(l3.contains(pos)) {
            return new Button(new android.view.ContextThemeWrapper(this, R.style.L3Place), null, 0);
        }
        if(l2.contains(pos)) {
            return new Button(new android.view.ContextThemeWrapper(this, R.style.L2Place), null, 0);
        }
        if(pos.equals(new Pair<>(4, 4))) {
            return new Button(new android.view.ContextThemeWrapper(this, R.style.StartPlace), null, 0);
        }
        return new Button(new android.view.ContextThemeWrapper(this, R.style.DefaultPlace), null, 0);
    }

    private void turn () {
        if(!isFirstWord && Game.checkWordInFile(this, Game.getWord(gameBoard))) {
            findViewById(R.id.enter_button_image).setVisibility(View.VISIBLE);
        }
        else if(isFirstWord && gameBoard[4][4] != 0 && Game.checkWordInFile(this, Game.getWord(gameBoard))) {
            findViewById(R.id.enter_button_image).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.enter_button_image).setVisibility(View.INVISIBLE);
        }

    }
}