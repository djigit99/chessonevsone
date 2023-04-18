package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Knight;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import dev.djigit.chessonevsone.game.chessboard.piece.Queen;
import dev.djigit.chessonevsone.game.chessboard.piece.Rook;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
    private static Queen queen;

    @BeforeAll
    static void setup() {
        queen = new Queen(Player.Color.WHITE, null);
    }


    @Test
    void getMoves_Test() {
        CellModel.Coords from = CellModel.Coords.D1;

        List<CellModel.Coords> expectedMoves = Arrays.asList(
                CellModel.Coords.A1, CellModel.Coords.B1, CellModel.Coords.C1,
                CellModel.Coords.E1, CellModel.Coords.F1, CellModel.Coords.G1,
                CellModel.Coords.H1, CellModel.Coords.D2, CellModel.Coords.D3,
                CellModel.Coords.D4, CellModel.Coords.D5, CellModel.Coords.D6,
                CellModel.Coords.D7, CellModel.Coords.D8, CellModel.Coords.E2,
                CellModel.Coords.C2, CellModel.Coords.F3, CellModel.Coords.B3,
                CellModel.Coords.G4, CellModel.Coords.A4, CellModel.Coords.H5
        );

        List<CellModel.Coords> actualMoves = queen.getMoves(from);

        assertIterableEquals(expectedMoves, actualMoves);
    }

    @Test
    void getPath_Test() {
        queen.setPieceColor(Player.Color.BLACK);

        CellModel.Coords from = CellModel.Coords.A4;
        CellModel.Coords to = CellModel.Coords.F4;

        CellModel.Coords[] expectedPath = new CellModel.Coords[]{
                CellModel.Coords.A4, CellModel.Coords.B4,
                CellModel.Coords.C4, CellModel.Coords.D4,
                CellModel.Coords.E4, CellModel.Coords.F4
        };

        CellModel.Coords[] actualPath = queen.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void isMovePossible_withEmptyCells_Test() {
        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.G5, queen);
        piecesMap.put(CellModel.Coords.F4, null);
        piecesMap.put(CellModel.Coords.E3, null);
        piecesMap.put(CellModel.Coords.D2, null);

        boolean movePossible = queen.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        queen.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.G5, queen);
        piecesMap.put(CellModel.Coords.F4, null);
        piecesMap.put(CellModel.Coords.E3, new Knight(Player.Color.BLACK, null));
        piecesMap.put(CellModel.Coords.D2, null);

        boolean movePossible = queen.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        queen.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.G5, queen);
        piecesMap.put(CellModel.Coords.F4, null);
        piecesMap.put(CellModel.Coords.E3, null);
        piecesMap.put(CellModel.Coords.D2, new Rook(Player.Color.WHITE, null));

        boolean movePossible = queen.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }
}
