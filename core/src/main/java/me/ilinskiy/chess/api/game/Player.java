package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @NotNull
    Move getMove(@NotNull MoveAwareBoard b);

    @NotNull
    PieceColor getColor();

    default Player inverse(Player p1, Player p2) {
        return this == p1 ? p2 : p1;
    }
}
