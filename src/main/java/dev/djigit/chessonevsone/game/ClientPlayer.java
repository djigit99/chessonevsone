package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.sockets.GameClientSocket;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class ClientPlayer {
    private GameClientSocket clientSocket;
    private Stage primaryStage;
    private Player.Color color;

    public ClientPlayer(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void init() {
        connectWithServer();
    }

    private void connectWithServer() {
        Thread connectThread = new Thread(() -> {
            clientSocket = GameClientSocket.getInstance();
            clientSocket.setSetColorConsumer(c -> color = c);
            clientSocket.connect();
            Platform.runLater(this::showChessBoard);
        });
        connectThread.setDaemon(true);
        connectThread.start();
    }

    private void showChessBoard() {
        final String CHESSBOARD_FOR_WHITE_SCENE_URL = "/scenes/ChessBoardSceneWhite.fxml";
        final String CHESSBOARD_FOR_BLACK_SCENE_URL = "/scenes/ChessBoardSceneBlack.fxml";

        Parent chessBoardRootNode;
        URL url;
        if (color.isWhite()) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }
        chessBoardRootNode = FXMLLoaderFactory.getRootNode(url);
        primaryStage.setScene(new Scene(chessBoardRootNode));
        primaryStage.setX(getCenterXForChessBoard());
        primaryStage.setY(getCenterYForChessBoard());
        primaryStage.show();
    }

    private double getCenterXForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth() / 2 - primaryStage.getWidth() / 2;
    }

    private double getCenterYForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight() / 2 - primaryStage.getHeight() / 2;
    }
}
