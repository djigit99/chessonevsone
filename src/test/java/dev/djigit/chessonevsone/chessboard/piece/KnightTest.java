package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.piece.King;
import dev.djigit.chessonevsone.game.chessboard.piece.Knight;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest extends PieceTest {

    private static Knight knight;

    @BeforeAll
    static void setup() {
        knight = new Knight(Player.Color.WHITE, null);
    }

    @Test
    void getMoves_Test() {
        CellModel.Coords from = CellModel.Coords.D5;

        List<CellModel.Coords> expectedCoords = Arrays.asList(
                CellModel.Coords.E7,
                CellModel.Coords.F6,
                CellModel.Coords.F4,
                CellModel.Coords.E3,
                CellModel.Coords.C7,
                CellModel.Coords.B6,
                CellModel.Coords.B4,
                CellModel.Coords.C3
        );

        List<CellModel.Coords> actualMoves = knight.getMoves(from);

        assertIterableEquals(expectedCoords, actualMoves);
    }

    @Test
    void getPath_Test() {
        CellModel.Coords from = CellModel.Coords.A2;
        CellModel.Coords to = CellModel.Coords.B4;

        CellModel.Coords[] expectedPath = new CellModel.Coords[]{from, to};

        CellModel.Coords[] actualPath = knight.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void isMovePossible_withEmptyCell_Test() {
        knight.setPieceColor(Player.Color.BLACK);

        String panelId1 = "panel_b7";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(knight);

        String panelId2 = "panel_c5";
        Cell cell2 = generateCell(panelId2, false);

        boolean movePossible = knight.isMovePossible(new Cell[]{cell1, cell2});

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        knight.setPieceColor(Player.Color.BLACK);

        String panelId1 = "panel_f6";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(knight);

        String panelId2 = "panel_e4";
        Cell cell2 = generateCell(panelId2, true);
        cell2.setPiece(new King(Player.Color.BLACK, null));

        boolean movePossible = knight.isMovePossible(new Cell[]{cell1, cell2});

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        knight.setPieceColor(Player.Color.BLACK);

        String panelId1 = "panel_c2";
        Cell cell1 = generateCell(panelId1, true);
        cell1.setPiece(knight);

        String panelId2 = "panel_e1";
        Cell cell2 = generateCell(panelId2, true);
        cell2.setPiece(new King(Player.Color.WHITE, null));

        boolean movePossible = knight.isMovePossible(new Cell[]{cell1, cell2});

        assertTrue(movePossible);
    }

}
