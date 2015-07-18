package me.ilinskiy.game;

import me.ilinskiy.chessBoard.Board;
import me.ilinskiy.chessBoard.PieceColor;
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
    public Player turn;
    public Optional<PieceColor> winner;
    private List<Move> movesMade;
    private Player player1;
    private Player player2;

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
        board.movePiece(m);
        if (!GameUtil.getAvailableMovesForPiece(m.getInitialPosition(), board.getInner()).contains(m)) {
            throw new RuntimeException("Illegal move: " + m);
        }
        movesMade.add(m);
        turn = turn == player1 ? player2 : player1;
        checkGameOver(turn);
        throw new NotImplementedException();
    }

    @NotNull
    public PieceColor whoseTurnIsIt() {
        return turn.getPlayerColor();
    }

    public boolean isGameOver() {
        return winner.isPresent();
    }

    private void checkGameOver(@NotNull Player nextToMove) {
        if (GameUtil.getAvailableMoves(nextToMove, board.getInner()).size() == 0) {
            //game is over
            if (GameUtil.kingIsAttacked(nextToMove.getPlayerColor(), board.getInner())) {
                winner = Optional.of((nextToMove == player1 ? player2 : player1).getPlayerColor());
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
