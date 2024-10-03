package me.ilinskiy.chess.api.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @NotNull
    Move getMove(@NotNull Board b);

    @NotNull
    PieceColor getPlayerColor();

    @NotNull
    PieceType getPieceTypeForPromotedPawn();

    default Player inverse(Player p1, Player p2) {
        return this == p1 ? p2 : p1;
    }
}
