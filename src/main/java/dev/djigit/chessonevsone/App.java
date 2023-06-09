package dev.djigit.chessonevsone;

import dev.djigit.chessonevsone.game.Game;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game(primaryStage);
        game.start();
    }
}
