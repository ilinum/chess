package me.ilinskiy.chess.game.moves;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessboard.Coordinates;
import me.ilinskiy.chess.game.Copyable;
import me.ilinskiy.chess.util.CollectionUtil;
import me.ilinskiy.chess.util.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 06.12.15.
 */
public abstract class Move implements Copyable, Comparable<Move> {
    public abstract Coordinates[] getInitialPositions();

    public abstract Coordinates[] getNewPositions();

    private Coordinates[] toArray() {
        Coordinates[] newPositions = getNewPositions();
        Coordinates[] initialPositions = getInitialPositions();
        Coordinates[] res = new Coordinates[newPositions.length + initialPositions.length];
        System.arraycopy(newPositions, 0, res, 0, newPositions.length);
        System.arraycopy(initialPositions, 0, res, newPositions.length, initialPositions.length);
        return res;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public boolean equals(Object o) {
        //noinspection SimplifiableIfStatement
        if (o instanceof Move) {
            return Arrays.equals(getInitialPositions(), ((Move) o).getInitialPositions()) &&
                    Arrays.equals(getNewPositions(), ((Move) o).getNewPositions());
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NotNull Move move) {
        Coordinates[] thatInitPos = move.getInitialPositions();
        Coordinates[] thisInitPos = getInitialPositions();
        int initDiff = thatInitPos.length - thisInitPos.length;
        if (initDiff != 0) {
            return initDiff;
        }
        Coordinates[] thatNewPos = move.getNewPositions();
        Coordinates[] thisNewPos = getNewPositions();
        initDiff = thatNewPos.length - thisNewPos.length;
        if (initDiff != 0) {
            return initDiff;
        }
        for (int i = 0; i < thatInitPos.length; i++) {
            int diff = thisInitPos[i].compareTo(thatInitPos[i]);
            if (diff != 0) {
                return diff;
            }
        }

        for (int i = 0; i < thatNewPos.length; i++) {
            int diff = thisNewPos[i].compareTo(thatNewPos[i]);
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    public List<Tuple2<Coordinates, Coordinates>> zippedPositions() {
        return CollectionUtil.zip(getInitialPositions(), getNewPositions());
    }
}
