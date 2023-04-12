package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.factories.FXMLLoaderFactory;
import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.PlayerListener;
import dev.djigit.chessonevsone.game.chessboard.cell.Cell;
import dev.djigit.chessonevsone.game.chessboard.cell.CellListener;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.history.ChessBoardSnapshot;
import dev.djigit.chessonevsone.game.chessboard.history.GameHistory;
import dev.djigit.chessonevsone.game.chessboard.piece.*;
import dev.djigit.chessonevsone.game.chessboard.popup.ChoosePiecePopup;
import dev.djigit.chessonevsone.game.chessboard.state.WaitForOpponentMoveState;
import dev.djigit.chessonevsone.game.chessboard.state.WaitForSelectedPieceState;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessBoard {
    private final Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCell = new HashMap<>();
    private Map<CellModel.Coords, Piece> coordsToActualPiece;
    private final URL url;
    private ChessBoardListener chessBoardListener;
    private BorderPane chessBoardRootPane;
    private final Player.Color playerColor;
    private final PlayerListener playerListener;
    private ChessBoardState boardState;
    private final GameLogic gameLogic;
    private GameHistory gameHistory;

    public ChessBoard(URL url, Player player) {
        this.url = url;
        this.playerColor = player.getColor();
        if (playerColor.isWhite()) {
            this.boardState = new WaitForSelectedPieceState(this);
        } else {
            this.boardState = new WaitForOpponentMoveState(this);
        }
        this.gameLogic = new GameLogic(this);
        this.playerListener = player.getListener();

        initData();
    }

    private void initData() {

        try {
            coordsToActualPiece = new HashMap<>();
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
                    coordsToActualPiece.put(pCoords, new Pawn(pColor, imgVAN.getImageView()));
                else if ("rook".equals(pName))
                    coordsToActualPiece.put(pCoords, new Rook(pColor, imgVAN.getImageView()));
                else if ("knight".equals(pName))
                    coordsToActualPiece.put(pCoords, new Knight(pColor, imgVAN.getImageView()));
                else if ("bishop".equals(pName))
                    coordsToActualPiece.put(pCoords, new Bishop(pColor, imgVAN.getImageView()));
                else if ("queen".equals(pName))
                    coordsToActualPiece.put(pCoords, new Queen(pColor, imgVAN.getImageView()));
                else if ("king".equals(pName))
                    coordsToActualPiece.put(pCoords, new King(pColor, imgVAN.getImageView()));
                else throw new RuntimeException("Unknown piece name");
            });


            chessBoardController.getCellPanesAndCoords().forEach(paneAndCoords -> {
                Cell newCell = new Cell(paneAndCoords.getCellPane());
                CellListener cellListener = newCell.getCellListener();
                Piece pieceToSet = coordsToActualPiece.getOrDefault(paneAndCoords.getCoords(), null);

                newCell.setPiece(pieceToSet);
                coordsToCell.put(paneAndCoords.getCoords(), ImmutablePair.of(newCell, cellListener));
            });

            this.chessBoardListener = new ChessBoardListener(coordsToCell, this, gameLogic);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void postInitData() {

        // set chessboard listener for all cell listeners
        coordsToCell.values().forEach(
                pair -> pair.getLeft().getCellListener().setChessBoardListener(chessBoardListener));

        // set history for all pawns to detect en passant in the game
        coordsToActualPiece.values().forEach(p -> {
            if (p instanceof Pawn)
                ((Pawn) p).setGameHistory(gameHistory);
        });
    }

    public Player.Color getPlayerColor() {
        return playerColor;
    }

    public boolean isMovePossible(Piece piece, CellModel.Coords from, CellModel.Coords to) {
        return gameLogic.isMovePossible(piece, from, to);
    }

    public boolean makeMove(CellModel.Coords from, CellModel.Coords to) {
        final int WHITE_LAST_ROW = 8;
        final int BLACK_LAST_ROW = 1;
        Cell fromCell = coordsToCell.get(from).getLeft();
        Cell toCell = coordsToCell.get(to).getLeft();

        Piece movingPiece = fromCell.cleanPiece();
        coordsToActualPiece.put(from, null);
        toCell.setPiece(movingPiece);
        coordsToActualPiece.put(to, movingPiece);
        movingPiece.setLastMove(new Piece.LastMove(from, to));

        // moves needed to clean opponent's piece after
        if (movingPiece instanceof Pawn && ((Pawn) movingPiece).isLastMoveEnPassant()) {
            Cell cellToClean = getOpponentsPawnToDelete(movingPiece);
            cellToClean.cleanPiece();
            coordsToActualPiece.put(cellToClean.getCellViewModel().getModel().getCoords(), null);
        }

        if (movingPiece instanceof King && King.isCastling(from, to)) {
            Cell rookCell = getOwnRookToReplaceWhenCastling(from, to);
            Rook movingRook = (Rook) rookCell.cleanPiece();
            coordsToActualPiece.put(rookCell.getCellViewModel().getModel().getCoords(), null);

            Cell rooksNewCell = coordsToCell
                    .get(from.getByCoords(to.getX() > from.getX() ? (short) 1 : (short) -1, (short) 0))
                    .getLeft();
            rooksNewCell.setPiece(movingRook);
            coordsToActualPiece.put(rooksNewCell.getCellViewModel().getModel().getCoords(), movingRook);
        }

        if (movingPiece instanceof Pawn && (to.getY() == BLACK_LAST_ROW || to.getY() == WHITE_LAST_ROW)) {

            if (playerColor.equals(movingPiece.getPieceColor())) {
                new ChoosePiecePopup(chessBoardListener).buildPopup(playerColor, toCell).showPopup();
            }

            return true;
        }

        ChessBoardSnapshot snapshot = createSnapshot();
        gameHistory.addMove(snapshot, movingPiece);
        return false;
    }

    public void doPostMakeMove(Piece piece, CellModel.Coords coords) {

        Cell pieceToInsertCell = coordsToCell.get(coords).getLeft();

        Piece pieceToClean = pieceToInsertCell.cleanPiece();

        pieceToInsertCell.setPiece(piece);
        coordsToActualPiece.put(coords, piece);

        ChessBoardSnapshot snapshot = createSnapshot();
        gameHistory.addMove(snapshot, pieceToClean);
    }

    private Cell getOpponentsPawnToDelete(Piece piece) {
        if (piece.getPieceColor().isWhite()) {
            return coordsToCell.get(piece.getLastMove().get().getTo().getByCoords((short) 0, (short) -1)).getLeft();
        } else {
            return coordsToCell.get(piece.getLastMove().get().getTo().getByCoords((short) 0, (short) 1)).getLeft();
        }
    }

    private Cell getOwnRookToReplaceWhenCastling(CellModel.Coords from, CellModel.Coords to) {
        if (to.getX() > from.getX()) { // short castling
            return coordsToCell.get(to.getByCoords((short) 1, (short) 0)).getLeft();
        } else {
            return coordsToCell.get(to.getByCoords((short) -2, (short) 0)).getLeft();
        }
    }

    private void showChoosePiecePopup(Player.Color pieceColor, Cell toCell) {
        final String CHOOSE_PIECE_WHITE_URL = "/scenes/ChoosePieceWhite.fxml";
        final String CHOOSE_PIECE_BLACK_URL = "/scenes/ChoosePieceBlack.fxml";
        URL choosePieceUrl;

        if (pieceColor.isWhite()) {
            choosePieceUrl = getClass().getResource(CHOOSE_PIECE_WHITE_URL);
        } else {
            choosePieceUrl = getClass().getResource(CHOOSE_PIECE_BLACK_URL);
        }

        Parent choosePieceParent = FXMLLoaderFactory.getRootNode(choosePieceUrl);

        Popup chooseColorPopup = new Popup();
        chooseColorPopup.getContent().add(choosePieceParent);

        Pane cellPane = toCell.getPane();
        Bounds boundsInLocal = cellPane.getBoundsInLocal();
        Bounds bounds = cellPane.localToScreen(boundsInLocal);
        chooseColorPopup.setX(bounds.getMinX() + cellPane.getWidth());
        chooseColorPopup.setY(bounds.getMinY());

        chooseColorPopup.show(cellPane.getScene().getWindow());
        chooseColorPopup.setX(Double.NaN); // not to update X-cord when the style of ChoosePiece popup's elements changed
        chooseColorPopup.setY(Double.NaN); // not to update Y-cord when the style of ChoosePiece popup's elements changed
    }

    public ChessBoardState getBoardState() {
        return boardState;
    }

    private void changeState(ChessBoardState state) {
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
        Map<CellModel.Coords, Piece> coordsPieceMap = new HashMap<>(coordsToActualPiece);
        return new ChessBoardSnapshot(coordsPieceMap);
    }

    public void restoreSnapshot(ChessBoardSnapshot snapshot) {
        Map<CellModel.Coords, Piece> state = snapshot.getState();
        state.forEach((coords, piece) -> coordsToCell.get(coords).getLeft().setPiece(piece));
    }

    public ChessBoardListener getChessBoardListener() {
        return chessBoardListener;
    }

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public static abstract class ChessBoardState {
        private final ChessBoard board;
        private ChessBoardState prevState;

        public ChessBoardState(ChessBoard board) {
            this.board = board;
        }
        protected ChessBoard getBoard() {
            return board;
        }

        public abstract void doOnUpdate(CellModel.Coords coords);
        public abstract void doOnUpdateFromPlayer();
        public abstract void doOnUpdateFromChoosePiecePopup(Piece piece);

        public abstract void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners);
        public void changeToPreviousState() {
            if (prevState != null) {
                getBoard().changeState(prevState);
            }
        }
        public void changeState(ChessBoardState newState) {
            newState.prevState = this;
            getBoard().changeState(newState);
        }
    }
}
