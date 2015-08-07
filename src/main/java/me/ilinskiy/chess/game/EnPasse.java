package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/7/15.
 */
public class EnPasse extends Move {
    public EnPasse(@NotNull Coordinates init, @NotNull Coordinates newC) {
        super(init, newC);
    }

    @NotNull
    public Coordinates eatenPiece() {
        return new Coordinates(getNewPosition().getX(), getInitialPosition().getY());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EnPasse && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 7;
    }
}
