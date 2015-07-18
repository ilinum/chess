package me.ilinskiy.game;

import me.ilinskiy.chessBoard.*;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class Game {
    @NotNull
    private final Board board;
    private Player turn;
    private Optional<PieceColor> winner;
    private final List<Move> movesMade;
    private final Player player1;
    private final Player player2;

    public Game(@NotNull Player p1, @NotNull Player p2) {
        board = new Board();
        movesMade = new ArrayList<>();
        player1 = p1;
        player2 = p2;
        turn = p1;
        winner = Optional.empty();
    }

    public void makeMove() {
        if (isGameOver()) {
            throw new RuntimeException("Game is over! Cannot make more moves!");
        }
        Move m = turn.makeMove(board.getInner());
        if (!GameUtil.getAvailableMovesForPiece(m.getInitialPosition(), board.getInner()).contains(m)) {
            throw new RuntimeException("Illegal move: " + m);
        }
        board.movePiece(m);
        //if reverting moves will ever be implemented, will have trouble reverting pawn promotion
        checkPawnPromoted(m, turn);
        movesMade.add(m);
        turn = ChessBoardUtil.inverse(turn, player1, player2);
        checkGameOver(turn);
        throw new NotImplementedException();
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
            boolean isWhitePromoted = piece.getColor() == PieceColor.White && newPosition.getY() == 0;
            boolean isBlackPromoted = piece.getColor() == PieceColor.Black &&
                    newPosition.getY() == (ImmutableBoard.BOARD_SIZE - 1);
            if (isBlackPromoted || isWhitePromoted) {
                Piece promotedTo = new Piece(madeLastMove.getPlayerColor(), madeLastMove.getPieceTypeForPromotedPawn());
                board.setPieceAt(newPosition, promotedTo);
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
