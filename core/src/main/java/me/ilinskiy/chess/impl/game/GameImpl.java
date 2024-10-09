package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.MoveAwareBoardImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class GameImpl implements Game {
    @NotNull
    private final MoveAwareBoard board;
    private PieceColor turn;
    @Nullable
    private PieceColor winner;

    public GameImpl(@NotNull PieceColor startPieceColor) {
        assert startPieceColor != PieceColor.Empty;
        board = new MoveAwareBoardImpl();
        turn = startPieceColor;
        winner = null;
    }

    @Override
    public void makeMove(@NotNull Move m) {
        if (isGameOver()) {
            throw new RuntimeException("Game is over! Cannot make more moves!");
        }
        for (Coordinates c : m.getInitialPositions()) {
            if (board.getPiece(c).getColor() != getTurn()) {
                throw new RuntimeException("You can only move pieces of your color!");
            }
        }
        Set<Move> availableMoves = new HashSet<>();
        for (Coordinates coordinates : m.getInitialPositions()) {
            availableMoves.addAll(BoardAnalyzer.getAvailableMovesForPiece(coordinates, board));
        }
        if (!availableMoves.contains(m)) {
            throw new RuntimeException("Illegal move: " + m);
        }
        board.makeMove(m);
        turn = turn.inverse();
        checkGameOver(turn);
    }

    @Override
    @NotNull
    public PieceColor getTurn() {
        return turn;
    }

    private void checkGameOver(@NotNull PieceColor nextToMove) {
        if (BoardAnalyzer.getAvailableMoves(nextToMove, board).isEmpty()) {
            //game is over
            if (BoardAnalyzer.kingIsAttacked(nextToMove, board)) {
                winner = nextToMove.inverse();
            } else {
                //it's a draw
                winner = PieceColor.Empty;
            }
        }
    }

    @Override
    @NotNull
    public Optional<PieceColor> getWinner() {
        return Optional.ofNullable(winner);
    }

    @Override
    public boolean isGameOver() {
        return winner != null;
    }

    @Override
    @NotNull
    public MoveAwareBoard getBoard() {
        return board.copy();
    }
}
