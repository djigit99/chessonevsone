package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.popup.ConnectingStage;
import dev.djigit.chessonevsone.sockets.GameCreatorSocket;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class AdminPlayer {

    private GameCreatorSocket adminSocket;
    private final Stage primaryStage;
    private Player.Color color;

    public AdminPlayer(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {
        showChooseColorScene();
    }

    private void showChooseColorScene() {
        String CHOOSE_COLOR_SCENE_URL = "/scenes/ChooseColor.fxml";
        Parent chooseColorNode = FXMLLoaderFactory.getRootNode(getClass().getResource(CHOOSE_COLOR_SCENE_URL));
        primaryStage.setScene(new Scene(chooseColorNode));
        primaryStage.show();

        addListenersForChooseColorButtons(chooseColorNode);
    }

    private void addListenersForChooseColorButtons(Parent chooseColorNode) {
        Button whiteColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(0);
        Button blackColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(1);

        EventHandler<MouseEvent> setToWhiteHandler = me -> {
            System.out.println("Server: server picked up a white side");
            color = Player.Color.WHITE;
            lookForAnOpponent();
        };

        EventHandler<MouseEvent> setToBlackHandler = me -> {
            System.out.println("Server: server picked up a black side");
            color = Player.Color.BLACK;
            lookForAnOpponent();
        };

        whiteColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToWhiteHandler);
        blackColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToBlackHandler);
    }

    private void lookForAnOpponent() {

        ConnectingStage connectingStage = new ConnectingStage();
        connectingStage.setOnCloseRequest(we -> adminSocket.close());
        connectingStage.show();

        adminSocket = new GameCreatorSocket();
        Thread waitForOpponentThread = new Thread(() -> {
            adminSocket.startServer(color);
            Platform.runLater(() -> showChessBoard(connectingStage));
        });
        waitForOpponentThread.setDaemon(true);
        waitForOpponentThread.start();
    }

    private void showChessBoard(ConnectingStage connectingStage) {
        connectingStage.close();
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
        primaryStage.show();
    }


}
