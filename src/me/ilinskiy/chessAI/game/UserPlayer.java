package me.ilinskiy.chessAI.game;

import me.ilinskiy.chessAI.annotations.NotNull;
import me.ilinskiy.chessAI.chessBoard.Coordinates;
import me.ilinskiy.chessAI.chessBoard.ImmutableBoard;
import me.ilinskiy.chessAI.chessBoard.PieceColor;
import me.ilinskiy.chessAI.chessBoard.PieceType;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public class UserPlayer implements Player {
    private volatile Optional<Move> moveMade = Optional.empty();
    private volatile Lock mouseLock;
    private final Condition moveIsMade;
    private PieceColor myColor;

    public UserPlayer(PieceColor color) {
        myColor = color;
        mouseLock = new ReentrantLock();
        moveIsMade = mouseLock.newCondition();
    }

    @NotNull
    @Override
    public synchronized Move makeMove(@NotNull ImmutableBoard board) {
        board.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                mouseLock.lock();
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                int size = board.getFrameSize().width;
                int cellSize = size / ImmutableBoard.BOARD_SIZE;
                Coordinates location = new Coordinates((int) x / cellSize, (int) y / cellSize);
                Optional<Coordinates> selected = board.getSelected();
                if (selected.isPresent()) {
                    List<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(selected.get(), board);
                    Move m = new Move(selected.get(), location);
                    if (availableMovesForPiece.contains(m)) {
                        moveMade = Optional.of(m);
                        moveIsMade.signal();
                        mouseLock.unlock();
                    } else {
                        board.setSelected(location);
                    }
                } else {
                    board.setSelected(location);
                }
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });

        try {
            mouseLock.lock(); //todo: this doesn't work
            while (!moveMade.isPresent()) {
                moveIsMade.await();
            }
            Move move = moveMade.get();
            moveMade = Optional.empty();
            if (move == null) {
                throw new IllegalArgumentException("Move is null! Concurrency problems!");
            }
            return move;
        } catch (Exception e) {
            Move move = moveMade.get();
            moveMade = Optional.empty();
            if (move == null) {
                throw new IllegalArgumentException("Move is null! Concurrency problems!");
            }
            return move;
        } finally {
            mouseLock.unlock();
        }
    }

    @NotNull
    @Override
    public PieceColor getPlayerColor() {
        return myColor;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return PieceType.Queen; //todo: ask for piece type
    }
}
