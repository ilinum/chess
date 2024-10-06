package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.game.Copyable;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/7/15.
 */
public final class EnPassant extends MoveAdapter {
    @NotNull
    public final Coordinates initialPosition;
    @NotNull
    public final Coordinates newPosition;

    public EnPassant(@NotNull Coordinates init, @NotNull Coordinates newC) {
        initialPosition = init;
        newPosition = newC;
    }

    @Override
    public Coordinates[] getInitialPositions() {
        return new Coordinates[]{initialPosition};
    }

    @Override
    public Coordinates[] getNewPositions() {
        return new Coordinates[]{newPosition};
    }

    @NotNull
    public Coordinates eatenPiece() {
        return new CoordinatesImpl(getNewPositions()[0].getX(), getInitialPositions()[0].getY());
    }

    @Override
    public Copyable copy() {
        return null;
    }
}
