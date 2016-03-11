package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.Move;
import org.jetbrains.annotations.NotNull;
import me.ilinskiy.chess.api.game.Copyable;
import me.ilinskiy.chess.api.ui.ChessPainter;

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
    ChessElement getPieceAt(@org.jetbrains.annotations.NotNull Coordinates c);

    boolean pieceHasNotMovedSinceStartOfGame(@NotNull Coordinates pos);

    @NotNull
    Optional<Coordinates> getSelected();

    boolean setSelected(@NotNull Coordinates newSelected);

    @NotNull
    PieceColor whoseTurnIsIt();

    @org.jetbrains.annotations.NotNull
    @Override
    Board copy();

    void paintCell(@org.jetbrains.annotations.NotNull Coordinates pos);

    @NotNull
    Optional<Move> getLastMove();

    @org.jetbrains.annotations.Nullable
    ChessPainter getPainter();
}
