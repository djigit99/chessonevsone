package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.cell.CellViewModel;
import dev.djigit.chessonevsone.game.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PawnTest extends PieceTest {
    private static Pawn pawn;

    @BeforeAll
    static void setup() {
        pawn = new Pawn(Player.Color.WHITE, null);
    }

    @Test
    public void getMoves_initialCords_forWhite_Test() {
        CellModel.Coords coords = CellModel.Coords.D2;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.D3,
                CellModel.Coords.D4,
                CellModel.Coords.E3,
                CellModel.Coords.C3
                );

        List<CellModel.Coords> actualMoves = pawn.getMoves(coords);

        assertIterableEquals(expectedMoves, actualMoves);
    }

    @Test
    public void getMoves_initialCords_forBlack_Test() {
        pawn.setPieceColor(Player.Color.BLACK);

        CellModel.Coords coords = CellModel.Coords.D7;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.D6,
                CellModel.Coords.D5,
                CellModel.Coords.E6,
                CellModel.Coords.C6
        );

        List<CellModel.Coords> actualMoves = pawn.getMoves(coords);

        assertIterableEquals(expectedMoves, actualMoves);
    }

    @Test
    public void getMoves_differentCords_Test() {
        pawn.setPieceColor(Player.Color.WHITE);

        CellModel.Coords coords = CellModel.Coords.B4;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.B5,
                CellModel.Coords.C5,
                CellModel.Coords.A5
        );

        List<CellModel.Coords> actualMoves = pawn.getMoves(coords);

        assertIterableEquals(expectedMoves, actualMoves);
    }


    @Test
    void getPath_Test() {
        pawn.setPieceColor(Player.Color.WHITE);

        CellModel.Coords from = CellModel.Coords.A2;
        CellModel.Coords to = CellModel.Coords.A4;

        List<CellModel.Coords> expectedPath = Arrays.asList(CellModel.Coords.A2, CellModel.Coords.A3, CellModel.Coords.A4);

        CellModel.Coords[] actualPath = pawn.getPath(from, to);

        assertIterableEquals(expectedPath, Arrays.asList(actualPath));
    }

    @Test
    void isMovePossible_Test() {
        String paneId1 = "panel_b2";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(pawn);

        String paneId2 = "panel_b3";
        Cell cell2 = generateCell(paneId2, false);

        boolean movePossible = pawn.isMovePossible(new Cell[]{cell1, cell2});

        assertTrue(movePossible);
    }
}
