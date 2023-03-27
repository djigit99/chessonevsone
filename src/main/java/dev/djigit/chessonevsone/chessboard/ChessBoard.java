package dev.djigit.chessonevsone.chessboard;

import dev.djigit.chessonevsone.chessboard.cell.Cell;
import dev.djigit.chessonevsone.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.chessboard.piece.*;
import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.Player;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {
    private Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCell = new HashMap<>();
    private URL url;
    private Stage primaryStage;
    private ChessBoardListener chessBoardListener;
    private Parent chessBoardRootNode;
    private Player.Color playerColor;
    private ChessBoardState boardState;

    public ChessBoard(Stage primaryStage, URL url, Player.Color playerColor) {
        this.primaryStage = primaryStage;
        this.url = url;
        this.playerColor = playerColor;
        this.boardState = new WaitForSelectedPieceState(this);

        initData();
    }

    private void initData() {

        try {
            ChessBoardController chessBoardController;
            FXMLLoader loader = FXMLLoaderFactory.getFXMLLoader(url);

            chessBoardRootNode = loader.load();
            chessBoardController = loader.getController();

            Map<CellModel.Coords, Piece> coordsToPiece = new HashMap<>();
            chessBoardController.getImageViewsAndNames().forEach(imgVAN -> {
                CellModel.Coords pCoords = imgVAN.getPieceCoords();
                String[] pInfo = imgVAN.getName().split("_");
                String pName = pInfo[0];
                Player.Color pColor = pInfo[1].equals("w") ? Player.Color.WHITE : Player.Color.BLACK;

                if ("pawn".equals(pName))
                    coordsToPiece.put(pCoords, new Pawn(pColor, imgVAN.getImageView(), pCoords));
                else if ("rook".equals(pName))
                    coordsToPiece.put(pCoords, new Rook(pColor, imgVAN.getImageView(), pCoords));
                else if ("knight".equals(pName))
                    coordsToPiece.put(pCoords, new Knight(pColor, imgVAN.getImageView(), pCoords));
                else if ("bishop".equals(pName))
                    coordsToPiece.put(pCoords, new Bishop(pColor, imgVAN.getImageView(), pCoords));
                else if ("queen".equals(pName))
                    coordsToPiece.put(pCoords, new Queen(pColor, imgVAN.getImageView(), pCoords));
                else if ("king".equals(pName))
                    coordsToPiece.put(pCoords, new King(pColor, imgVAN.getImageView(), pCoords));
                else throw new RuntimeException("Unknown piece name");
            });


            chessBoardController.getCellPanesAndCoords().forEach(paneAndCoords -> {
                Cell newCell = new Cell(paneAndCoords.getCellPane(), this);
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

    public void showChessBoard() {
        primaryStage.setScene(new Scene(chessBoardRootNode));
        primaryStage.setX(getCenterXForChessBoard());
        primaryStage.setY(getCenterYForChessBoard());
        primaryStage.show();
    }

    private double getCenterXForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getWidth() / 2 - primaryStage.getWidth() / 2;
    }

    private double getCenterYForChessBoard() {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return screenBounds.getHeight() / 2 - primaryStage.getHeight() / 2;
    }

    public Player.Color getPlayerColor() {
        return playerColor;
    }

    public boolean isMovePossible(Piece piece, CellModel.Coords from, CellModel.Coords to) {
        List<CellModel.Coords> moves = piece.getMoves(from);
        if (moves.contains(to)) {
            // todo: check if the move is correct by chess rules
            return true;
        }
        return false;
    }

    public void makeMove(CellModel.Coords from, CellModel.Coords to) {
        Cell fromCell = coordsToCell.get(from).getLeft();
        Cell toCell = coordsToCell.get(to).getLeft();

        Piece movingPiece = fromCell.cleanPiece();
        toCell.setPiece(movingPiece);
    }

    public ChessBoardState getBoardState() {
        return boardState;
    }
}
