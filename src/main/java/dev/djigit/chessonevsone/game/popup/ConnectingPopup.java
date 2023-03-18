package dev.djigit.chessonevsone.game.popup;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import javafx.scene.Scene;
import javafx.stage.Popup;

import java.net.URL;

public class ConnectingPopup extends Popup {

    public ConnectingPopup() {
        super();
        init();
    }

    private void init() {
        URL url = getClass().getResource("scenes/ConnectingPopUp.fxml");
        Scene scene = new Scene(FXMLLoaderFactory.getRootNode(url));
        setScene(scene);
        show();
    }
}
