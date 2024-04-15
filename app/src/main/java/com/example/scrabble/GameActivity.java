package com.example.scrabble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import android.os.CountDownTimer;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private static final char[][] gameBoard = new char[9][9];

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

    public static final List<Pair<Integer, Integer>> wordTiles = new ArrayList<>();
    private static final List<Integer> moddedTilesList = new ArrayList<>();

    private Tile[] handTiles;
    private int prevSelectedHandTileId = -1;
    private char selectedChar = '0';
    private String selectedScore = "";
    private int placedTilesCount = 0;
    public static boolean isFirstWord = true;

    public static boolean tripleWordMod = false;

    public int finalScore = 0;

    private static View gameboard;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView timerTextView = findViewById(R.id.timerTextView);
        int timeLimitSeconds = getIntent().getIntExtra("TIME_LIMIT", 0);
        new CountDownTimer (timeLimitSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(GameActivity.this, ScoreActivity.class);
                intent.putExtra("FINAL_SCORE", finalScore);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }.start();
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Score " + finalScore);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gameboard = gridLayout;
        initializeBoard(gridLayout);
        ConstraintLayout constraintLayout = findViewById(R.id.botConstraint);
        initializeHand(constraintLayout);
        initializeControlButtons();
    }


    private void initializeBoard(GridLayout gridLayout) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button boardCell = getBoardCell(row, col);
                boardCell.setId(View.generateViewId());
                Tile boardTile = new Tile(this);
                boardTile.setId(View.generateViewId());
                boardTile.setTag(new Pair<>(row, col));
                boardTile.setVisibility(View.GONE);
                int finalRow = row;
                int finalCol = col;
                boardCell.setOnClickListener(v -> boardCellClick(boardCell, boardTile, finalRow, finalCol));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1, 1f);
                params.columnSpec = GridLayout.spec(col, 1, 1f);
                params.setMargins(0, 0, 0, 0);

                gridLayout.addView(boardCell, params);
                gridLayout.addView(boardTile, params);
            }
        }
    }

    private void initializeHand(ConstraintLayout constraintLayout) {
        handTiles = new Tile[7];
        for (int i = 0; i < 7; i++) {
            Tile handTile = new Tile(this);
            handTile.setId(View.generateViewId());
            handTiles[i] = handTile;
            Pair<Character, String> tileValues = Game.getRandomChar();
            handTile.setLetter(String.valueOf(tileValues.first));
            handTile.setScore(String.valueOf(tileValues.second));
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

        int[] tilesIds = new int[handTiles.length];
        for (int i = 0; i < handTiles.length; i++) {
            tilesIds[i] = handTiles[i].getId();
        }

        constraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                tilesIds, null, ConstraintSet.CHAIN_SPREAD);

        constraintSet.applyTo(constraintLayout);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("FINAL_SCORE", finalScore);
        outState.putSerializable("GAME_BOARD", gameBoard);
        outState.putBoolean("IS_FIRST_WORD", isFirstWord);
        outState.putBoolean("TRIPLE_WORD_MOD", tripleWordMod);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        finalScore = savedInstanceState.getInt("FINAL_SCORE", 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeControlButtons() {
        ImageButton enterBtn = findViewById(R.id.enter_button_image);
        enterBtn.setVisibility(View.INVISIBLE);
        ImageButton returnBtn = findViewById(R.id.return_button_image);
        returnBtn.setVisibility(View.INVISIBLE);
        ImageButton swapBtn = findViewById(R.id.swap_button_image);
        TextView scoreTextView = findViewById(R.id.scoreTextView);


        returnBtn.setOnClickListener(v -> {
            if (placedTilesCount != 0) {
                for(int i = 0; i < placedTilesCount; ++i) {
                    Pair<Integer, Integer> ids = Game.returnCellAndTileId();
                    Button returnCell = findViewById(ids.first);
                    returnCell.setVisibility(View.VISIBLE);
                    Tile returnTile = findViewById(ids.second);
                    returnTile.setVisibility(View.GONE);
                    Game.undoLastMove(returnTile, gameBoard);
                }
                for (Tile handTile : handTiles) {
                    if (!handTile.isShown()) {
                        handTile.setVisibility(View.VISIBLE);
                    }
                }
                placedTilesCount = 0;
                tripleWordMod = false;
                returnTilesMod();
                wordTiles.clear();
                enterBtn.setVisibility(View.INVISIBLE);
                swapBtn.setVisibility(View.VISIBLE);
                returnBtn.setVisibility(View.INVISIBLE);
            }
        });

        enterBtn.setOnClickListener(v -> {
            if (enterBtn.getVisibility() == View.VISIBLE) {
                Game.endTurn();
                placedTilesCount = 0;
                finalScore += getScore();
                scoreTextView.setText("Score " + finalScore);
                if(Game.charList.size() >= 7) {
                    for (Tile handTile : handTiles) {
                        if (!handTile.isShown()) {
                            Pair<Character, String> tileValues = Game.getRandomChar();
                            handTile.setLetter(String.valueOf(tileValues.first));
                            handTile.setScore(String.valueOf(tileValues.second));
                            handTile.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    finish();
                }

                isFirstWord = false;
                tripleWordMod = false;
                enterBtn.setVisibility(View.INVISIBLE);
                swapBtn.setVisibility(View.VISIBLE);
                returnBtn.setVisibility(View.INVISIBLE);
            }
        });

        swapBtn.setOnClickListener(v -> {
            if(Game.charList.size() >= 7) {
                for (Tile handTile : handTiles) {
                    Game.addChar(handTile.getLetter(), handTile.getScore());
                    Pair<Character, String> tileValues = Game.getRandomChar();
                    handTile.setLetter(String.valueOf(tileValues.first));
                    handTile.setScore(String.valueOf(tileValues.second));
                }
            }
        });
    }

    private void boardCellClick(Button boardCell,Tile boardTile, int row, int col) {
        if (selectedChar != '0' && boardCell.getVisibility() == View.VISIBLE) {
            if (boardCell.getText() == "2") {
                selectedScore = String.valueOf(Integer.parseInt(selectedScore) * 2);
                boardTile.setL2();
            }
            if (boardCell.getText() == "3") {
                selectedScore = String.valueOf(Integer.parseInt(selectedScore) * 3);
                boardTile.setL3();
            }
            if (tripleWordMod) {
                selectedScore = String.valueOf(Integer.parseInt(selectedScore) * 3);
                boardTile.setW3();
            }
            if (boardCell.getText() == "33") {
                selectedScore = String.valueOf(Integer.parseInt(selectedScore) * 3);
                for(int i = 0; i < Game.cellAndTileList.size(); ++i) {
                    Tile modTile = findViewById(Game.cellAndTileList.get(i).second);
                    modTile.setScore(String.valueOf(Integer.parseInt(modTile.getScore()) * 3));
                    modTile.setW3();
                }
                boardTile.setW3();
                tripleWordMod = true;
            }
            boardCell.setVisibility(View.GONE);
            boardTile.setVisibility(View.VISIBLE);
            Game.addTile(boardCell, boardTile, selectedChar, selectedScore, row, col, gameBoard);
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

    public static void getTileW3Modification(Pair<Integer, Integer> tileTag) {
        Tile modTile = gameboard.findViewWithTag(tileTag);
        if(Game.cellPosList.contains(tileTag)) {
            if(!modTile.isModed()) {
                modTile.setScore(String.valueOf(Integer.parseInt(modTile.getScore()) * 3));
                modTile.setW3();
            }
        } else {
            modTile.setScore(String.valueOf(Integer.parseInt(modTile.getScore()) * 3));
            if(modTile.isModed()) {
                modTile.isModedTwice();
            }
            modTile.setW3();
            moddedTilesList.add(modTile.getId());
        }
    }

    private void returnTilesMod() {
        if(!moddedTilesList.isEmpty()) {
            for(int i = 0; i < moddedTilesList.size(); ++i) {
                Tile modTile = findViewById(moddedTilesList.get(i));
                modTile.setScore(String.valueOf(Integer.parseInt(modTile.getScore()) / 3));
                modTile.setPrevious();
            }
            moddedTilesList.clear();
        }

    }

    private int getScore() {
        int score = 0;
        for(int i = 0; i < wordTiles.size(); ++i) {
            Tile wordTile = gameboard.findViewWithTag(wordTiles.get(i));
            score += Integer.parseInt(wordTile.getScore());
        }
        wordTiles.clear();
        return score;
    }

    private void handTileClick(Tile tile) {
        if (prevSelectedHandTileId != -1 && prevSelectedHandTileId != tile.getId()) {
            Tile prevSelectedTile = findViewById(prevSelectedHandTileId);
            ViewGroup.LayoutParams prevLayoutParams = prevSelectedTile.getLayoutParams();
            prevLayoutParams.width = (int) (prevSelectedTile.getWidth() / 1.2);
            prevLayoutParams.height = (int) (prevSelectedTile.getHeight() / 1.2);
            prevSelectedTile.setLayoutParams(prevLayoutParams);
        }

        selectedChar = tile.getLetter();
        selectedScore = tile.getScore();
        if (prevSelectedHandTileId != tile.getId()) {
            int newWidth = (int) (tile.getWidth() * 1.2);
            int newHeight = (int) (tile.getHeight() * 1.2);

            ViewGroup.LayoutParams layoutParams = tile.getLayoutParams();
            layoutParams.width = newWidth;
            layoutParams.height = newHeight;

            tile.setLayoutParams(layoutParams);
            prevSelectedHandTileId = tile.getId();
        }
    }

    @SuppressLint("SetTextI18n")
    private Button getBoardCell(int row, int col) {
        Pair<Integer, Integer> pos = new Pair<>(row, col);
        Button boardCell;
        if(w3.contains(pos)) {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.W3Place), null, 0);
            boardCell.setText("33");
            return boardCell;
        }
        if(l3.contains(pos)) {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.L3Place), null, 0);
            boardCell.setText("3");
            return boardCell;
        }
        if(l2.contains(pos)) {
            boardCell = new Button(new android.view.ContextThemeWrapper(this, R.style.L2Place), null, 0);
            boardCell.setText("2");
            return boardCell;
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