package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoard;
import dev.djigit.chessonevsone.game.chessboard.GameLogic;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Pawn;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameLogicTest {
    private static ChessBoard board;
    private static GameLogic gameLogic;

    @BeforeAll
    static void setup() {
        board = mock(ChessBoard.class);
        gameLogic = new GameLogic(board);
    }

    @Test
    public void moveIsNotPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A1;
        CellModel.Coords to = CellModel.Coords.B5;

        List<CellModel.Coords> pieceMoves = Arrays.asList(
            CellModel.Coords.A6,
            CellModel.Coords.A2,
            CellModel.Coords.B6
        );

        Piece piece = mock(Piece.class);
        when(piece.getMoves(from)).thenReturn(pieceMoves);

        assertFalse(gameLogic.isMovePossible(piece, from, to));
    }

    @Test
    public void moveIsNotPossible_pieceMoveIsNotPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A1;
        CellModel.Coords to = CellModel.Coords.B5;

        List<CellModel.Coords> pieceMoves = Arrays.asList(
                CellModel.Coords.A6,
                CellModel.Coords.A2,
                CellModel.Coords.B5
        );

        Piece piece = mock(Piece.class);
        when(piece.getMoves(from)).thenReturn(pieceMoves);
        when(piece.isMovePossible(any())).thenReturn(false);

        assertFalse(gameLogic.isMovePossible(piece, from, to));
    }

    @Test
    public void moveIsPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A1;
        CellModel.Coords to = CellModel.Coords.B5;

        List<CellModel.Coords> pieceMoves = Arrays.asList(
                CellModel.Coords.A6,
                CellModel.Coords.A2,
                CellModel.Coords.B5
        );

        Piece piece = mock(Piece.class);
        when(piece.getMoves(from)).thenReturn(pieceMoves);
        when(piece.isMovePossible(any())).thenReturn(true);

        assertTrue(gameLogic.isMovePossible(piece, from, to));
    }

    @Test
    public void isMoveEnPassant_Test() {
        CellModel.Coords from = CellModel.Coords.H5;
        CellModel.Coords to = CellModel.Coords.G6;

        Pawn whitePawn = mock(Pawn.class);

        Cell cell1 = generateCell("panel_h5", true);
        cell1.setPiece(whitePawn);

        Cell cell2 = generateCell("panel_g6", false);

        when(board.getCellsOnPath(new CellModel.Coords[]{from, to})).thenReturn(new Cell[]{cell1, cell2});

        boolean isMoveEnPassant = gameLogic.isMoveEnPassant(from, to);

        verify(whitePawn, times(1)).markLastMoveEnPassant();

        assertTrue(isMoveEnPassant);

    }

    @Test
    public void moveIsNotEnPassant_Test() {
        CellModel.Coords from = CellModel.Coords.H5;
        CellModel.Coords to = CellModel.Coords.G6;

        Pawn whitePawn = mock(Pawn.class);
        Pawn blackPawn = mock(Pawn.class);

        Cell cell1 = generateCell("panel_h5", true);
        cell1.setPiece(whitePawn);

        Cell cell2 = generateCell("panel_g6", true);
        cell1.setPiece(blackPawn);

        when(board.getCellsOnPath(new CellModel.Coords[]{from, to})).thenReturn(new Cell[]{cell1, cell2});

        boolean isMoveEnPassant = gameLogic.isMoveEnPassant(from, to);

        verify(whitePawn, times(0)).markLastMoveEnPassant();

        assertFalse(isMoveEnPassant);

    }

    @SuppressWarnings("unchecked")
    private Cell generateCell(String paneId, boolean containsPiece) {
        ObservableList<Node> children = (ObservableList<Node>) mock(ObservableList.class);
        Pane cellPane = mock(Pane.class);
        when(cellPane.getStyle()).thenReturn("");
        when(cellPane.getId()).thenReturn(paneId);
        when(cellPane.getChildren()).thenReturn(children);
        when(children.size()).thenReturn(containsPiece ? 1 : 0);

        return new Cell(cellPane);
    }
}
