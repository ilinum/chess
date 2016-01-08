package me.ilinskiy.chess.game.moves;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;
import me.ilinskiy.chess.game.Copyable;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/7/15.
 */
public final class EnPasse extends Move {
    @NotNull
    public final Coordinates initialPosition;
    @NotNull
    public final Coordinates newPosition;

    public EnPasse(@NotNull Coordinates init, @NotNull Coordinates newC) {
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
        return new Coordinates(getNewPositions()[0].getX(), getInitialPositions()[0].getY());
    }

    @Override
    public Copyable copy() {
        return null;
    }
}
