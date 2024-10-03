package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import org.jetbrains.annotations.NotNull;

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
        Coordinates c = new CoordinatesImpl(0, 0);
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
