package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.ImmutableBoard;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.chessBoard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player extends Copyable {

    @NotNull
    Move getMove(@NotNull ImmutableBoard b);

    @NotNull
    PieceColor getPlayerColor();

    @NotNull
    PieceType getPieceTypeForPromotedPawn();

    @NotNull
    @Override
    Player copy();
}
