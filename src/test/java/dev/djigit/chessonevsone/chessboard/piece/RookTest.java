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

public class RookTest {
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

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.G5, rook);
        piecesMap.put(CellModel.Coords.G6, null);

        boolean movePossible = rook.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_pieceIsOnThePath_Test() {
        rook.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.D3, rook);
        piecesMap.put(CellModel.Coords.E3, new Queen(Player.Color.WHITE, null));
        piecesMap.put(CellModel.Coords.F3, null);
        piecesMap.put(CellModel.Coords.G3, null);

        boolean movePossible = rook.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        rook.setPieceColor(Player.Color.WHITE);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H8, rook);
        piecesMap.put(CellModel.Coords.H7, null);
        piecesMap.put(CellModel.Coords.H6, null);
        piecesMap.put(CellModel.Coords.H5, new Knight(Player.Color.BLACK, null));

        boolean movePossible = rook.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }
}
