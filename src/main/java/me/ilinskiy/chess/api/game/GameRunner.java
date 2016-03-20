package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.ui.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface GameRunner {
    @NotNull
    PieceColor runGame(@NotNull Player p1, @NotNull Player p2);

    boolean askToPlayAgain();

    void askTimeOut();

    void dispose();
}
