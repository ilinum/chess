package me.ilinskiy.chessBoard;

import com.sun.istack.internal.NotNull;
import me.ilinskiy.game.Move;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public class Board {
    private final ImmutableBoard inner;

    public Board() {
        inner = new ImmutableBoard();
    }

    public void reset() {
        inner.reset();
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    public void movePiece(Move m) {
        inner.movePiece(m);
    }

    public ImmutableBoard getInner() {
        return inner;
    }
}
