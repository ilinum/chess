package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import org.jetbrains.annotations.NotNull;

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
