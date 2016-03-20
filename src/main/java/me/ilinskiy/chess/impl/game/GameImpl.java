package me.ilinskiy.chess.impl.game;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Game;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.ChessPainter;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.chessboard.BoardWrapperImpl;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.chessboard.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.*;

import static me.ilinskiy.chess.impl.game.GameUtil.println;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
@SuppressWarnings("WeakerAccess")
public final class GameImpl implements Game {
    @NotNull
    private final BoardWrapper board;
    private Player turn;
    private Optional<PieceColor> winner;
    @NotNull
    private final List<Move> movesMade;
    @NotNull
    private final Player player1;
    @NotNull
    private final Player player2;
    private Optional<ChessPainter> myPainter;

    public GameImpl(@NotNull Player p1, @NotNull Player p2, @Nullable ChessPainter painter) {
        if (p1.getPlayerColor().inverse() != p2.getPlayerColor()) {
            throw new IllegalArgumentException("Wrong colors for players!");
        }
        board = new BoardWrapperImpl(painter);
        movesMade = new ArrayList<>();
        player1 = p1;
        player2 = p2;
        turn = p1.getPlayerColor() == PieceColor.White ? p1 : p2;
        winner = Optional.empty();
        myPainter = Optional.ofNullable(painter);
    }

    @Override
    public void makeMove() {
        if (isGameOver()) {
            throw new RuntimeException("Game is over! Cannot make more moves!");
        }
        Optional<Move> moveOptional = getMove();
        if (!moveOptional.isPresent()) {
            //it timed out
            winner = Optional.of(turn.getPlayerColor().inverse());
            println("Timed out!");
        } else {
            Move m = moveOptional.get();
            Set<Move> availableMoves = new HashSet<>();
            for (Coordinates coordinates : m.getInitialPositions()) {
                availableMoves.addAll(GameUtil.getAvailableMovesForPiece(coordinates, board.getInner()));
            }
            if (!availableMoves.contains(m)) {
                throw new RuntimeException("Illegal move: " + m);
            }
            board.movePiece(m);
            checkPawnPromoted(m, turn);
            movesMade.add(m);
            turn = turn.inverse(player1, player2);
            checkGameOver(turn);
        }
    }

    private Optional<Move> getMove() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Move> future = executorService.submit(() -> {
            Move m = null;
            while (m == null) {
                m = turn.getMove(board.getInner());
            }
            return m;
        });
        Move result = null;
        Optional<Thread> updateTimeLeftThread = Optional.empty();
        try {
            if (GameRunnerImpl.TIMEOUT_IN_SECONDS > 0) {
                if (myPainter.isPresent()) {
                    updateTimeLeftThread = Optional.ofNullable(myPainter.get().getUpdateTimeLeftThread());
                    updateTimeLeftThread.ifPresent(Thread::start);
                }
                result = future.get(GameRunnerImpl.TIMEOUT_IN_SECONDS + 1, TimeUnit.SECONDS); //be nice and add an extra second
            } else {
                result = future.get();
            }
        } catch (InterruptedException | ExecutionException ignored) {

        } catch (TimeoutException e) {
            future.cancel(true);
        } finally {
            updateTimeLeftThread.ifPresent(Thread::interrupt);
        }
        return Optional.ofNullable(result);
    }

    /**
     * NOTE: THIS METHOD MUST BE CALLED AFTER BOARD IS UPDATED!!!
     *
     * @param move that has been made
     */
    private void checkPawnPromoted(@NotNull Move move, @NotNull Player madeLastMove) {
        for (Coordinates newPosition : move.getNewPositions()) {
            ChessElement piece = board.getPieceAt(newPosition);
            if (piece.getType() == PieceType.Pawn) {
                PieceColor pieceColor = madeLastMove.getPlayerColor();
                int x = newPosition.getX();
                int y = newPosition.getY();
                Coordinates nextAdvance = new CoordinatesImpl(x, y + GameUtil.getDirectionForPlayer(pieceColor));
                if (nextAdvance.isOutOfBounds()) {
                    ArrayList<PieceType> canBePromotedTo = new ArrayList<>(4);
                    canBePromotedTo.add(PieceType.Queen);
                    canBePromotedTo.add(PieceType.Rook);
                    canBePromotedTo.add(PieceType.Bishop);
                    canBePromotedTo.add(PieceType.Knight);
                    PieceType promotedTo = PieceType.Empty;
                    while (!canBePromotedTo.contains(promotedTo)) {
                        promotedTo = madeLastMove.getPieceTypeForPromotedPawn();
                    }
                    Piece promoted = Piece.createPiece(pieceColor, promotedTo);
                    board.setPieceAt(newPosition, promoted);
                    board.getInner().paintCell(newPosition);
                }
            }
        }
    }

    @Override
    @NotNull
    public PieceColor whoseTurnIsIt() {
        return turn.getPlayerColor();
    }

    @Override
    public boolean isGameOver() {
        return winner.isPresent();
    }

    private void checkGameOver(@NotNull Player nextToMove) {
        if (GameUtil.getAvailableMoves(nextToMove.getPlayerColor(), board.getInner()).size() == 0) {
            //game is over
            if (GameUtil.kingIsAttacked(nextToMove.getPlayerColor(), board.getInner())) {
                winner = Optional.of((nextToMove.inverse(player1, player2)).getPlayerColor());
            } else {
                //it's a draw
                winner = Optional.of(PieceColor.Empty);
            }
        }
    }

    @Override
    @NotNull
    public Optional<PieceColor> getWinner() {
        return winner;
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
}
