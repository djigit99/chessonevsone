package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;

public class PlayerListener {
    private final Player player;

    public PlayerListener(Player player) {
        this.player = player;
    }

    public void onMakeMove(CellModel.Coords from, CellModel.Coords to) {
        player.sendMove(from, to);
    }

    public void onMakeMove(CellModel.Coords from, CellModel.Coords to, Piece piece) {
        player.sendMove(from, to, piece);
    }
}
