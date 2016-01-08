package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessboard.Board;
import me.ilinskiy.chess.chessboard.Coordinates;
import me.ilinskiy.chess.chessboard.PieceColor;
import me.ilinskiy.chess.chessboard.PieceType;
import me.ilinskiy.chess.game.moves.Move;
import me.ilinskiy.chess.game.moves.RegularMove;

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
        return new RegularMove(c, c);
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
