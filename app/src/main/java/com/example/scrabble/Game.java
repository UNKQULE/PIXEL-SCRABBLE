package com.example.scrabble;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final ArrayList<Character> charList;
    private static final Random random;

    private static final List<Integer> tileList;

    private static final List<Drawable> cellBackgroundList;

    private static final List<Pair<Integer, Integer>> cellPosList;

    private static String direction = "none";

    private static boolean accessToFind = true;


    static  {
        charList = new ArrayList<>();
        random = new Random();
        tileList = new ArrayList<>();
        cellBackgroundList = new ArrayList<>();
        cellPosList = new ArrayList<>();
        initializeList();
    }

    private static void initializeList() {
        addChars('E', 12);
        addChars('A', 9);
        addChars('I', 9);
        addChars('O', 8);
        addChars('N', 6);
        addChars('R', 6);
        addChars('T', 6);
        addChars('L', 4);
        addChars('S', 4);
        addChars('U', 4);
        addChars('D', 4);
        addChars('G', 3);
        addChars('B', 2);
        addChars('C', 2);
        addChars('M', 2);
        addChars('P', 2);
        addChars('F', 2);
        addChars('H', 2);
        addChars('V', 2);
        addChars('W', 2);
        addChars('Y', 2);
        addChars('K', 1);
        addChars('J', 1);
        addChars('X', 1);
        addChars('Q', 1);
        addChars('Z', 1);
    }

    private static void addChars(char character, int count) {
        for (int i = 0; i < count; i++) {
            charList.add(character);
        }
    }

    public static void addChar(char c) {
        charList.add(c);
    }

    public static void removeChar(char c) {
        charList.remove((Character) c);
    }

    public static char getRandomChar() {
        return charList.get(random.nextInt(charList.size()));
    }

    public static void hasNeighbours(char[][] array, int row, int col) {
        boolean hasVerticalNeighbour = false;
        boolean hasHorizontalNeighbour = false;
        direction = "none";
        if (row > 0 && array[row - 1][col] != 0) { // Check top cell
            direction = "bottom";
            hasVerticalNeighbour = true;
        }
        if (row < 8 && array[row + 1][col] != 0) { // Check bottom cell
            direction = "top";
            hasVerticalNeighbour = true;
        }
        if (col > 0 && array[row][col - 1] != 0) { // Check left cell
            direction = "right";
            hasHorizontalNeighbour = true;
        }
        if (col < 8 && array[row][col + 1] != 0) { // Check right cell
            direction = "left";
            hasHorizontalNeighbour = true;
        }

        if(hasHorizontalNeighbour && hasVerticalNeighbour) {
            accessToFind = false;
        }
    }

    public static void addTile(Button button, char value, int row, int col, char[][] gameBoard) {
        tileList.add(button.getId());
        cellBackgroundList.add(button.getBackground());
        cellPosList.add(new Pair<>(row, col));
        removeChar(value);
        button.setTextColor(Color.BLACK);
        button.setText(String.valueOf(value));
        button.setBackgroundResource(R.drawable.tile_image);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        button.setGravity(Gravity.CENTER);
        gameBoard[row][col] = value;
    }

    public static void endTurn() {
        tileList.clear();
        cellBackgroundList.clear();
        cellPosList.clear();
        direction = "none";
        accessToFind = true;
    }

    public static int returnTileId() {
        return tileList.remove(0);
    }

    public static Drawable returnCellBackground() {
        return cellBackgroundList.remove(0);
    }

    public static Pair<Integer, Integer> returnCellPosition() {
        return cellPosList.remove(0);
    }


    public static void undoLastMove(Button returnTile, char[][] gameBoard) {
        Drawable background = Game.returnCellBackground();
        Pair<Integer, Integer> pos = Game.returnCellPosition();
        Game.addChar(returnTile.getText().charAt(0));
        returnTile.setText("");
        returnTile.setBackground(background);
        gameBoard[pos.first][pos.second] = 0;
        direction = "none";
        accessToFind = true;
    }

    public static boolean checkWordInFile(Context context, String wordToFind) {
        Log.i("MyAppTag", "get" + wordToFind);
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open("dictionary.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(wordToFind)) {
                    Log.i("MyAppTag", "finded");
                    return true; // Слово найдено
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("MyAppTag", "not finded");
        return false; // Слово не найдено
    }

    public static String getWord(char[][] gameBoard) {
        if(!accessToFind) {
            return "Too many neighbors";
        }

        StringBuilder sb = new StringBuilder();
        int lastPosRow = cellPosList.get(cellPosList.size() - 1).first;
        int lastPosCol = cellPosList.get(cellPosList.size() - 1).second;
        boolean reverse = false;


        if(direction.equals("none")) {
            sb.append(gameBoard[lastPosRow][lastPosCol]);
        }
        if(direction.equals("bottom")) {
            reverse = true;
            if(lastPosRow != 8) {
                if(gameBoard[lastPosRow + 1][lastPosCol] != 0) {
                    sb.append(gameBoard[lastPosRow + 1][lastPosCol]);
                }
            }
            for(int i = lastPosRow; i >= 0; --i) {
                if(gameBoard[i][lastPosCol] != 0) {
                    sb.append(gameBoard[i][lastPosCol]);
                } else {
                    break;
                }
            }
        }
        if(direction.equals("top")) {
            reverse = false;
            if(lastPosRow != 0) {
                if(gameBoard[lastPosRow - 1][lastPosCol] != 0) {
                    sb.append(gameBoard[lastPosRow - 1][lastPosCol]);
                }
            }
            for(int i = lastPosRow; i < 9; ++i) {
                if(gameBoard[i][lastPosCol] != 0) {
                    sb.append(gameBoard[i][lastPosCol]);
                } else {
                    break;
                }
            }
        }
        if(direction.equals("right")) {
            reverse = true;
            if(lastPosCol != 8) {
                if(gameBoard[lastPosRow][lastPosCol + 1] != 0) {
                    sb.append(gameBoard[lastPosRow][lastPosCol + 1]);
                }
            }
            for(int i = lastPosCol; i >= 0; --i) {
                if(gameBoard[lastPosRow][i] != 0) {
                    sb.append(gameBoard[lastPosRow][i]);
                } else {
                    break;
                }
            }
        }
        if(direction.equals("left")) {
            reverse = false;
            if(lastPosCol != 0) {
                if(gameBoard[lastPosRow][lastPosCol - 1] != 0) {
                    sb.append(gameBoard[lastPosRow][lastPosCol - 1]);
                }
            }
            for(int i = lastPosCol; i < 9; ++i) {
                if(gameBoard[lastPosRow][i] != 0) {
                    sb.append(gameBoard[lastPosRow][i]);
                } else {
                    break;
                }
            }
        }

        if(!GameActivity.isFirstWord && sb.toString().length() < tileList.size() + 1) {
            return "The word must be a neighbor of the previous one";
        }
        if(sb.toString().length() < tileList.size()) {
            return "A word in two directions is unacceptable";
        }

        if(reverse) {
            return sb.reverse().toString();
        } else {
            return sb.toString();
        }


    }

}
