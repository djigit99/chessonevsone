package dev.djigit.chessonevsone.game;

import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;

public class PlayerListener {
    private Player player;

    public PlayerListener(Player player) {
        this.player = player;
    }

    public void onMakeMove(CellModel.Coords from, CellModel.Coords to) {
        player.sendMove(from, to);
    }
}
