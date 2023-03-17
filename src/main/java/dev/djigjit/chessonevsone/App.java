package dev.djigjit.chessonevsone;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess One vs One");

        HBox hBox = new HBox();
        Button helloButton = new Button("Print 'Hello'");
        helloButton.setOnAction(e -> {
            System.out.println("Hello");
        });
        hBox.getChildren().add(helloButton);

        primaryStage.setScene(new Scene(hBox));
        primaryStage.show();
    }
}
