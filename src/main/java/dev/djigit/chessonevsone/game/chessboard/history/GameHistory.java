package dev.djigit.chessonevsone.game.chessboard.history;

import java.util.Optional;

public class GameHistory {
    private SnapshotLinkedList history;
    private SnapshotLinkedList curStateIt;

    public GameHistory(ChessBoardSnapshot snapshot) {
        this.history = new SnapshotLinkedList(snapshot);
        this.curStateIt = history;
    }

    public void addMove(ChessBoardSnapshot state) {
        curStateIt = history = history.addToLast(state);
    }

    public Optional<ChessBoardSnapshot> getNextMove() {
        if (curStateIt.hasNext()) {
            curStateIt = curStateIt.getNext();
            return Optional.of(curStateIt.value());
        }
        return Optional.empty();
    }

    public Optional<ChessBoardSnapshot> getPrevMove() {
        if (curStateIt.hasPrevious()) {
            curStateIt = curStateIt.getPrev();
            return Optional.of(curStateIt.value());
        }
        return Optional.empty();
    }
}
