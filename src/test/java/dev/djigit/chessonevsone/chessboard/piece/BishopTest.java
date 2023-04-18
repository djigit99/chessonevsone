package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Bishop;
import dev.djigit.chessonevsone.game.chessboard.piece.King;
import dev.djigit.chessonevsone.game.chessboard.piece.Pawn;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {
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
        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H5, bishop);
        piecesMap.put(CellModel.Coords.G4, null);
        piecesMap.put(CellModel.Coords.F3, null);

        boolean movePossible = bishop.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H5, bishop);
        piecesMap.put(CellModel.Coords.G4, null);
        piecesMap.put(CellModel.Coords.F3, new Pawn(Player.Color.BLACK, null));

        boolean movePossible = bishop.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H5, bishop);
        piecesMap.put(CellModel.Coords.G6, null);
        piecesMap.put(CellModel.Coords.F7, new King(Player.Color.WHITE, null));

        boolean movePossible = bishop.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withPieceOnWay_Test() {
        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.H5, bishop);
        piecesMap.put(CellModel.Coords.G6, new King(Player.Color.WHITE, null));
        piecesMap.put(CellModel.Coords.F7, null);

        boolean movePossible = bishop.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }
}
