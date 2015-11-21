package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class EmptyCell implements ChessElement {

    public static final EmptyCell INSTANCE = new EmptyCell();

    private EmptyCell() {
    }

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

    @Override
    public String toString() {
        return "Empty cell";
    }
}
