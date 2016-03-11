package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.Move;
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

    void movePiece(@org.jetbrains.annotations.NotNull Move m);

    Board getInner();
}
