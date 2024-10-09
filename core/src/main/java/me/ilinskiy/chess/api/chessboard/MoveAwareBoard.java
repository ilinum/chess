package me.ilinskiy.chess.api.chessboard;

import me.ilinskiy.chess.api.game.Copyable;
import me.ilinskiy.chess.api.game.Move;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MoveAwareBoard extends Copyable {
    @NotNull
    ChessElement getPiece(@NotNull Coordinates c);

    void makeMove(@NotNull Move m);

    @NotNull
    List<Move> getMoves();

    @NotNull
    Board getBoardCopy();

    @Override
    MoveAwareBoard copy();
}
