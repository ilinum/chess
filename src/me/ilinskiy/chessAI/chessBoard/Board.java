package me.ilinskiy.chessAI.chessBoard;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.game.Move;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public final class Board {
    private final ImmutableBoard inner;

    public Board() {
        inner = new ImmutableBoard();
    }

    Board(ImmutableBoard b) {
        inner = b;
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    public void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        inner.setPieceAt(pos, element);
    }

    public void movePiece(@NotNull Move m) {
        inner.movePiece(m);
    }

    public void paint() {
        inner.paint(inner.getGraphics());
    }

    public ImmutableBoard getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}
