package me.ilinskiy.game;

import me.ilinskiy.chessBoard.ImmutableBoard;
import me.ilinskiy.chessBoard.PieceColor;
import org.jetbrains.annotations.NotNull;

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
