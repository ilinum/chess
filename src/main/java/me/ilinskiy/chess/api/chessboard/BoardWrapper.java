package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/8/16
 */
public interface BoardWrapper {
    @NotNull
    ChessElement getPieceAt(@NotNull Coordinates c);

    void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element);

    void setPieceAccordingToMove(@NotNull Move move);

    void movePiece(@NotNull Move m);

    Board getInner();
}
