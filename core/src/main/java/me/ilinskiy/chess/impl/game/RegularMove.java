package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public final class RegularMove extends MoveAdapter {
    @NotNull
    public final Coordinates initialPosition;
    @NotNull
    public final Coordinates newPosition;

    public RegularMove(@NotNull Coordinates init, @NotNull Coordinates newC) {
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

    @Override
    @NotNull
    public Move copy() {
        return new RegularMove(initialPosition.copy(), newPosition.copy());
    }
}

