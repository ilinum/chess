package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.annotations.NotNull;

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
