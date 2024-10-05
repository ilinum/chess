package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.ChessPainter;
import me.ilinskiy.chess.impl.chessboard.BoardWrapperImpl;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
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
    private final BoardWrapper board;
    private PieceColor turn;
    @Nullable
    private PieceColor winner;
    @NotNull
    private final List<Move> movesMade;

    public GameImpl(@NotNull PieceColor startPieceColor) {
        assert startPieceColor != PieceColor.Empty;
        board = new BoardWrapperImpl();
        movesMade = new ArrayList<>();
        turn = startPieceColor;
        winner = null;
    }

    @Override
    public void makeMove(@NotNull Move m, @NotNull Callable<PieceType> pieceTypeForPromotedPawn) {
        if (isGameOver()) {
            throw new RuntimeException("Game is over! Cannot make more moves!");
        }
        for (Coordinates c: m.getInitialPositions()) {
            if (board.getPieceAt(c).getColor() != whoseTurnIsIt()) {
                throw new RuntimeException("You can only move pieces of your color!");
            }
        }
        Set<Move> availableMoves = new HashSet<>();
        for (Coordinates coordinates : m.getInitialPositions()) {
            availableMoves.addAll(GameUtil.getAvailableMovesForPiece(coordinates, board.getInner()));
        }
        if (!availableMoves.contains(m)) {
            throw new RuntimeException("Illegal move: " + m);
        }
        board.movePiece(m);
        checkPawnPromoted(m, turn, pieceTypeForPromotedPawn);
        movesMade.add(m);
        turn = turn.inverse();
        checkGameOver(turn);
    }

    /**
     * NOTE: THIS METHOD MUST BE CALLED AFTER BOARD IS UPDATED!!!
     *
     * @param move that has been made
     */
    private void checkPawnPromoted(@NotNull Move move, @NotNull PieceColor madeLastMove,
                                   @NotNull Callable<PieceType> pieceTypeForPromotedPawn) {
        for (Coordinates newPosition : move.getNewPositions()) {
            ChessElement piece = board.getPieceAt(newPosition);
            if (piece.getType() == PieceType.Pawn) {
                int x = newPosition.getX();
                int y = newPosition.getY();
                Coordinates nextAdvance = new CoordinatesImpl(x, y + GameUtil.getDirectionForPlayer(madeLastMove));
                if (nextAdvance.isOutOfBounds()) {
                    ArrayList<PieceType> canBePromotedTo = new ArrayList<>(4);
                    canBePromotedTo.add(PieceType.Queen);
                    canBePromotedTo.add(PieceType.Rook);
                    canBePromotedTo.add(PieceType.Bishop);
                    canBePromotedTo.add(PieceType.Knight);
                    PieceType promotedTo = PieceType.Empty;
                    while (!canBePromotedTo.contains(promotedTo)) {
                        try {
                            promotedTo = pieceTypeForPromotedPawn.call();
                        } catch (Exception ignored) {

                        }
                    }
                    Piece promoted = Piece.createPiece(madeLastMove, promotedTo);
                    board.setPieceAt(newPosition, promoted);
                }
            }
        }
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
        if (GameUtil.getAvailableMoves(nextToMove, board.getInner()).size() == 0) {
            //game is over
            if (GameUtil.kingIsAttacked(nextToMove, board.getInner())) {
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
        return movesMade;
    }

    @Override
    public int numberOfMovesMade() {
        return movesMade.size();
    }

    @Override
    @NotNull
    public Board getBoard() {
        return board.getInner();
    }
}
