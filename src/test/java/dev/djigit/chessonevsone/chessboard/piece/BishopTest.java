package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest extends PieceTest {
    private static Bishop bishop;

    @BeforeAll
    static void setup() {
        bishop = new Bishop(Player.Color.WHITE, null);
    }

    @Test
    void getMoves_Test() {
        CellModel.Coords from = CellModel.Coords.C5;

        List<CellModel.Coords> expectedCoords = Arrays.asList(
                CellModel.Coords.D6,
                CellModel.Coords.D4,
                CellModel.Coords.B6,
                CellModel.Coords.B4,
                CellModel.Coords.E7,
                CellModel.Coords.E3,
                CellModel.Coords.A7,
                CellModel.Coords.A3,
                CellModel.Coords.F8,
                CellModel.Coords.F2,
                CellModel.Coords.G1
        );

        List<CellModel.Coords> actualMoves = bishop.getMoves(from);

        assertIterableEquals(expectedCoords, actualMoves);
    }

    @Test
    void getPath_Test() {
        CellModel.Coords from = CellModel.Coords.G1;
        CellModel.Coords to = CellModel.Coords.A7;

        CellModel.Coords[] expectedPath = new CellModel.Coords[] {
                CellModel.Coords.G1,
                CellModel.Coords.F2,
                CellModel.Coords.E3,
                CellModel.Coords.D4,
                CellModel.Coords.C5,
                CellModel.Coords.B6,
                CellModel.Coords.A7
        };

        CellModel.Coords[] actualPath = bishop.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void isMovePossible_withEmptyCells_Test() {
        String panelId1 = "panel_h5";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(bishop);

        String panelId2 = "panel_g4";
        Cell cell2 = generateCell(panelId2, false);

        String panelId3 = "panel_f3";
        Cell cell3 = generateCell(panelId3, false);

        boolean movePossible = bishop.isMovePossible(new Cell[]{cell1, cell2, cell3});

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        String panelId1 = "panel_h5";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(bishop);

        String panelId2 = "panel_g4";
        Cell cell2 = generateCell(panelId2, false);

        String panelId3 = "panel_f3";
        Cell cell3 = generateCell(panelId3, true);
        cell3.setPiece(new Pawn(Player.Color.BLACK, null));

        boolean movePossible = bishop.isMovePossible(new Cell[]{cell1, cell2, cell3});

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        String panelId1 = "panel_h5";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(bishop);

        String panelId2 = "panel_g6";
        Cell cell2 = generateCell(panelId2, false);

        String panelId3 = "panel_f7";
        Cell cell3 = generateCell(panelId3, true);
        cell3.setPiece(new King(Player.Color.WHITE, null));

        boolean movePossible = bishop.isMovePossible(new Cell[]{cell1, cell2, cell3});

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withPieceOnWay_Test() {
        String panelId1 = "panel_h5";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(bishop);

        String panelId2 = "panel_g6";
        Cell cell2 = generateCell(panelId2, true);
        cell2.setPiece(new King(Player.Color.WHITE, null));

        String panelId3 = "panel_f7";
        Cell cell3 = generateCell(panelId3, false);

        boolean movePossible = bishop.isMovePossible(new Cell[]{cell1, cell2, cell3});

        assertFalse(movePossible);
    }
}
