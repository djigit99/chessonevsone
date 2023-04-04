package dev.djigit.chessonevsone.game.chessboard.history;

public class SnapshotLinkedList {
    private ChessBoardSnapshot snapshot;
    private SnapshotLinkedList next;
    private SnapshotLinkedList prev;
    private SnapshotLinkedList start;

    public SnapshotLinkedList(ChessBoardSnapshot snapshot) {
        this.snapshot = snapshot;
        next = prev = start = null;
    }

    public ChessBoardSnapshot value() {
        return snapshot;
    }

    public SnapshotLinkedList getNext() {
        return next;
    }

    public SnapshotLinkedList getPrev() {
        return prev;
    }

    public SnapshotLinkedList getStart() {
        return start;
    }

    public SnapshotLinkedList addToLast(ChessBoardSnapshot snapshot) throws IllegalStateException {
        if(next != null) {
            throw new IllegalStateException("Adding new snapshot inside the list is not permitted");
        }

        SnapshotLinkedList tail = new SnapshotLinkedList(snapshot);
        next = tail;
        tail.prev = this;
        tail.start = start;
        return tail;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return prev != null;
    }
}
