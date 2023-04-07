package dev.djigit.chessonevsone.game.chessboard.history;

import dev.djigit.chessonevsone.game.chessboard.piece.Piece;

import java.util.Optional;

public class GameHistory {
    private SnapshotLinkedList history;
    private SnapshotLinkedList curStateIt;

    public GameHistory(ChessBoardSnapshot snapshot) {
        this.history = new SnapshotLinkedList(snapshot, null);
        this.curStateIt = history;
    }

    public void addMove(ChessBoardSnapshot state, Piece lastMovePiece) {
        curStateIt = history = history.addToLast(state, lastMovePiece);
    }

    public Optional<ChessBoardSnapshot> getNextMove() {
        if (curStateIt.hasNext()) {
            curStateIt = curStateIt.getNext();
            return Optional.of(curStateIt.snapshot());
        }
        return Optional.empty();
    }

    public Optional<ChessBoardSnapshot> getPrevMove() {
        if (curStateIt.hasPrevious()) {
            curStateIt = curStateIt.getPrev();
            return Optional.of(curStateIt.snapshot());
        }
        return Optional.empty();
    }

    public ChessBoardSnapshot changeToLatest() {
        curStateIt = history;
        return history.snapshot();
    }

    public boolean isLastMove() {
        return history == curStateIt;
    }

    public Piece getLastMovePiece() {
        return history.lastMovePiece();
    }
}
