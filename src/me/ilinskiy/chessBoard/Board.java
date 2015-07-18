package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class Board {
    private final ImmutableBoard inner;

    public Board() {
        inner = new ImmutableBoard();
    }

    Board(ImmutableBoard b) {
        inner = b;
    }

    public void reset() {
        inner.reset();
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    public void movePiece(@NotNull Move m) {
        inner.movePiece(m);
    }

    public ImmutableBoard getInner() {
        return inner;
    }
}
