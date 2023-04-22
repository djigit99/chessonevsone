package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.ChessBoardModel;
import dev.djigit.chessonevsone.game.chessboard.GameLogic;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameLogicTest {
    private static ChessBoardModel boardModel;
    private static GameLogic gameLogic;

    @BeforeAll
    static void setup() {
        boardModel = mock(ChessBoardModel.class);
        gameLogic = new GameLogic(boardModel);
    }

    @Test
    public void moveIsNotPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A1;
        CellModel.Coords to = CellModel.Coords.B5;

        List<CellModel.Coords> pieceMoves = Arrays.asList(
            CellModel.Coords.A6,
            CellModel.Coords.A2,
            CellModel.Coords.B6
        );

        Piece piece = mock(Piece.class);
        when(piece.getMoves(from)).thenReturn(pieceMoves);

        assertFalse(gameLogic.isMovePossible(piece, from, to));
    }

    @Test
    public void moveIsNotPossible_pieceMoveIsNotPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A1;
        CellModel.Coords to = CellModel.Coords.B5;

        List<CellModel.Coords> pieceMoves = Arrays.asList(
                CellModel.Coords.A6,
                CellModel.Coords.A2,
                CellModel.Coords.B5
        );

        Piece piece = mock(Piece.class);
        when(piece.getMoves(from)).thenReturn(pieceMoves);
        when(piece.isMovePossible(any())).thenReturn(false);

        assertFalse(gameLogic.isMovePossible(piece, from, to));
    }

    @Test
    public void moveIsPossible_Test() {
        CellModel.Coords from = CellModel.Coords.A6;
        CellModel.Coords to = CellModel.Coords.C4;

        LinkedMap<CellModel.Coords, Piece> pieceOnPath = new LinkedMap<>();
        pieceOnPath.put(from, null);
        pieceOnPath.put(CellModel.Coords.B5, null);
        pieceOnPath.put(to, null);

        Piece bishop = new Bishop(Player.Color.WHITE, null);
        Piece king = new King(Player.Color.WHITE, null);

        Map<CellModel.Coords, Piece> pieceMap = new HashMap<>();
        pieceMap.put(from, bishop);
        pieceMap.put(CellModel.Coords.D8, king);

        when(boardModel.getPiecesByCoords(any())).thenReturn(pieceOnPath);
        when(boardModel.clone()).thenReturn(new ChessBoardModel(pieceMap, Player.Color.WHITE));

        assertTrue(gameLogic.isMovePossible(bishop, from, to));
    }

    @Test
    public void isMoveEnPassant_Test() {
        CellModel.Coords from = CellModel.Coords.H5;
        CellModel.Coords to = CellModel.Coords.G6;

        Pawn whitePawn = mock(Pawn.class);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(from, whitePawn);
        piecesMap.put(to, null);

        when(boardModel.getPiecesByCoords(new CellModel.Coords[]{from, to})).thenReturn(piecesMap);

        boolean isMoveEnPassant = gameLogic.isMoveEnPassant(from, to);

        verify(whitePawn, times(1)).markLastMoveEnPassant();

        assertTrue(isMoveEnPassant);

    }

    @Test
    public void moveIsNotEnPassant_Test() {
        CellModel.Coords from = CellModel.Coords.H5;
        CellModel.Coords to = CellModel.Coords.G6;

        Pawn whitePawn = mock(Pawn.class);
        Pawn blackPawn = mock(Pawn.class);

        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(from, whitePawn);
        piecesMap.put(to, blackPawn);

        when(boardModel.getPiecesByCoords(new CellModel.Coords[]{from, to})).thenReturn(piecesMap);

        boolean isMoveEnPassant = gameLogic.isMoveEnPassant(from, to);

        verify(whitePawn, times(0)).markLastMoveEnPassant();

        assertFalse(isMoveEnPassant);

    }

    @Test
    public void doesPieceAttack_Test() {
        Rook rook = new Rook(Player.Color.WHITE, null);
        Knight enemyKnight = new Knight(Player.Color.BLACK, null);
        CellModel.Coords rookCoords = CellModel.Coords.B5;
        CellModel.Coords knightCoords = CellModel.Coords.H5;


        LinkedMap<CellModel.Coords, Piece> piecesMap = new LinkedMap<>();
        piecesMap.put(rookCoords, rook);
        piecesMap.put(knightCoords, enemyKnight);
        CellModel.Coords[] pieceOnPath = new CellModel.Coords[]{
                rookCoords, rookCoords.getByCoords((short) 1, (short) 0), rookCoords.getByCoords((short) 2, (short) 0),
                rookCoords.getByCoords((short) 3, (short) 0), rookCoords.getByCoords((short) 4, (short) 0),
                rookCoords.getByCoords((short) 5, (short) 0), knightCoords
        };

        when(boardModel.getPiecesByCoords(pieceOnPath)).thenReturn(piecesMap);

        rook.doesPieceAttack(rookCoords, knightCoords, boardModel);
    }
}
