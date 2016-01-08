package me.ilinskiy.chess.chessboard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.annotations.Nullable;
import me.ilinskiy.chess.game.moves.Move;
import me.ilinskiy.chess.ui.ChessPainter;
import me.ilinskiy.chess.util.Tuple2;

import java.util.List;

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

    private BoardWrapper(Board b) {
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
        List<Tuple2<Coordinates, Coordinates>> positions = move.zippedPositions();
        for (Tuple2<Coordinates, Coordinates> position : positions) {
            Coordinates initPos = position.getFirst();
            Coordinates newPos = position.getSecond();
            ChessElement initPosPiece = inner.getPieceAt(initPos);
            if (initPosPiece instanceof EmptyCell) {
                throw new IllegalArgumentException("Cannot move an empty cell!");
            }
            inner.setPieceAt(initPos, EmptyCell.INSTANCE);
            inner.setPieceAt(newPos, initPosPiece);
        }
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

    public static BoardWrapper getCopy(Board b) {
        return new BoardWrapper(b.copy());
    }
}
