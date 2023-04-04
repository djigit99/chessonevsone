package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class PieceTest {

    @SuppressWarnings("unchecked")
    protected Cell generateCell(String paneId, boolean containsPiece) {
        ObservableList<Node> children = (ObservableList<Node>) mock(ObservableList.class);
        Pane cellPane = mock(Pane.class);
        when(cellPane.getStyle()).thenReturn("");
        when(cellPane.getId()).thenReturn(paneId);
        when(cellPane.getChildren()).thenReturn(children);
        when(children.size()).thenReturn(containsPiece ? 1 : 0);

        return new Cell(cellPane);
    }
}
