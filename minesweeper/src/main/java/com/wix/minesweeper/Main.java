package com.wix.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(StandardCharsets.UTF_8);

        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("application.fxml")) {
            Scene scene = new Scene(loader.load(Objects.requireNonNull(stream)));

            primaryStage.setTitle("Amazing Minesweeper");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
