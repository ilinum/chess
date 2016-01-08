package me.ilinskiy.chess.game.moves;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 06.12.15.
 */

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public final class RegularMove extends Move {
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

