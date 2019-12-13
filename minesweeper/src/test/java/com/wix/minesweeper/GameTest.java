package com.wix.minesweeper;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    @Test
    public void testTileIsOpenedAfterOpeningTileWithNoMine() {
        Game game = givenGameWithMinesInTheMiddle();

        Assert.assertFalse("tile 2,2 must be not opened", game.isOpened[2][2]);
        game.open(2, 2);
        Assert.assertTrue("tile 2,2 must be opened", game.isOpened[2][2]);
    }

    @Test
    public void testTileIsOpenedAfterOpeningTileWithMine() {
        Game game = givenGameWithMinesInTheMiddle();
        Assert.assertFalse("tile 3,3 must be not opened", game.isOpened[3][3]);

        try {
            game.open(3, 3);
        } catch (Exception ignore) {
        }
        Assert.assertTrue("tile 3,3 must be opened", game.isOpened[3][3]);
    }

    @Test
    public void testGameIsLostAfterOpeningTileWithMine() {
        Game game = givenGameWithMinesInTheMiddle();

        try {
            game.open(3, 3);
        } catch (Exception ignore) {
        }
        Assert.assertTrue("tile 3,3 must be opened", game.isLose());
    }

    @Test
    public void testTileIsFlaggedWithFlagAfterMarkingTile() {
        Game game = givenGameWithMinesInTheMiddle();

        Assert.assertFalse("tile 3,3 must not be flagged", game.isFlagged[3][3]);
        game.flagUnFlag(3, 3);
        Assert.assertTrue("tile 3,3 must be flagged", game.isFlagged[3][3]);
    }

    @Test
    public void testTileIsNotFlaggedWithFlagAfterUnMarkingTile() {
        Game game = givenGameWithMinesInTheMiddle();
        game.flagUnFlag(3, 3);

        Assert.assertTrue("tile 3,3 must be flagged", game.isFlagged[3][3]);
        game.flagUnFlag(3, 3);
        Assert.assertFalse("tile 3,3 must not be flagged", game.isFlagged[3][3]);
    }


    @Test
    public void testTileIsNotOpenedAfterOpeningFlaggedTile() {
        Game game = givenGameWithMinesInTheMiddle();
        game.flagUnFlag(3, 3);

        Assert.assertFalse("tile 3,3 must be not opened", game.isOpened[3][3]);
        game.open(3, 3);
        Assert.assertFalse("tile 3,3 must be not opened", game.isOpened[3][3]);
    }


    @Test
    public void testNumberIsKnownAfterOpeningTileWithNoMine() {
        Game game = givenGameWithMinesInTheMiddle();
        game.open(2, 2);

        Assert.assertEquals(1, game.numbers[2][2]);
    }

    @Test
    public void testTilesAreOpenedAfterCascadeOpening() {
        Game game = givenGameWithMinesInTheMiddle();
        game.open(0, 0);

        for (int x = 0; x < game.numbers.length; x++)
            for (int y = 0; y < game.numbers[x].length; y++)
                if (!game.containsMine(x, y))
                    Assert.assertTrue("tile " + x + "," + y + " must be opened", game.isOpened[x][y]);
    }

    @Test
    public void testTileNumbersAreKnownAfterCascadeOpening() {
        Game game = givenGameWithMinesInTheMiddle();
        game.open(0, 0);
        Assert.assertEquals(1, game.numbers[2][2]);
        Assert.assertEquals(2, game.numbers[3][2]);
        Assert.assertEquals(3, game.numbers[4][2]);
    }

    @Test
    public void testGameIsWinAfterOpeningAllTilesWithNoMine() {
        Game game = givenGameWithMinesInTheMiddle();

        Assert.assertFalse("Game must not be Win", game.isWin());
        for (int x = 0; x < game.numbers.length; x++)
            for (int y = 0; y < game.numbers[x].length; y++)
                if (!game.containsMine(x, y)) game.open(x, y);

        Assert.assertTrue("Game must be Win", game.isWin());
    }

    Game givenGameWithMinesInTheMiddle() {
        // _ _ _ _ _ _ _ _ _
        // _ _ _ _ _ _ _ _ _
        // _ _ 1 2 3 2 1 _ _
        // _ _ 2 * * * 2 _ _
        // _ _ 2 * * * 2 _ _
        // _ _ 1 2 3 2 1 _ _
        // _ _ _ _ _ _ _ _ _
        // _ _ _ _ _ _ _ _ _
        return new Game(new int[]{3, 4, 5, 3, 4, 5}, new int[]{3, 3, 3, 4, 4, 4});
    }
}
