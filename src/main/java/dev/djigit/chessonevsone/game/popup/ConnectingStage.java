package dev.djigit.chessonevsone.game.popup;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import java.net.URL;

public class ConnectingStage extends Stage {

    public ConnectingStage() {
        super();
        init();
    }

    private void init() {
        initModality(Modality.APPLICATION_MODAL);
        URL url = getClass().getResource("/scenes/ConnectingPopUp.fxml");
        Scene scene = new Scene(FXMLLoaderFactory.getRootNode(url));
        setScene(scene);
    }
}
