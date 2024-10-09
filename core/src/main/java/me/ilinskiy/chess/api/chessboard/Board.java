package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import org.jetbrains.annotations.NotNull;

public interface Board extends Copyable {
    int BOARD_SIZE = 8;
    int WHITE_DIRECTION = -1;
    int BLACK_DIRECTION = 1;

    @NotNull
    ChessElement get(@NotNull Coordinates c);

    void set(@NotNull Coordinates c, @NotNull ChessElement e);

    @Override
    Board copy();
}
