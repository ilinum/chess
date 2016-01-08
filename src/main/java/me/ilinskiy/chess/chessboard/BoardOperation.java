package me.ilinskiy.chess.chessboard;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
@FunctionalInterface
public interface BoardOperation<V> {
    V run(Board b);
}
