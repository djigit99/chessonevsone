package dev.djigit.chessonevsone.game;

public abstract class Player {
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
}
