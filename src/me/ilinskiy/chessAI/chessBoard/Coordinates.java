package me.ilinskiy.chessAI.chessBoard;


import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.game.Copyable;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public class Coordinates implements Copyable {
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
}
