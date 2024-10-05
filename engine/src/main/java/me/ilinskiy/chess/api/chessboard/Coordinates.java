package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/8/16
 */
public interface Coordinates extends Copyable, Comparable<Coordinates> {
    int getX();

    int getY();

    @NotNull
    int[] toArray();

    @NotNull
    @Override
    Coordinates copy();

    @Override
    int compareTo(@NotNull Coordinates coordinates);

    boolean isOutOfBounds();
}
