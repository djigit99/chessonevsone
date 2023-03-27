package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.sockets.GameClientSocket;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.net.URL;

public class ClientPlayer extends Player {
    private GameClientSocket clientSocket;
    private final Stage primaryStage;
    private Color color;

    public ClientPlayer(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void init() {
        primaryStage.setOnCloseRequest(null);
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

        URL url;
        if (color.isWhite()) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }

        ChessBoard board = new ChessBoard(primaryStage, url, color);
        board.showChessBoard();
        primaryStage.setOnCloseRequest(we -> clientSocket.close());
    }
}
