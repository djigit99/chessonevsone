package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.history.ChessBoardSnapshot;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import dev.djigit.chessonevsone.game.chessboard.piece.Pawn;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void isMovePossible_enPassant_Test() {
        pawn.setPieceColor(Player.Color.WHITE);
        pawn.setLastMove(new Piece.LastMove(CellModel.Coords.E4, CellModel.Coords.E5));
        ChessBoardSnapshot snapshot = new ChessBoardSnapshot(new HashMap<CellModel.Coords, Piece>(){{
            put(CellModel.Coords.E5, pawn);
        }});
        GameHistory gameHistory = new GameHistory(snapshot);

        // add opponent's last move
        Pawn blackPawn = new Pawn(Player.Color.BLACK, null);
        blackPawn.setLastMove(new Piece.LastMove(CellModel.Coords.D7, CellModel.Coords.D5));
        ChessBoardSnapshot nextBlackMove = new ChessBoardSnapshot(new HashMap<CellModel.Coords, Piece>(){{
            put(CellModel.Coords.E5, pawn);
            put(CellModel.Coords.D5, blackPawn);
        }});
        gameHistory.addMove(nextBlackMove, blackPawn);

        pawn.setGameHistory(gameHistory);

        String paneId1 = "panel_e5";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(pawn);

        String paneId2 = "panel_d6";
        Cell cell2 = generateCell(paneId2, false);

        boolean movePossible = pawn.isMovePossible(new Cell[]{cell1, cell2});

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_fakeEnPassant_Test() {
        pawn.setPieceColor(Player.Color.WHITE);
        pawn.setLastMove(new Piece.LastMove(CellModel.Coords.E4, CellModel.Coords.E5));
        Pawn blackPawn = new Pawn(Player.Color.BLACK, null);
        blackPawn.setLastMove(new Piece.LastMove(CellModel.Coords.C7, CellModel.Coords.C6));
        ChessBoardSnapshot snapshot = new ChessBoardSnapshot(new HashMap<CellModel.Coords, Piece>(){{
            put(CellModel.Coords.E5, pawn);
            put(CellModel.Coords.C6, blackPawn);
        }});
        GameHistory gameHistory = new GameHistory(snapshot);

        // add opponent's last move
        blackPawn.setLastMove(new Piece.LastMove(CellModel.Coords.C6, CellModel.Coords.D5));
        ChessBoardSnapshot nextBlackMove = new ChessBoardSnapshot(new HashMap<CellModel.Coords, Piece>(){{
            put(CellModel.Coords.E5, pawn);
            put(CellModel.Coords.D5, blackPawn);
        }});
        gameHistory.addMove(nextBlackMove, blackPawn);

        pawn.setGameHistory(gameHistory);

        String paneId1 = "panel_e5";
        Cell cell1 = generateCell(paneId1, true);
        cell1.setPiece(pawn);

        String paneId2 = "panel_d6";
        Cell cell2 = generateCell(paneId2, false);

        boolean movePossible = pawn.isMovePossible(new Cell[]{cell1, cell2});

        assertFalse(movePossible);
    }
}
