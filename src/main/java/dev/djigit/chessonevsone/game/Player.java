package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import dev.djigit.chessonevsone.sockets.MessageType;
import dev.djigit.chessonevsone.sockets.PlayerSocket;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Player {
    private final Stage primaryStage;
    private Color color;
    protected PlayerSocket socket;
    protected GameBackView gameBackView;
    private final ExecutorService listenForMessagesService;
    private ChessBoardListener chessBoardListener;
    private Future<?> listenForMessagesFuture;
    private final PlayerListener playerListener;

    public Player(Stage stage) {
        this.primaryStage = stage;
        this.listenForMessagesService = Executors.newSingleThreadExecutor();
        this.playerListener = new PlayerListener(this);
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

        ChessBoard board = new ChessBoard(url, this);
        gameBackView.setChessBoard(board);

        chessBoardListener = board.getChessBoardListener();

        getPrimaryStage().setOnCloseRequest(we -> {
            boolean isFinished = listenForMessagesFuture.cancel(true);
            if (!isFinished)
                System.out.println("ListenForMessages task has not been finished.");
            listenForMessagesService.shutdownNow();

            socket.close();
        });
    }

    Stage getPrimaryStage() {
        return primaryStage;
    }

    void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    void sendMove(CellModel.Coords from, CellModel.Coords to) {
        socket.sendMessage(MessageType.OPP_MOVE, from.name() + " " + to.name());
    }

    void sendMove(CellModel.Coords from, CellModel.Coords to, Piece piece) {
        socket.sendMessage(MessageType.OPP_MOVE, from.name() + " " + to.name() + " " + piece.getName());
    }

    protected void listenForMessages() {
        listenForMessagesFuture = listenForMessagesService.submit(() -> {
            while (socket.isConnectionAlive()) {
                ImmutablePair<MessageType, String> msg = socket.getMessagesQueue().poll();
                if (msg != null) {
                    processMessageFromQueue(msg);
                }
            }
        });
    }

    private void processMessageFromQueue(ImmutablePair<MessageType, String> msg) {
        chessBoardListener.onUpdateFromPlayerReceived(msg);
    }



    public PlayerListener getListener() {
        return playerListener;
    }
}
