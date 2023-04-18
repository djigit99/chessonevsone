package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

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
    void getPath_whenShortCastling_Test() {
        king.setPieceColor(Player.Color.WHITE);

        CellModel.Coords from = CellModel.Coords.E1;
        CellModel.Coords to = CellModel.Coords.H1;

        CellModel.Coords[] expectedPath = new CellModel.Coords[]
                {from, from.getByCoords((short) 1, (short) 0), from.getByCoords((short) 2, (short) 0), to};

        CellModel.Coords[] actualPath = king.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void getPath_whenLongCastling_Test() {
        king.setPieceColor(Player.Color.BLACK);

        CellModel.Coords from = CellModel.Coords.E8;
        CellModel.Coords to = CellModel.Coords.A8;

        CellModel.Coords[] expectedPath = new CellModel.Coords[]
                {from, from.getByCoords((short) -1, (short) 0), from.getByCoords((short) -2, (short) 0),
                        from.getByCoords((short) -3, (short) 0), to};

        CellModel.Coords[] actualPath = king.getPath(from, to);

        assertArrayEquals(expectedPath, actualPath);
    }

    @Test
    void isMovePossible_withEmptyPath_Test() {
        king.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.D8, king);
        piecesMap.put(CellModel.Coords.E7, null);

        boolean movePossible = king.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        king.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H8, king);
        piecesMap.put(CellModel.Coords.G8, new Queen(Player.Color.BLACK, null));

        boolean movePossible = king.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        king.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.D7, king);
        piecesMap.put(CellModel.Coords.D6, new Queen(Player.Color.WHITE, null));

        boolean movePossible = king.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_castling_Test() {
        king.setPieceColor(Player.Color.BLACK);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.E8, king);
        piecesMap.put(CellModel.Coords.F8, null);
        piecesMap.put(CellModel.Coords.G8, null);
        piecesMap.put(CellModel.Coords.H8, new Rook(Player.Color.BLACK, null));

        boolean movePossible = king.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_castlingAndPiece_Test() {
        king.setPieceColor(Player.Color.BLACK);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.E8, king);
        piecesMap.put(CellModel.Coords.F8, new Bishop(Player.Color.WHITE, null));
        piecesMap.put(CellModel.Coords.G8, null);
        piecesMap.put(CellModel.Coords.H8, new Rook(Player.Color.BLACK, null));

        boolean movePossible = king.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_castlingWithNoRook_Test() {
        king.setPieceColor(Player.Color.BLACK);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.E8, king);
        piecesMap.put(CellModel.Coords.F8, new Bishop(Player.Color.WHITE, null));
        piecesMap.put(CellModel.Coords.G8, null);
        piecesMap.put(CellModel.Coords.H8, null);

        boolean movePossible = king.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }
}
