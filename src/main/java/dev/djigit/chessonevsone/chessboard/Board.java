package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.controller.ChessBoardController;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Cell> cells = new ArrayList<>();
    private ChessBoardController chessBoardController;
    private GridPane pane = new GridPane();

    public Board(ChessBoardController chessBoardController) {
        this.chessBoardController = chessBoardController;

        initBoard();
    }

    private void initBoard() {
        List<Pane> cellPanes = chessBoardController.getCellPanes();
        cellPanes.forEach(pane -> cells.add(new Cell(pane)));
    }

    public GridPane getPane() {
        return pane;
    }
}
