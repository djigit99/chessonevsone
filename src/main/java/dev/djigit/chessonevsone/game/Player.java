package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.sockets.PlayerSocket;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;

public abstract class Player {
    private final Stage primaryStage;
    private Color color;
    protected PlayerSocket socket;
    protected GameBackView gameBackView;

    public Player(Stage stage) {
        this.primaryStage = stage;
    }

    void init() {
        gameBackView = new GameBackView();
        gameBackView.init();

        primaryStage.setScene(new Scene(gameBackView.getRootPane()));
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

    void initChessBoard() {
        final String CHESSBOARD_FOR_WHITE_SCENE_URL = "/scenes/ChessBoardSceneWhite.fxml";
        final String CHESSBOARD_FOR_BLACK_SCENE_URL = "/scenes/ChessBoardSceneBlack.fxml";

        URL url;
        if (getColor().isWhite()) {
            url = getClass().getResource(CHESSBOARD_FOR_WHITE_SCENE_URL);
        } else {
            url = getClass().getResource(CHESSBOARD_FOR_BLACK_SCENE_URL);
        }

        ChessBoard board = new ChessBoard(url, getColor());
        gameBackView.setChessBoard(board);

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
}
