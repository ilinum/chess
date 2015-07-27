package me.ilinskiy.chessAI.game;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.chessBoard.ImmutableBoard;
import me.ilinskiy.chessAI.chessBoard.PieceColor;
import me.ilinskiy.chessAI.chessBoard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @NotNull
    Move makeMove(@NotNull ImmutableBoard b);

    @NotNull
    PieceColor getPlayerColor();

    @NotNull
    PieceType getPieceTypeForPromotedPawn();
}
