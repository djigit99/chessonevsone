package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest extends PieceTest{
    private static Rook rook;

    @BeforeAll
    static void setup() {
        rook = new Rook(Player.Color.WHITE, null);
    }

    @Test
    void getMoves_Test() {
        CellModel.Coords from = CellModel.Coords.E4;

        List<CellModel.Coords> expectedCoords = Arrays.asList(
                CellModel.Coords.A4,
                CellModel.Coords.B4,
                CellModel.Coords.C4,
                CellModel.Coords.D4,
                CellModel.Coords.F4,
                CellModel.Coords.G4,
                CellModel.Coords.H4,
                CellModel.Coords.E1,
                CellModel.Coords.E2,
                CellModel.Coords.E3,
                CellModel.Coords.E5,
                CellModel.Coords.E6,
                CellModel.Coords.E7,
                CellModel.Coords.E8
        );

        List<CellModel.Coords> actualMoves = rook.getMoves(from);

        assertIterableEquals(expectedCoords, actualMoves);
    }

    @Test
    void getPath_Test() {
        rook.setPieceColor(Player.Color.BLACK);

        CellModel.Coords from = CellModel.Coords.F3;
        CellModel.Coords to = CellModel.Coords.F7;

        CellModel.Coords[] expectedCoords = new CellModel.Coords[]{
                CellModel.Coords.F3,
                CellModel.Coords.F4,
                CellModel.Coords.F5,
                CellModel.Coords.F6,
                CellModel.Coords.F7
        };

        CellModel.Coords[] actualCoords = rook.getPath(from, to);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    void isMovePossible_Test() {
        rook.setPieceColor(Player.Color.BLACK);

        String paneId1 = "panel_g5";

        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(rook);

        String paneId2 = "panel_g6";

        Cell cell2 = generateCell(paneId2, false);

        boolean movePossible = rook.isMovePossible(new Cell[]{cell1, cell2});

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_pieceIsOnThePath_Test() {
        rook.setPieceColor(Player.Color.WHITE);

        String paneId1 = "panel_d3";

        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(rook);

        String paneId2 = "panel_e3";

        Cell cell2 = generateCell(paneId2, true);
        cell2.setPiece(new Queen(Player.Color.WHITE, null));

        String paneId3 = "panel_f3";

        Cell cell3 = generateCell(paneId3, false);

        String paneId4 = "panel_g3";

        Cell cell4 = generateCell(paneId4, false);

        boolean movePossible = rook.isMovePossible(new Cell[]{cell1, cell2, cell3, cell4});

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        rook.setPieceColor(Player.Color.WHITE);

        String paneId1 = "panel_h8";

        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(rook);

        String paneId2 = "panel_h7";

        Cell cell2 = generateCell(paneId2, false);
        cell2.setPiece(new Queen(Player.Color.WHITE, null));

        String paneId3 = "panel_h6";

        Cell cell3 = generateCell(paneId3, false);

        String paneId4 = "panel_h5";

        Cell cell4 = generateCell(paneId4, true);
        cell4.setPiece(new Knight(Player.Color.BLACK, null));

        boolean movePossible = rook.isMovePossible(new Cell[]{cell1, cell2, cell3, cell4});

        assertTrue(movePossible);
    }
}
