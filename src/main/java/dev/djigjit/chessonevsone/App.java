package dev.djigjit.chessonevsone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess One vs One");

        URL introSceneURL = getClass().getResource("/scenes/IntroScene.fxml");
        Parent parent = FXMLLoader.load(introSceneURL);

        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
