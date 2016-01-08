package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessboard.Board;
import me.ilinskiy.chess.chessboard.PieceColor;
import me.ilinskiy.chess.chessboard.PieceType;
import me.ilinskiy.chess.game.moves.Move;

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
