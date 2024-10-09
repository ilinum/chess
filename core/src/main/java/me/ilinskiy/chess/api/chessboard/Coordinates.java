package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Coordinates implements Copyable, Comparable<Coordinates> {
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

    public boolean isOutOfBounds() {
        return this.getX() < 0 || this.getX() >= Board.BOARD_SIZE || this.getY() < 0 || this.getY() >= Board.BOARD_SIZE;
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
        return o instanceof Coordinates && getX() == ((Coordinates) o).getX() && getY() == ((Coordinates) o).getY();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new int[]{myX, myY});
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

    public String toUCINotation() {
        char col = (char) ('a' + getX());
        int row = Board.BOARD_SIZE - getY();
        return "%c%d".formatted(col, row);
    }


    public static Coordinates fromUCINotation(String uci) {
        if (uci.length() != 2) {
            throw new IllegalArgumentException("unexpected UCI coordinates length: " + uci);
        }
        int x = uci.charAt(0) - 'a';
        int y = Board.BOARD_SIZE - Integer.parseInt("" + uci.charAt(1));
        return new Coordinates(x, y);
    }
}
