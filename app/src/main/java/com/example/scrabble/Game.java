package com.example.scrabble;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static final ArrayList<Pair<Character, String>> charList;
    private static final Random random;

    public static final List<Pair<Integer, Integer>> cellAndTileList;


    private static final List<Pair<Integer, Integer>> cellPosList;

    private static String direction = "none";

    private static boolean accessToFind = true;
    private static boolean startDirectionIsNone = false;


    static  {
        charList = new ArrayList<>();
        random = new Random();
        cellAndTileList = new ArrayList<>();
        cellPosList = new ArrayList<>();
        initializeList();
    }

    private static void initializeList() {
        addChars('E', "1", 12);
        addChars('A', "1", 9);
        addChars('I', "1",9);
        addChars('O', "1", 8);
        addChars('N', "1", 6);
        addChars('R', "1", 6);
        addChars('T', "1", 6);
        addChars('L', "1", 4);
        addChars('S', "1", 4);
        addChars('U', "1", 4);
        addChars('D', "2", 4);
        addChars('G', "2", 3);
        addChars('B', "3",2);
        addChars('C', "3",2);
        addChars('M', "3",2);
        addChars('P', "3",2);
        addChars('F', "4",2);
        addChars('H', "4",2);
        addChars('V', "4",2);
        addChars('W', "4",2);
        addChars('Y', "4",2);
        addChars('K', "5",1);
        addChars('J', "8",1);
        addChars('X', "8",1);
        addChars('Q', "10",1);
        addChars('Z', "10",1);
    }

    private static void addChars(char character, String number, int count) {
        for (int i = 0; i < count; i++) {
            charList.add(new Pair<>(character, number));
        }
    }

    public static void addChar(char c, String num) {
        charList.add(new Pair<>(c, num));
    }

    public static void removeChar(char c, String num) {
        charList.remove(new Pair<>(c, num));
    }

    public static Pair<Character, String> getRandomChar() {
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
            if (!startDirectionIsNone) {
                direction = "top";
            } else {
                direction = "bottom";
            }

            hasVerticalNeighbour = true;
        }
        if (col > 0 && array[row][col - 1] != 0) { // Check left cell
            direction = "right";
            hasHorizontalNeighbour = true;
        }
        if (col < 8 && array[row][col + 1] != 0) { // Check right cell
            if(!startDirectionIsNone) {
                direction = "left";
            } else {
                direction = "right";
            }

            hasHorizontalNeighbour = true;
        }

        if(hasHorizontalNeighbour && hasVerticalNeighbour) {
            accessToFind = false;
        }
    }

    public static void addTile(Button cell, Tile tile, char value, String scoreValue, int row, int col, char[][] gameBoard) {
        cellAndTileList.add(new Pair<>(cell.getId(), tile.getId()));
        cellPosList.add(new Pair<>(row, col));
        removeChar(value, scoreValue);
        tile.setLetter(String.valueOf(value));
        tile.setScore(scoreValue);
        gameBoard[row][col] = value;
    }

    public static void endTurn() {
        cellAndTileList.clear();
        cellPosList.clear();
        direction = "none";
        accessToFind = true;
        startDirectionIsNone = false;
    }

    public static Pair<Integer, Integer> returnCellAndTileId() {
        return cellAndTileList.remove(0);
    }

    public static Pair<Integer, Integer> returnCellPosition() {
        return cellPosList.remove(0);
    }


    public static void undoLastMove(Tile returnTile, char[][] gameBoard) {
        returnTile.setDefault();
        Pair<Integer, Integer> pos = Game.returnCellPosition();
        Game.addChar(returnTile.getLetter(), returnTile.getScore());
        returnTile.setLetter("");
        returnTile.setScore("");
        gameBoard[pos.first][pos.second] = 0;
        direction = "none";
        accessToFind = true;
        startDirectionIsNone = false;
    }

    public static boolean checkWordInFile(Context context, String wordToFind) {
        Log.i("MyAppTag",  wordToFind);
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
        int firstPosRow = cellPosList.get(0).first;
        int firstPosCol = cellPosList.get(0).second;
        int lastPosRow = cellPosList.get(cellPosList.size() - 1).first;
        int lastPosCol = cellPosList.get(cellPosList.size() - 1).second;
        boolean reverse = false;

        Log.i("MyAppTag", direction);


        if(direction.equals("none")) {
            sb.append(gameBoard[lastPosRow][lastPosCol]);
            startDirectionIsNone = true;
        }
        if(direction.equals("bottom")) {
            if(startDirectionIsNone) {
                reverse = false;
                for(int i = firstPosRow; i < 9; ++i) {
                    if(gameBoard[i][firstPosCol] != 0) {
                        sb.append(gameBoard[i][firstPosCol]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(i, firstPosCol));
                        }
                    } else {
                        break;
                    }
                }
            } else {
                reverse = true;
                for(int i = lastPosRow; i >= 0; --i) {
                    if(gameBoard[i][lastPosCol] != 0) {
                        sb.append(gameBoard[i][lastPosCol]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(i, lastPosCol));
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        if(direction.equals("top")) {
            if(startDirectionIsNone) {
                reverse = true;
                for(int i = firstPosRow; i >= 0; --i) {
                    if(gameBoard[i][firstPosCol] != 0) {
                        sb.append(gameBoard[i][firstPosCol]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(i, firstPosCol));
                        }
                    } else {
                        break;
                    }
                }
            } else {
                reverse = false;
                for(int i = lastPosRow; i < 9; ++i) {
                    if(gameBoard[i][lastPosCol] != 0) {
                        sb.append(gameBoard[i][lastPosCol]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(i, lastPosCol));
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        if(direction.equals("right")) {
            if(startDirectionIsNone) {
                reverse = false;
                for(int i = firstPosCol; i < 9; ++i) {
                    if(gameBoard[firstPosRow][i] != 0) {
                        sb.append(gameBoard[firstPosRow][i]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(firstPosRow, i));
                        }
                    } else {
                        break;
                    }
                }
            } else {
                reverse = true;
                for(int i = lastPosCol; i >= 0; --i) {
                    if(gameBoard[lastPosRow][i] != 0) {
                        sb.append(gameBoard[lastPosRow][i]);
                        if(GameActivity.tripleWordMod) {
                            GameActivity.getTileW3Modification(new Pair<>(lastPosRow, i));
                        }
                    } else {
                        break;
                    }
                }
            }

        }
        if(direction.equals("left")) {
            if(startDirectionIsNone) {
                reverse = true;
                for(int i = firstPosCol; i >= 0; --i) {
                    if(gameBoard[firstPosRow][i] != 0) {
                        sb.append(gameBoard[firstPosRow][i]);
                    } else {
                        break;
                    }
                }
            } else {
                reverse = false;
                for(int i = lastPosCol; i < 9; ++i) {
                    if(gameBoard[lastPosRow][i] != 0) {
                        sb.append(gameBoard[lastPosRow][i]);
                    } else {
                        break;
                    }
                }
            }

        }

        Log.i("MyAppTag", sb.toString());

        if(!GameActivity.isFirstWord && sb.toString().length() < cellAndTileList.size() + 1) {
            return "The word must be a neighbor of the previous one";
        }
        if(sb.toString().length() < cellAndTileList.size()) {
            return "A word in two directions is unacceptable";
        }

        if(reverse) {
            return sb.reverse().toString();
        } else {
            return sb.toString();
        }
    }

}
