package me.ilinskiy.chess.impl.util;

import me.ilinskiy.chess.api.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 06.12.15.
 */
public final class Tuple2<T1, T2> {
    private final T1 first;
    private final T2 second;

    public Tuple2(@NotNull T1 first, @NotNull T2 second) {
        this.first = first;
        this.second = second;
    }

    public T2 getSecond() {
        return second;
    }

    public T1 getFirst() {
        return first;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple2) {
            Tuple2 that = (Tuple2) obj;
            return first.equals(that.first) && second.equals(that.second);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 13 + second.hashCode();
    }
}
