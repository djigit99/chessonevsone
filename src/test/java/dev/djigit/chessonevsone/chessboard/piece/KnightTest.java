package dev.djigit.chessonevsone.chessboard.piece;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.King;
import dev.djigit.chessonevsone.game.chessboard.piece.Knight;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {

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

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.B7, knight);
        piecesMap.put(CellModel.Coords.C5, null);

        boolean movePossible = knight.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

    @Test
    void isMovePossible_withOwnPiece_Test() {
        knight.setPieceColor(Player.Color.BLACK);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.F6, knight);
        piecesMap.put(CellModel.Coords.E4, new King(Player.Color.BLACK, null));

        boolean movePossible = knight.isMovePossible(piecesMap);

        assertFalse(movePossible);
    }

    @Test
    void isMovePossible_withOpponentPiece_Test() {
        knight.setPieceColor(Player.Color.BLACK);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(CellModel.Coords.C2, knight);
        piecesMap.put(CellModel.Coords.E1, new King(Player.Color.WHITE, null));

        boolean movePossible = knight.isMovePossible(piecesMap);

        assertTrue(movePossible);
    }

}
