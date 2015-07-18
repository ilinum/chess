package me.ilinskiy.chessBoard;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class Piece implements ChessElement {
    public final PieceColor color;
    public final PieceType type;

    public Piece(PieceColor pColor, PieceType pType) {
        color = pColor;
        type = pType;
    }

    @Override
    public PieceType getType() {
        return type;
    }

    @Override
    public PieceColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Chess piece: " + getColor() + " " + getType();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Piece) {
            return getColor() == ((Piece) o).getColor() && getType() == ((Piece) o).getType();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int colorMultiplier;
        switch (color) {
            case White:
                colorMultiplier = 1;
                break;
            case Black:
                colorMultiplier = -1;
                break;
            case Empty:
                colorMultiplier = 0;
                break;
            default:
                throw new RuntimeException("Unknown color!");
        }
        int pieceTypeMultiplier;
        switch (type) {
            case Pawn:
                pieceTypeMultiplier = 1;
                break;
            case Knight:
                pieceTypeMultiplier = 2;
                break;
            case Bishop:
                pieceTypeMultiplier = 3;
                break;
            case Rook:
                pieceTypeMultiplier = 4;
                break;
            case Queen:
                pieceTypeMultiplier = 5;
                break;
            case King:
                pieceTypeMultiplier = 6;
                break;
            case Empty:
                pieceTypeMultiplier = 7;
                break;
            default:
                throw new RuntimeException("Unknown type!");
        }
        return colorMultiplier * pieceTypeMultiplier;
    }
}
