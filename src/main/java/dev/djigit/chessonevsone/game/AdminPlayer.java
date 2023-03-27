package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.popup.ConnectingStage;
import dev.djigit.chessonevsone.game.popup.ErrorMessageStage;
import dev.djigit.chessonevsone.sockets.CantCreateServerException;
import dev.djigit.chessonevsone.sockets.GameCreatorSocket;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public class AdminPlayer extends Player {
    private GameCreatorSocket adminSocket;
    private final Stage primaryStage;
    private Color color;

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
        primaryStage.setOnCloseRequest(null);
        primaryStage.show();

        addListenersForChooseColorButtons(chooseColorNode);
    }

    private void addListenersForChooseColorButtons(Parent chooseColorNode) {
        Button whiteColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(0);
        Button blackColorBtn = (Button) ((VBox) chooseColorNode).getChildren().get(1);

        EventHandler<MouseEvent> setToWhiteHandler = me -> {
            System.out.println("Server: server picked up a white side");
            color = Color.WHITE;
            lookForAnOpponent();
        };

        EventHandler<MouseEvent> setToBlackHandler = me -> {
            System.out.println("Server: server picked up a black side");
            color = Color.BLACK;
            lookForAnOpponent();
        };

        whiteColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToWhiteHandler);
        blackColorBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, setToBlackHandler);
    }

    private void lookForAnOpponent() {
        try {
            adminSocket = GameCreatorSocket.getInstance();

            ConnectingStage connectingStage = new ConnectingStage();
            connectingStage.setOnCloseRequest(we -> adminSocket.close());

            Thread waitForOpponentThread = new Thread(() -> {
                adminSocket.startServer(color);
                Platform.runLater(() -> {
                    connectingStage.close();
                    showChessBoard();
                });
            });
            waitForOpponentThread.setDaemon(true);
            waitForOpponentThread.start();

            connectingStage.show();
        } catch (CantCreateServerException ex) {
            ErrorMessageStage error = new ErrorMessageStage(ex.getMessage());
            error.show();
            throw new RuntimeException(ex);
        }
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
        primaryStage.setOnCloseRequest(we -> adminSocket.close());
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
