package me.ilinskiy.chess.impl.chessboard;


import me.ilinskiy.chess.api.annotations.NotNull;
import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public final class CoordinatesImpl implements Coordinates {
    private final int myX;
    private final int myY;

    public CoordinatesImpl(int x, int y) {
        this.myX = x;
        this.myY = y;
    }

    @Override
    public int getX() {
        return myX;
    }

    @Override
    public int getY() {
        return myY;
    }

    @Override
    @NotNull
    public int[] toArray() {
        return new int[]{myX, myY};
    }

    @Override
    @NotNull
    public String toString() {
        return "(" + myX + ", " + myY + ")";
    }

    /**
     * @return a deep copy of this coordinates
     */
    @NotNull
    @Override
    public Coordinates copy() {
        return new CoordinatesImpl(myX, myY);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Coordinates && getX() == ((Coordinates) o).getX() && getY() == ((Coordinates) o).getY();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public int compareTo(@NotNull Coordinates coordinates) {
        int xDiff = myX - coordinates.getX();
        if (xDiff != 0) {
            return xDiff;
        } else {
            return myY - coordinates.getY();
        }
    }

    @Override
    public boolean isOutOfBounds() {
        return this.getX() < 0 || this.getX() >= Board.BOARD_SIZE || this.getY() < 0 || this.getY() >= Board.BOARD_SIZE;
    }
}
