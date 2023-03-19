package dev.djigit.chessonevsone.chessboard;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class Piece {

    private Cell cell;

    public Piece(Cell cell) {
        this.cell = cell;
    }

    public abstract void move(Cell.Coords cord2);

    public Cell.Coords getCords() {
        throw new NotImplementedException();
    }
}
