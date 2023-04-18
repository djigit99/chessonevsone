package dev.djigit.chessonevsone.game.chessboard;

import dev.djigit.chessonevsone.game.Player;
import dev.djigit.chessonevsone.game.chessboard.cell.CellModel;
import dev.djigit.chessonevsone.game.chessboard.piece.King;
import dev.djigit.chessonevsone.game.chessboard.piece.Piece;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessBoardModel implements Cloneable {
    private Map<CellModel.Coords, Piece> coordsToPiece;
    private Player.Color playerColor;

    public ChessBoardModel(Map<CellModel.Coords, Piece> coordsToPiece, Player.Color playerColor) {
        this.coordsToPiece = coordsToPiece;
        this.playerColor = playerColor;
    }

    public void setPiece(CellModel.Coords pCoords, Piece p) {
        coordsToPiece.put(pCoords, p);
    }

    public void setPieces(Map<CellModel.Coords, Piece> pieces) {
        coordsToPiece.putAll(pieces);
    }

    public Piece cleanPiece(CellModel.Coords pFrom) {
        Piece pieceToDelete = coordsToPiece.get(pFrom);
        setPiece(pFrom, null);
        return pieceToDelete;
    }

    public void transferPiece(CellModel.Coords pFrom, CellModel.Coords pTo) {
        Piece pieceToMove = cleanPiece(pFrom);
        coordsToPiece.put(pTo, pieceToMove);
    }

    public Player.Color getPlayerColor() {
        return playerColor;
    }

    public Piece getPiece(CellModel.Coords pCoords) {
        return coordsToPiece.get(pCoords);
    }

    public Collection<Piece> getPieces() {
        return coordsToPiece.values();
    }

    public HashMap<CellModel.Coords, Piece> getModelMap() {
        return new HashMap<>(coordsToPiece);
    }

    public void cleanChessboard() {
        coordsToPiece.clear();
    }

    public void setPlayerColor(Player.Color color) {
        this.playerColor = color;
    }

    public LinkedMap<CellModel.Coords, Piece> getPiecesByCoords(CellModel.Coords[] coords) {
        LinkedMap<CellModel.Coords, Piece> piecesOnPath = new LinkedMap<>();
        for (CellModel.Coords coord : coords) {
            piecesOnPath.put(coord, coordsToPiece.get(coord));
        }
        return piecesOnPath;
    }

    @Override
    public ChessBoardModel clone() {
        try {
            ChessBoardModel clone = (ChessBoardModel) super.clone();
            clone.coordsToPiece = new HashMap<>();
            clone.setPieces(this.getModelMap());
            clone.setPlayerColor(this.getPlayerColor());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public ImmutablePair<CellModel.Coords, King> getKing(Player.Color pieceColor) {
        Map.Entry<CellModel.Coords, Piece> coordsPieceEntry = coordsToPiece.entrySet().stream()
                .filter(cp -> cp.getValue() != null && cp.getValue() instanceof King && cp.getValue().getPieceColor().equals(pieceColor))
                .collect(Collectors.toList()).get(0);
        return ImmutablePair.of(coordsPieceEntry.getKey(), (King) coordsPieceEntry.getValue());
    }
    
    public Map<CellModel.Coords, Piece> getOpponentsPieces(Player.Color myColor) {
        List<Map.Entry<CellModel.Coords, Piece>> opPiecesList = coordsToPiece.entrySet().stream()
                .filter(cp -> cp.getValue() != null && !cp.getValue().getPieceColor().equals(myColor))
                .collect(Collectors.toList());
        return opPiecesList.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
