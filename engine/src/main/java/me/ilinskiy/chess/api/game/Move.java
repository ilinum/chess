package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface Move extends Copyable, Comparable<Move> {
    Coordinates[] getInitialPositions();

    Coordinates[] getNewPositions();
}
