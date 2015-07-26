package me.ilinskiy.game;

import me.ilinskiy.chessBoard.Coordinates;
import me.ilinskiy.chessBoard.ImmutableBoard;
import me.ilinskiy.chessBoard.PieceColor;
import me.ilinskiy.chessBoard.PieceType;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/19/15
 */
public class PlayerMock implements Player {
    @Override
    @NotNull
    public Move makeMove(@NotNull ImmutableBoard b) {
        Coordinates c = new Coordinates(0, 0);
        return new Move(c, c);
    }

    @Override
    @NotNull
    public PieceColor getPlayerColor() {
        return PieceColor.Empty;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return PieceType.Empty;
    }
}
