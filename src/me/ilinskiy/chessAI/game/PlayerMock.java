package me.ilinskiy.chessAI.game;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.chessBoard.Coordinates;
import me.ilinskiy.chessAI.chessBoard.ImmutableBoard;
import me.ilinskiy.chessAI.chessBoard.PieceColor;
import me.ilinskiy.chessAI.chessBoard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/19/15
 */
public class PlayerMock implements Player {
    public final PieceColor myColor;


    public PlayerMock() {
        this(PieceColor.Empty);
    }

    public PlayerMock(PieceColor color) {
        myColor = color;
    }

    @Override
    @NotNull
    public Move makeMove(@NotNull ImmutableBoard b) {
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

    @Override
    @NotNull
    public PlayerMock copy() {
        return new PlayerMock();
    }
}
