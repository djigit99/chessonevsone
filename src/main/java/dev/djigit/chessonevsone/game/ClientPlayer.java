package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.sockets.GameClientSocket;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ClientPlayer {
    private GameClientSocket clientSocket;

    private Stage primaryStage;

    public ClientPlayer(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {
        connectWithServer();
    }

    private void connectWithServer() {
        Thread connectThread = new Thread(() -> {
            clientSocket = new GameClientSocket();
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
        if (clientSocket.getColor().isWhite()) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }
        chessBoardRootNode = FXMLLoaderFactory.getRootNode(url);
        primaryStage.setScene(new Scene(chessBoardRootNode));
        primaryStage.show();
    }
}