package me.ilinskiy.chess.chessBoard;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
@FunctionalInterface
public interface BoardOperation {
    boolean run(Board b);
}
