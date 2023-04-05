package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.history.ChessBoardSnapshot;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
import dev.djigit.chessonevsone.game.chessboard.state.ChessBoardState;
import dev.djigit.chessonevsone.game.chessboard.state.WaitForSelectedPieceState;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessBoard {
    private final Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCell = new HashMap<>();
    private URL url;
    private ChessBoardListener chessBoardListener;
    private BorderPane chessBoardRootPane;
    private final Player.Color playerColor;
    private ChessBoardState boardState;
    private final GameLogic gameLogic;
    private GameHistory gameHistory;

    public ChessBoard(URL url, Player.Color playerColor) {
        this.url = url;
        this.playerColor = playerColor;
        this.boardState = new WaitForSelectedPieceState(this);
        this.gameLogic = new GameLogic(this);

        initData();
    }

    private void initData() {

        try {
            Map<CellModel.Coords, Piece> coordsToPiece = new HashMap<>();
            ChessBoardController chessBoardController;
            FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader(url);

            this.chessBoardRootPane = loader.load();
            chessBoardController = loader.getController();

            chessBoardController.getImageViewsAndNames().forEach(imgVAN -> {
                CellModel.Coords pCoords = imgVAN.getPieceCoords();
                String[] pInfo = imgVAN.getName().split("_");
                String pName = pInfo[0];
                Player.Color pColor = pInfo[1].equals("w") ? Player.Color.WHITE : Player.Color.BLACK;

                if ("pawn".equals(pName))
                    coordsToPiece.put(pCoords, new Pawn(pColor, imgVAN.getImageView()));
                else if ("rook".equals(pName))
                    coordsToPiece.put(pCoords, new Rook(pColor, imgVAN.getImageView()));
                else if ("knight".equals(pName))
                    coordsToPiece.put(pCoords, new Knight(pColor, imgVAN.getImageView()));
                else if ("bishop".equals(pName))
                    coordsToPiece.put(pCoords, new Bishop(pColor, imgVAN.getImageView()));
                else if ("queen".equals(pName))
                    coordsToPiece.put(pCoords, new Queen(pColor, imgVAN.getImageView()));
                else if ("king".equals(pName))
                    coordsToPiece.put(pCoords, new King(pColor, imgVAN.getImageView()));
                else throw new RuntimeException("Unknown piece name");
            });


            chessBoardController.getCellPanesAndCoords().forEach(paneAndCoords -> {
                Cell newCell = new Cell(paneAndCoords.getCellPane());
                newCell.setPiece(coordsToPiece.getOrDefault(paneAndCoords.getCoords(),null));
                CellListener cellListener = newCell.getCellListener();
                coordsToCell.put(newCell.getCellViewModel().getModel().getCoords(), ImmutablePair.of(newCell, cellListener));
            });

            this.chessBoardListener = new ChessBoardListener(coordsToCell, this);
            coordsToCell.values().forEach(
                    pair -> pair.getLeft().getCellListener().setChessBoardListener(chessBoardListener));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Player.Color getPlayerColor() {
        return playerColor;
    }

    public boolean isMovePossible(Piece piece, CellModel.Coords from, CellModel.Coords to) {
        return gameLogic.isMovePossible(piece, from, to);
    }

    public void makeMove(CellModel.Coords from, CellModel.Coords to) {
        Cell fromCell = coordsToCell.get(from).getLeft();
        Cell toCell = coordsToCell.get(to).getLeft();

        Piece movingPiece = fromCell.cleanPiece();
        toCell.setPiece(movingPiece);

        ChessBoardSnapshot snapshot = createSnapshot();
        gameHistory.addMove(snapshot);
    }

    public ChessBoardState getBoardState() {
        return boardState;
    }

    public void changeState(ChessBoardState state) {
        this.boardState = state;
    }

    public Cell[] getCellsOnPath(CellModel.Coords[] coords) {
        final int pathLength = coords.length;
        Cell[] cellsOnPath = new Cell[pathLength];

        for (int i = 0; i < pathLength; i++) {
            Cell cell = coordsToCell.get(coords[i]).getLeft();
            cellsOnPath[i] = cell;
        }

        return cellsOnPath;
    }

    public BorderPane getRootPane() {
        return chessBoardRootPane;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

    public ChessBoardSnapshot createSnapshot() {
        Map<CellModel.Coords, Piece> coordsPieceMap = new HashMap<>();
        coordsToCell.forEach((coords, cCl) -> coordsPieceMap.put(coords, cCl.getLeft().getPiece()));
        return new ChessBoardSnapshot(coordsPieceMap);
    }

    public void restoreState(ChessBoardSnapshot snapshot) {
        Map<CellModel.Coords, Piece> state = snapshot.getState();
        state.forEach((coords, piece) -> coordsToCell.get(coords).getLeft().setPiece(piece));
    }

    public ChessBoardListener getChessBoardListener() {
        return chessBoardListener;
    }
}
