package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KingTest extends PieceTest {

    private static King king;

    @BeforeAll
    static void setup() {
        king = new King(Player.Color.WHITE, null);
    }

    @Test
    void getMoves_forWhite_Test() {
        CellModel.Coords fromCoords = CellModel.Coords.F2;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.G2,
                CellModel.Coords.G3,
                CellModel.Coords.G1,
                CellModel.Coords.E2,
                CellModel.Coords.E3,
                CellModel.Coords.E1,
                CellModel.Coords.F3,
                CellModel.Coords.F1
        );

        List<CellModel.Coords> actualMoves = king.getMoves(fromCoords);

        assertIterableEquals(expectedMoves, actualMoves);
    }

    @Test
    void getMoves_forBlack_Test() {
        king.setPieceColor(Player.Color.BLACK);

        CellModel.Coords fromCoords = CellModel.Coords.E6;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.F6,
                CellModel.Coords.F7,
                CellModel.Coords.F5,
                CellModel.Coords.D6,
                CellModel.Coords.D7,
                CellModel.Coords.D5,
                CellModel.Coords.E7,
                CellModel.Coords.E5
        );

        List<CellModel.Coords> actualMoves = king.getMoves(fromCoords);

        assertIterableEquals(expectedMoves, actualMoves);
    }

    @Test
    void getPath_Test() {
        CellModel.Coords from = CellModel.Coords.A5;
        CellModel.Coords to = CellModel.Coords.A6;

        CellModel.Coords[] expectedPath = new CellModel.Coords[] {from, to};

        CellModel.Coords[] actualPath = king.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void isMovePossible_withEmptyPath_Test() {
        king.setPieceColor(Player.Color.WHITE);

        String paneId1 = "panel_d8";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(king);

        String paneId2 = "panel_e7";
        Cell cell2 = generateCell(paneId2, false);

        Cell[] path = new Cell[]{cell1, cell2};

        boolean movePossible = king.isMovePossible(path);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        king.setPieceColor(Player.Color.WHITE);

        String paneId1 = "panel_h8";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(king);

        String paneId2 = "panel_g8";
        Cell cell2 = generateCell(paneId2, true);
        cell2.setPiece(new Queen(Player.Color.BLACK, null));

        Cell[] path = new Cell[]{cell1, cell2};

        boolean movePossible = king.isMovePossible(path);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        king.setPieceColor(Player.Color.WHITE);

        String paneId1 = "panel_d7";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(king);

        String paneId2 = "panel_d6";
        Cell cell2 = generateCell(paneId2, true);
        cell2.setPiece(new Queen(Player.Color.WHITE, null));

        Cell[] path = new Cell[]{cell1, cell2};

        boolean movePossible = king.isMovePossible(path);

        assertFalse(movePossible);
    }
}
