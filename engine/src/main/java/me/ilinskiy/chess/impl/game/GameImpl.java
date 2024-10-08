package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.MoveAwareBoardImpl;
import me.ilinskiy.chess.impl.chessboard.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
@SuppressWarnings("WeakerAccess")
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
            if (board.getPiece(c).getColor() != whoseTurnIsIt()) {
                throw new RuntimeException("You can only move pieces of your color!");
            }
        }
        Set<Move> availableMoves = new HashSet<>();
        for (Coordinates coordinates : m.getInitialPositions()) {
            availableMoves.addAll(GameUtil.getAvailableMovesForPiece(coordinates, board));
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
    public PieceColor whoseTurnIsIt() {
        return turn;
    }

    @Override
    public boolean isGameOver() {
        return winner != null;
    }

    private void checkGameOver(@NotNull PieceColor nextToMove) {
        if (GameUtil.getAvailableMoves(nextToMove, board).size() == 0) {
            //game is over
            if (GameUtil.kingIsAttacked(nextToMove, board)) {
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
    @NotNull
    public List<Move> getMovesMade() {
        return board.getMoves();
    }

    @Override
    public int numberOfMovesMade() {
        // todo(stas): this is not necessary
        return board.getMoves().size();
    }

    @Override
    @NotNull
    public MoveAwareBoard getBoard() {
        return board.copy();
    }
}
