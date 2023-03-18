package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.chessboard.Board;

public class Player {

    private Board board;
    public enum Color {
        WHITE {
            @Override
            boolean isWhite() {
                return true;
            }
        },
        BLACK;

        boolean isWhite() { return false;}
    }

    private Color color;

    public void makeMove() {


    }
}
