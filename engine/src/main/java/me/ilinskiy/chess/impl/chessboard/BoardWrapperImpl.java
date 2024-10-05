package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.BoardWrapper;
import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.util.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public final class BoardWrapperImpl implements BoardWrapper {
    private final BoardImpl inner;

    public BoardWrapperImpl() {
        inner = new BoardImpl();
    }

    private BoardWrapperImpl(BoardImpl b) {
        inner = b;
    }

    @Override
    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    @Override
    public void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        inner.setPieceAt(pos, element);
    }

    @Override
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

    @Override
    public void movePiece(@NotNull Move m) {
        inner.movePiece(m);
    }

    @Override
    public Board getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return inner.toString();
    }

    public static BoardWrapper getCopy(Board b) {
        return new BoardWrapperImpl((BoardImpl) b.copy());
    }
}
