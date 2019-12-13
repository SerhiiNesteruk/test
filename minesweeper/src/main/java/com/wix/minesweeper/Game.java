package com.wix.minesweeper;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Game {

    static int WIDTH = 9;
    static int HEIGHT = 8;
    static int MINES_COUNT = 10;

    boolean[][] isOpened;
    boolean[][] isFlagged;
    int[][] numbers;
    int[] minesX;
    int[] minesY;
    boolean win = false;
    boolean lose = false;

    public Game(int[] minesX, int[] minesY) {
        isOpened = new boolean[WIDTH][];
        isFlagged = new boolean[WIDTH][];
        numbers = new int[WIDTH][];

        for (int i = 0; i < WIDTH; i++) {
            isOpened[i] = new boolean[HEIGHT];
            isFlagged[i] = new boolean[HEIGHT];
            numbers[i] = new int[HEIGHT];
        }

        MINES_COUNT = minesX.length;
        this.minesX = minesX;
        this.minesY = minesY;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int number = 0;
                for (int i = 0; i < minesX.length; i++) {
                    if ((x - minesX[i] == -1 || x - minesX[i] == 0 || x - minesX[i] == 1) &&
                            (y - minesY[i] == -1 || y - minesY[i] == 0 || y - minesY[i] == 1) &&
                            (!(x == minesX[i] && y == minesY[i]))) number++;
                }
                numbers[x][y] = number;
            }
        }
    }

    public boolean open(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return false;
        if (isFlagged[x][y]) return false;
        if (isOpened[x][y]) return false;
        for (int i = 0; i < minesX.length; i++)
            if (minesX[i] == x && minesY[i] == y) {
                lose = true;
                isOpened[x][y] = true;
                throw new RuntimeException("Boom!");
            }
        isOpened[x][y] = true;
        if (numbers[x][y] == 0) {
            open(x - 1, y - 1);
            open(x - 1, y);
            open(x - 1, y + 1);
            open(x, y - 1);
            open(x, y + 1);
            open(x + 1, y - 1);
            open(x + 1, y);
            open(x + 1, y + 1);
        }
        calcIsWin();
        return isOpened[x][y];
    }

    public void calcIsWin() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (isOpened[x][y] == false) {
                    boolean isMine = false;
                    for (int i = 0; i < MINES_COUNT; i++) {
                        if (minesX[i] == x && minesY[i] == y) isMine = true;
                    }
                    if (!isMine) {
                        win = false;
                        return;
                    }
                }
            }
        }
        win = true;
    }

    boolean flagUnFlag(int x, int y) {
        return isFlagged[x][y] = !isFlagged[x][y];
    }

    public boolean[][] getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(boolean[][] isOpened) {
        this.isOpened = isOpened;
    }

    public boolean[][] getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(boolean[][] isFlagged) {
        this.isFlagged = isFlagged;
    }

    public int[] getMinesX() {
        return minesX;
    }

    public void setMinesX(int[] minesX) {
        this.minesX = minesX;
    }

    public int[] getMinesY() {
        return minesY;
    }

    public void setMinesY(int[] minesY) {
        this.minesY = minesY;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isLose() {
        return lose;
    }

    public void setLose(boolean lose) {
        this.lose = lose;
    }

    public boolean containsMine(int x, int y) {
        for (int i = 0; i < minesX.length; i++)
            if (minesX[i] == x && minesY[i] == y)
                return true;
        return false;
    }

    public int flagsLeft() {
        return MINES_COUNT - (int) Arrays.stream(isFlagged).map(s -> IntStream.range(0, s.length)
                .mapToObj(idx -> s[idx])).flatMap(x -> x).filter(b -> b).count();
    }

    public int[][] getNumbers() {
        return numbers;
    }
    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }

    public static Game newBeginnerGame() { // 9 * 8 and 10 mines
        int[] minesX = new int[Game.MINES_COUNT];
        int[] minesY = new int[Game.MINES_COUNT];
        for (int i = 0; i < Game.MINES_COUNT; i++) {
            minesX[i] = ThreadLocalRandom.current().nextInt(Game.WIDTH);
            minesY[i] = ThreadLocalRandom.current().nextInt(Game.HEIGHT);
        }
        return new Game(minesX, minesY);
    }

    public static Game newIntermediateGame() { // 15 * 13 and 40 mines
        return newBeginnerGame(); // TODO
    }

    public static Game newAdvancedGame() { // 30 * 16 and 99 mines
        return newBeginnerGame(); // TODO
    }
}
