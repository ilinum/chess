package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/8/16
 */
public interface Board extends Copyable {
    int BOARD_SIZE = 8;
    int WHITE_DIRECTION = -1;
    int BLACK_DIRECTION = 1;

    @NotNull
    ChessElement getPieceAt(@NotNull Coordinates c);

    boolean pieceHasNotMovedSinceStartOfGame(@NotNull Coordinates pos);

    @NotNull
    @Override
    Board copy();

    @NotNull
    Optional<Move> getLastMove();
}
