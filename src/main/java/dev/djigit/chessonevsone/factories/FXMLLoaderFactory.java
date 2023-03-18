package dev.djigit.chessonevsone.factories;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class FXMLLoaderFactory {

    public static FXMLLoader getFXMLLoader(URL url) {
        return new FXMLLoader(url);
    }

    public static Parent getRootNode(URL url) {
        FXMLLoader loader = getFXMLLoader(url);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
