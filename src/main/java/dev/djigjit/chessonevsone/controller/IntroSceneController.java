package dev.djigjit.chessonevsone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class IntroSceneController {

    private Stage stage;
    private Scene scene;

    public void chooseNewGameAction(ActionEvent event) throws IOException {
        URL resource = getClass().getResource("/scenes/ChooseGame.fxml");
        Parent chooseGamePane = FXMLLoader.load(resource);

        stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        scene = new Scene(chooseGamePane);
        stage.setScene(scene);
        stage.show();
    }
}
