package me.ilinskiy.chess.chessBoard;


import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Copyable;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public class Coordinates implements Copyable, Comparable<Coordinates> {
    private final int myX;
    private final int myY;

    public Coordinates(int x, int y) {
        this.myX = x;
        this.myY = y;
    }

    public int getX() {
        return myX;
    }

    public int getY() {
        return myY;
    }

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
        return new Coordinates(myX, myY);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinates) {
            return getX() == ((Coordinates) o).getX() && getY() == ((Coordinates) o).getY();
        } else {
            return false;
        }
    }

    /**
     * @return a new instance of coordinates with x and y switched
     */
    @NotNull
    public Coordinates inverse() {
        //noinspection SuspiciousNameCombination
        return new Coordinates(myY, myX);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public int compareTo(@NotNull Coordinates coordinates) {
        int xDiff = myX - coordinates.myX;
        if (xDiff != 0) {
            return xDiff;
        } else {
            return myY - coordinates.myY;
        }
    }
}
