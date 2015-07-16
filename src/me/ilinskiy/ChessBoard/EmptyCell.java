package me.ilinskiy.ChessBoard;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class EmptyCell implements ChessElement {

    public static final EmptyCell INSTANCE = new EmptyCell();

    @Override
    public PieceType getType() {
        return PieceType.Empty;
    }

    @Override
    public PieceColor getColor() {
        return PieceColor.Empty;
    }
}
