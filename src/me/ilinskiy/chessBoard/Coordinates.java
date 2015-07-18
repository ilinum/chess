package me.ilinskiy.chessBoard;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public class Coordinates {
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
    public String toString() {
        return "(" + myX + ", " + myY + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinates) {
            return getX() == ((Coordinates) o).getX() && getY() == ((Coordinates) o).getY();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }
}
