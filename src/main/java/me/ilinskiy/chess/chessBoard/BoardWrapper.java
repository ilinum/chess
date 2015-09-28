package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.annotations.Nullable;
import me.ilinskiy.chess.game.Move;
import me.ilinskiy.chess.ui.ChessPainter;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public final class BoardWrapper {
    private final Board inner;

    public BoardWrapper(@Nullable ChessPainter painter) {
        inner = new Board(painter);
    }

    public BoardWrapper() {
        this((ChessPainter) null);
    }

    BoardWrapper(Board b) {
        inner = b;
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    public void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        inner.setPieceAt(pos, element);
    }

    public void setPieceAccordingToMove(@NotNull Move move) {
        ChessElement initPosPiece = inner.getPieceAt(move.getInitialPosition());
        if (initPosPiece instanceof EmptyCell) {
            throw new IllegalArgumentException("Cannot move an empty cell!");
        }
        inner.setPieceAt(move.getInitialPosition(), EmptyCell.INSTANCE);
        inner.setPieceAt(move.getNewPosition(), initPosPiece);
    }

    public void movePiece(@NotNull Move m) {
        inner.movePiece(m);
    }

    public Board getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}
