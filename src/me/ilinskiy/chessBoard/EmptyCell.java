package me.ilinskiy.chessBoard;

import com.sun.istack.internal.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class EmptyCell implements ChessElement {

    public static final EmptyCell INSTANCE = new EmptyCell();

    @Override
    @NotNull
    public PieceType getType() {
        return PieceType.Empty;
    }

    @Override
    @NotNull
    public PieceColor getColor() {
        return PieceColor.Empty;
    }
}
