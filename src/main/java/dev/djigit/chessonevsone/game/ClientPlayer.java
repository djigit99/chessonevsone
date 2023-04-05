package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.sockets.GameClientSocket;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientPlayer extends Player {

    public ClientPlayer(Stage primaryStage) {
        super(primaryStage);
    }

    void init() {
        super.init();

        getPrimaryStage().setOnCloseRequest(null);
        connectWithServer();
    }

    private void connectWithServer() {
        Thread connectThread = new Thread(() -> {
            socket = GameClientSocket.getInstance();
            clientSocket().setSetColorConsumer(this::setColor);
            clientSocket().connect();

            listenForMessages(); // listen for and process the messages which have been got in the message queue

            Platform.runLater(this::initChessBoard);
        });
        connectThread.setDaemon(true);
        connectThread.start();
    }

    private GameClientSocket clientSocket() {
        return (GameClientSocket) socket;
    }
}
