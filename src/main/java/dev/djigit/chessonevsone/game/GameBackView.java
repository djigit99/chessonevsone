package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.history.ChessBoardSnapshot;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import dev.djigit.chessonevsone.game.chessboard.state.LookInHistoryState;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Optional;

public class GameBackView {
    private ChessBoard chessBoard;
    private BorderPane rootPane;
    private GameHistory history;

    public GameBackView() {}

    public void init() {
        final String DEFAULT_CHESSBOARD_URL = "/scenes/GameScene.fxml";
        URL url = getClass().getResource(DEFAULT_CHESSBOARD_URL);
        rootPane = (BorderPane) FXMLLoaderFactory.getRootNode(url);

        addHandlers();
    }

    void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;

        BorderPane parentNode = chessBoard.getRootPane();
        rootPane.setCenter(parentNode);

        createGameHistory();

        chessBoard.setGameHistory(history);
    }

    private void createGameHistory() {
        history = new GameHistory(chessBoard.createSnapshot());
    }

    private void addHandlers() {
        HBox historyButtonsHBox = (HBox) rootPane.getRight();
        Button prevBtn = (Button) historyButtonsHBox.getChildren().get(0);
        Button nextBtn = (Button) historyButtonsHBox.getChildren().get(1);

        prevBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Optional<ChessBoardSnapshot> prevState = history.getPrevMove();
            prevState.ifPresent(chessBoard::restoreSnapshot);
            if (!(chessBoard.getBoardState() instanceof LookInHistoryState)) {
                chessBoard.getBoardState().changeState(new LookInHistoryState(chessBoard));
            }
        });

        nextBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Optional<ChessBoardSnapshot> nextState = history.getNextMove();
            nextState.ifPresent(chessBoard::restoreSnapshot);
            if (chessBoard.getBoardState() instanceof LookInHistoryState
                && history.isLastMove()) {
                chessBoard.getBoardState().changeToPreviousState();
            }
        });
    }

    protected BorderPane getRootPane() {
        return rootPane;
    }
}
