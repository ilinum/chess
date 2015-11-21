package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Board;
import me.ilinskiy.chess.chessBoard.Coordinates;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.chessBoard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/19/15
 */
public class PlayerMock implements Player {
    @SuppressWarnings("WeakerAccess")
    public final PieceColor myColor;

    public PlayerMock(PieceColor color) {
        myColor = color;
    }

    @Override
    @NotNull
    public Move getMove(@NotNull Board b) {
        Coordinates c = new Coordinates(0, 0);
        return new Move(c, c);
    }

    @Override
    @NotNull
    public PieceColor getPlayerColor() {
        return myColor;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return PieceType.Empty;
    }
}
