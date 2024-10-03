package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.impl.util.Tuple2;

import java.util.List;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface Move extends Copyable, Comparable<Move> {
    Coordinates[] getInitialPositions();

    Coordinates[] getNewPositions();

    List<Tuple2<Coordinates, Coordinates>> zippedPositions();
}
