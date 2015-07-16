package me.ilinskiy.ChessBoard;

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

    public PieceColor getColor() {
        return color;
    }

    public String toString() {
        return "Chess piece: " + getColor() + " " + getType();
    }
}
