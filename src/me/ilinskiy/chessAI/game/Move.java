package me.ilinskiy.chessAI.game;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.chessBoard.Coordinates;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public class Move implements Copyable, Comparable<Move> {
    @NotNull
    private final Coordinates initialPosition;
    @NotNull
    private final Coordinates newPosition;

    public Move(@NotNull Coordinates init, @NotNull Coordinates newC) {
        initialPosition = init;
        newPosition = newC;
    }

    @NotNull
    public Coordinates getInitialPosition() {
        return initialPosition;
    }

    @NotNull
    public Coordinates getNewPosition() {
        return newPosition;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("Move: ");
        res.append(initialPosition);
        res.append(" -> ");
        res.append(newPosition);
        return res.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Move) {
            return initialPosition.equals(((Move) o).getInitialPosition()) && newPosition.equals(((Move) o).getNewPosition());
        } else {
            return false;
        }
    }

    @NotNull
    private int[] toArray() {
        int[] a = initialPosition.toArray();
        int[] b = newPosition.toArray();
        return new int[]{a[0], a[1], b[0], b[1]};
    }

    /**
     * @return new Move that is inverse of this
     */
    @NotNull
    public Move inverse() {
        return new Move(newPosition, initialPosition);
    }

    @Override
    public int compareTo(Move move) {
        int initDiff = initialPosition.compareTo(move.getInitialPosition());
        if (initDiff != 0) {
            return initDiff;
        } else {
            return newPosition.compareTo(move.getNewPosition());
        }
    }

    @Override
    @NotNull
    public Move copy() {
        return new Move(initialPosition.copy(), newPosition.copy());
    }
}
