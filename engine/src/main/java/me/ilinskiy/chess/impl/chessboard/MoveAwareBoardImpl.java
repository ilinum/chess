package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.game.Castling;
import me.ilinskiy.chess.impl.game.EnPassant;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.ilinskiy.chess.api.chessboard.PieceType.Pawn;

public class MoveAwareBoardImpl implements MoveAwareBoard {
    private final Board board;
    private final List<Move> moves;

    public MoveAwareBoardImpl() {
        this(new BoardImpl());
    }

    public MoveAwareBoardImpl(@NotNull Board board) {
        this(board, new ArrayList<>());
    }

    public MoveAwareBoardImpl(@NotNull Board board, List<Move> moves) {
        this.board = board;
        this.moves = moves;
    }

    @Override
    public @NotNull ChessElement getPiece(@NotNull Coordinates c) {
        return board.get(c);
    }

    @Override
    public @NotNull List<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    public @NotNull Board getBoardCopy() {
        return board.copy();
    }

    @Override
    public MoveAwareBoard copy() {
        return new MoveAwareBoardImpl(board.copy(), getMoves());
    }

    @Override
    public void makeMove(@NotNull Move move) {
        switch (move) {
            case Castling c -> {
                makeActualMove(c.getKingInitialPosition(), c.getKingNewPosition());
                makeActualMove(c.getRookInitialPosition(), c.getRookNewPosition());
            }
            case EnPassant enPassant -> {
                Coordinates eaten = enPassant.eatenPiece();
                assert getPiece(eaten).getType() == Pawn;
                board.set(eaten, EmptyCell.INSTANCE);
                makeActualMove(enPassant.initialPosition, enPassant.newPosition);
            }
            case RegularMove rm -> {
                makeActualMove(rm.initialPosition, rm.newPosition);
            }
            case PawnPromotion pp -> {
                makeActualMove(pp.initialPosition, pp.newPosition);
                board.set(pp.newPosition, Piece.createPiece(board.get(pp.newPosition).getColor(), pp.promoteTo));
            }
            default -> {
                throw new RuntimeException("Unknown Move type: " + move);
            }
        }
        moves.add(move);
    }

    private void makeActualMove(@NotNull Coordinates initialPosition, @NotNull Coordinates newPosition) {
        ChessElement initialPositionPiece = board.get(initialPosition);
        if (initialPositionPiece instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        board.set(newPosition, initialPositionPiece);
        board.set(initialPosition, EmptyCell.INSTANCE);
    }

    // todo(stas): tests
    public static boolean pieceHasMovedSinceStartOfGame(@NotNull List<Move> moves, @NotNull Coordinates curPos) {
        for (Move move : moves) {
            for (Coordinates newPos : move.getNewPositions()) {
                if (curPos.equals(newPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MoveAwareBoardImpl other) {
            return this.board.equals(other.board) && this.moves.equals(other.moves);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, moves);
    }
}
