package dev.djigit.chessonevsone.chessboard;

import javafx.scene.layout.Pane;

public class Cell {

    public static final int CELL_SIZE = 100;

    private Pane cellPane;

    public Cell(Pane cellPane) {
        this.cellPane = cellPane;
    }
}
