package me.ilinskiy.chess.api.ui;

import me.ilinskiy.chess.api.Move;
import org.jetbrains.annotations.NotNull;
import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @org.jetbrains.annotations.NotNull
    Move getMove(@org.jetbrains.annotations.NotNull Board b);

    @org.jetbrains.annotations.NotNull
    PieceColor getPlayerColor();

    @NotNull
    PieceType getPieceTypeForPromotedPawn();

    default Player inverse(Player p1, Player p2) {
        return this == p1 ? p2 : p1;
    }
}
