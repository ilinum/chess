package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.annotations.Nullable;
import me.ilinskiy.chess.chessBoard.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

import static me.ilinskiy.chess.game.GameUtil.println;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class Game {
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

    public Game(@NotNull Player p1, @NotNull Player p2, @Nullable JFrame frame) {
        if (ChessBoardUtil.inverse(p1.getPlayerColor()) != p2.getPlayerColor()) {
            throw new IllegalArgumentException("Wrong colors for players!");
        }
        board = new BoardWrapper();
        movesMade = new ArrayList<>();
        player1 = p1;
        player2 = p2;
        turn = p1.getPlayerColor() == PieceColor.White ? p1 : p2;
        winner = Optional.empty();
        Board inner = board.getInner();
        if (frame != null) {
            frame.add(inner, BorderLayout.CENTER);
        }
    }

    public void makeMove() {
        if (isGameOver()) {
            throw new RuntimeException("Game is over! Cannot make more moves!");
        }
        Optional<Move> moveOptional = getMove();
        if (!moveOptional.isPresent()) {
            //it timed out
            winner = Optional.of(ChessBoardUtil.inverse(turn.getPlayerColor()));
            println("Timed out!");
        } else {
            Move m = moveOptional.get();
            Set<Move> availableMoves = GameUtil.getAvailableMovesForPiece(m.getInitialPosition(), board.getInner());
            if (!availableMoves.contains(m)) {
                throw new RuntimeException("Illegal move: " + m);
            }
            board.movePiece(m);
            checkPawnPromoted(m, turn);
            movesMade.add(m);
            turn = ChessBoardUtil.inverse(turn, player1, player2);
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
        try {
            result = future.get(GameRunner.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException ignored) {

        } catch (TimeoutException e) {
            future.cancel(true);
        }
        return Optional.ofNullable(result);
    }

    /**
     * NOTE: THIS METHOD MUST BE CALLED AFTER BOARD IS UPDATED!!!
     *
     * @param move that has been made
     */
    private void checkPawnPromoted(@NotNull Move move, @NotNull Player madeLastMove) {
        Coordinates newPosition = move.getNewPosition();
        ChessElement piece = board.getPieceAt(newPosition);
        if (piece.getType() == PieceType.Pawn) {
            PieceColor pieceColor = madeLastMove.getPlayerColor();
            int x = move.getNewPosition().getX();
            int y = move.getNewPosition().getY();
            Coordinates nextAdvance = new Coordinates(x, y + GameUtil.getDirectionForPlayer(pieceColor));
            if (ChessBoardUtil.isOutOfBounds(nextAdvance)) {
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

    @NotNull
    public PieceColor whoseTurnIsIt() {
        return turn.getPlayerColor();
    }

    public boolean isGameOver() {
        return winner.isPresent();
    }

    private void checkGameOver(@NotNull Player nextToMove) {
        if (GameUtil.getAvailableMoves(nextToMove.getPlayerColor(), board.getInner()).size() == 0) {
            //game is over
            if (GameUtil.kingIsAttacked(nextToMove.getPlayerColor(), board.getInner())) {
                winner = Optional.of((ChessBoardUtil.inverse(nextToMove, player1, player2)).getPlayerColor());
            } else {
                //it's a draw
                winner = Optional.of(PieceColor.Empty);
            }
        }
    }

    @NotNull
    public Optional<PieceColor> getWinner() {
        return winner;
    }

    @NotNull
    public List<Move> getMovesMade() {
        return movesMade;
    }

    public int numberOfMovesMade() {
        return movesMade.size();
    }
}
