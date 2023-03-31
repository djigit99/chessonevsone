package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.piece.Piece;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
}
