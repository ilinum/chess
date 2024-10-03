package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.ChessPainter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    Coordinates getSelected();

    boolean setSelected(@NotNull Coordinates newSelected);

    @NotNull
    PieceColor whoseTurnIsIt();

    @NotNull
    @Override
    Board copy();

    void paintCell(@NotNull Coordinates pos);

    @NotNull
    Optional<Move> getLastMove();

    @Nullable
    ChessPainter getPainter();
}
