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
import javafx.scene.layout.BorderPane;
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

        if (GameLogic.isTheLastRawForPawn(to, movingPiece)) {
            return true;
        }

        writeToHistory(movingPiece);
        return false;
    }

    public void doPostMakeMove(Piece piece, CellModel.Coords coords) {

        Cell pieceToInsertCell = coordsToCell.get(coords).getLeft();

        pieceToInsertCell.setPiece(piece);
        coordsToActualPiece.put(coords, piece);

        writeToHistory(piece);
    }

    public ChoosePiecePopup showChoosePiecePopup(Cell toCell) {
        ChoosePiecePopup choosePiecePopup = new ChoosePiecePopup(chessBoardListener).buildPopup(playerColor, toCell);
        choosePiecePopup.showPopup();
        return choosePiecePopup;
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

    private void writeToHistory(Piece lastMovePiece) {
        ChessBoardSnapshot snapshot = createSnapshot();
        gameHistory.addMove(snapshot, lastMovePiece);
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

    public void restoreActualPosition() {
        ChessBoardSnapshot lastSnapshot = gameHistory.getActualPositionSnapshot();
        Map<CellModel.Coords, Piece> coordsToPiece = lastSnapshot.getState();

        coordsToActualPiece.clear();
        coordsToActualPiece.putAll(coordsToPiece);
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

        /**
         * Marks a state to be 'candidate' when it comes to decide to which
         * state to go when leaving the history mode.
         *
         * @return true - if the state is the one to change the state for after
         * leaving history mode.
         */

        protected abstract boolean isStateReturnedAfterHistory();

        public abstract void doOnUpdateFromCell(CellModel.Coords coords);

        public abstract void doOnUpdateFromPlayer();

        public abstract void doOnUpdateFromChoosePiecePopup(Piece piece);

        public abstract void setCoordsToCellListeners(Map<CellModel.Coords, ImmutablePair<Cell, CellListener>> coordsToCellListeners);

        /**
         * Do something when the state is going to be changed to another.
         */
        public abstract void beforeStateChanged();

        /**
         * Do something when the state is going to be change to history state.
         */
        public abstract void beforeStateChangedToHistory();

        /**
         * If we leave history mode we need to decide for which state to go.
         * This method helps us to decide looking for the last 'candidate'
         * to make decision for and change the state based on the search.
         */
        public void changeToLastPossibleStateBeforeHistory() {
            ChessBoardState possibleState = prevState;

            if (possibleState == null)
                return;

            while (!possibleState.isStateReturnedAfterHistory()) {
                possibleState = possibleState.prevState;
            }

            if (possibleState.isStateReturnedAfterHistory()) {
                getBoard().changeState(possibleState);
            }
        }

        public void changeState(ChessBoardState newState) {
            getBoard().getBoardState().beforeStateChanged();

            newState.prevState = this;

            getBoard().changeState(newState);
            getBoard().getBoardState().setCoordsToCellListeners(
                    getBoard().getChessBoardListener().getCoordsToCellListeners());
        }
    }
}
