package com.wix.minesweeper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    private static String NO_TEXT = "";

    @FXML
    private Label status;

    private Collection<TileView> tiles;

    @FXML
    private TilePane tilePanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start(Game.newBeginnerGame());
    }

    public void startBeginnerGame() {
        start(Game.newBeginnerGame());
    }

    public void startIntermediateGame() {
        start(Game.newBeginnerGame());
    }

    public void startExpertGame() {
        start(Game.newAdvancedGame());
    }

    private void start(Game game) {
        status.setText(NO_TEXT);

        tiles = new LinkedList<>();
        for (int x = 0; x < game.WIDTH; x++) {
            for (int y = 0; y < game.HEIGHT; y++) {
                TileView tile = new TileView(x, y);
                tile.setOnMouseClicked(e -> onTileClick(tile, game, e.getButton()));
                updateTileView(tile, game);
                tiles.add(tile);
            }
        }

        tilePanel.setPrefColumns(game.WIDTH);
        tilePanel.setPrefRows(game.HEIGHT);
        tilePanel.getChildren().setAll(tiles);
        if (tilePanel.getScene() != null) { // no reference to scene on #initialize
            tilePanel.getScene().getWindow().sizeToScene();
        }
    }

    public void onTileClick(TileView tile, Game game, MouseButton button) {
        if (MouseButton.PRIMARY.equals(button)) {
            try {
                game.open(tile.x, tile.y);
                if (game.isWin()) status.setText("YOU WIN!");
                tiles.forEach(t -> updateTileView(t, game));
            } catch (Exception e) {
                e.printStackTrace();
                tiles.forEach(t -> updateTileView(t, game));
                status.setText(e.getMessage());
            }
        } else if (MouseButton.SECONDARY.equals(button)) {
            game.flagUnFlag(tile.x, tile.y);

            tiles.forEach(t -> updateTileView(t, game));
            if (!game.isWin() && !game.isLose()) {
                status.setText("Flags left: " + game.flagsLeft());
            }
        }
    }

    void updateTileView(TileView tile, Game game) {
        if (game.containsMine(tile.x, tile.y) && game.isOpened[tile.x][tile.y]) {
            tile.drawAsTileWithTriggeredMine();
        } else if (game.containsMine(tile.x, tile.y) && (game.isWin() || game.isLose())) {
            tile.drawAsTileWithMine();
        } else if (game.isFlagged[tile.x][tile.y]) {
            tile.drawAsTileWithFlag();
        } else if (!game.isOpened[tile.x][tile.y]) {
            tile.drawAsClosedTile();
        } else {
            tile.drawAsTileWithNumber(game.numbers[tile.x][tile.y]);
        }
    }

    private static class TileView extends StackPane {
        private static String MINE_EMOJI = "\u25cf";
        private static String FLAG_EMOJI = "\u2691";
        private Text text = new Text();
        private int x, y;

        TileView(int x, int y) {
            this.x = x;
            this.y = y;

            getChildren().addAll(
                    setStyles(new Rectangle(20, 20), "tile"),
                    setStyles(text, "text")
            );
        }

        void drawAsTileWithTriggeredMine() {
            setText(MINE_EMOJI).setStyle("cell", "triggered", "mine");
        }

        void drawAsTileWithMine() {
            setText(MINE_EMOJI).setStyle("cell", "mine");
        }

        void drawAsTileWithFlag() {
            setText(FLAG_EMOJI).setStyle("cell", "flag", "closed");
        }

        void drawAsClosedTile() {
            setText(NO_TEXT).setStyle("cell", "closed");
        }

        void drawAsTileWithNumber(int number) {
            if (number == 0) setText(NO_TEXT);
            else setText(String.valueOf(number));
            setStyle("cell", "opened", "n" + number);
        }

        private TileView setText(String value) {
            this.text.setText(value);
            return this;
        }

        private TileView setStyle(String... values) {
            return setStyles(this, values);
        }

        private static <T extends Node> T setStyles(T node, String... values) {
            node.getStyleClass().setAll(values);
            return node;
        }
    }
}
