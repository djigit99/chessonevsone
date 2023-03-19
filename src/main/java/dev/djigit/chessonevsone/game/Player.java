package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.Board;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Player {

    private Board board;
    public enum Color {
        WHITE(true),
        BLACK(false);

        private boolean isWhite;
        Color(boolean isWhite) {
            this.isWhite = isWhite;
        }

        public boolean isWhite() {
            return isWhite;
        }
    }

    private Color color;

    public void makeMove() {
        throw new NotImplementedException();
    }
}
