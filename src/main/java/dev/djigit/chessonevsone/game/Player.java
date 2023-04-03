package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.ChessBoard;
import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.sockets.PlayerSocket;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public abstract class Player {
    private final Stage primaryStage;
    private Color color;
    private BorderPane parentNode;
    protected PlayerSocket socket;

    public Player(Stage stage) {
        this.primaryStage = stage;
    }

    void init() {
        final String DEFAULT_CHESSBOARD_URL = "/scenes/GameScene.fxml";
        URL url = getClass().getResource(DEFAULT_CHESSBOARD_URL);

        parentNode = (BorderPane) FXMLLoaderFactory.getRootNode(url);
        primaryStage.setScene(new Scene(parentNode));
        primaryStage.setX(getCenterXForChessBoard());
        primaryStage.setY(getCenterYForChessBoard());
    }

    double getCenterXForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth() / 2 - primaryStage.getWidth() / 2;
    }

    double getCenterYForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight() / 2 - primaryStage.getHeight() / 2;
    }

    public enum Color {
        WHITE(true),
        BLACK(false);

        private boolean isWhite;

        Color(boolean isWhite) {
            this.isWhite = isWhite;
        }

        public boolean isWhite() {
            return isWhite;
        }
    }

    void showChessBoard() {
        final String CHESSBOARD_FOR_WHITE_SCENE_URL = "/scenes/ChessBoardSceneWhite.fxml";
        final String CHESSBOARD_FOR_BLACK_SCENE_URL = "/scenes/ChessBoardSceneBlack.fxml";

        URL url;
        if (getColor().isWhite()) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }

        ChessBoard board = new ChessBoard(getParentNode(), url, getColor());
        board.showChessBoard();
        getPrimaryStage().setOnCloseRequest(we -> socket.close());
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }

    void setColor(Color color) {
        this.color = color;
    }

    Color getColor() {
        return color;
    }

    BorderPane getParentNode() {
        return parentNode;
    }
}
