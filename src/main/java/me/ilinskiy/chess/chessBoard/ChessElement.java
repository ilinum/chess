package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public interface ChessElement {

    @NotNull
    PieceType getType();

    @NotNull
    PieceColor getColor();
}
