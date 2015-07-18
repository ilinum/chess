package me.ilinskiy.game;

import com.sun.istack.internal.NotNull;
import me.ilinskiy.chessBoard.Coordinates;

import java.util.Arrays;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public class Move {
    private final Coordinates initialPosition;
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

    @NotNull
    private int[] toArray() {
        int[] a = initialPosition.toArray();
        int[] b = newPosition.toArray();
        return new int[]{a[0], a[1], b[0], b[1]};
    }
}
