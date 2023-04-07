package dev.djigit.chessonevsone.game.chessboard.history;

import dev.djigit.chessonevsone.game.chessboard.piece.Piece;

public class SnapshotLinkedList {
    private final ChessBoardSnapshot snapshot;
    private final Piece lastMovePiece;
    private SnapshotLinkedList next;
    private SnapshotLinkedList prev;

    public SnapshotLinkedList(ChessBoardSnapshot snapshot, Piece lastMovePiece) {
        this.snapshot = snapshot;
        this.lastMovePiece = lastMovePiece;
        next = prev  = null;
    }

    public ChessBoardSnapshot snapshot() {
        return snapshot;
    }
    public Piece lastMovePiece() {
        return lastMovePiece;
    }

    public SnapshotLinkedList getNext() {
        return next;
    }

    public SnapshotLinkedList getPrev() {
        return prev;
    }

    public SnapshotLinkedList addToLast(ChessBoardSnapshot snapshot, Piece lastMovePiece) throws IllegalStateException {
        if(next != null) {
            throw new IllegalStateException("Adding new snapshot inside the list is not permitted");
        }

        SnapshotLinkedList tail = new SnapshotLinkedList(snapshot, lastMovePiece);
        next = tail;
        tail.prev = this;
        return tail;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return prev != null;
    }
}
