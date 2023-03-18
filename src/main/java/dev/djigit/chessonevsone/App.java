package dev.djigit.chessonevsone;

import dev.djigit.chessonevsone.game.Game;
import javafx.application.Application;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game(primaryStage, null);
        game.start();
    }
}
