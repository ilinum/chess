package me.ilinskiy.game;

import com.sun.istack.internal.NotNull;
import me.ilinskiy.chessBoard.ImmutableBoard;
import me.ilinskiy.chessBoard.PieceColor;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @NotNull
    Move makeMove(@NotNull ImmutableBoard b);

    @NotNull
    PieceColor getPlayerColor();
}
