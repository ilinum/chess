package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;
import me.ilinskiy.chess.chessBoard.ImmutableBoard;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.chessBoard.PieceType;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public class UserPlayer implements Player {
    private Optional<Move> moveMade = Optional.empty();
    @NotNull
    private final Lock mouseLock;
    @NotNull
    private final Condition moveIsMade;
    private final PieceColor myColor;

    public UserPlayer(PieceColor color) {
        myColor = color;
        mouseLock = new ReentrantLock();
        moveIsMade = mouseLock.newCondition();
    }

    @NotNull
    @Override
    public Move getMove(@NotNull ImmutableBoard board) {
        MouseListener mouseListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(@NotNull MouseEvent mouseEvent) {
                mouseLock.lock();
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                int size = board.getFrameSize().width;
                int cellSize = size / ImmutableBoard.BOARD_SIZE;
                Coordinates location = new Coordinates((int) x / cellSize, (int) y / cellSize);
                Optional<Coordinates> selected = board.getSelected();
                if (selected.isPresent()) {
                    Set<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(selected.get(), board);
                    Move m = new Move(selected.get(), location);
                    Optional<Move> res = Optional.empty();
                    for (Move move : availableMovesForPiece) {
                        if (m.equals(move)) {
                            res = Optional.of(move);
                        }
                    }
                    if (res.isPresent()) {
                        moveMade = res;
                        moveIsMade.signal();
                    } else {
                        board.setSelected(location);
                    }
                } else {
                    board.setSelected(location);
                }
                mouseLock.unlock();
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
        board.addMouseListener(mouseListener);

        try {
            mouseLock.lock();
            while (!moveMade.isPresent()) { //"You shall always wait in a while loop," - Alison Norman
                try {
                    moveIsMade.await();
                } catch (InterruptedException ignored) {

                }
            }
            Move move = moveMade.get();
            assert move != null;
            moveMade = Optional.empty();
            return move;
        } finally {
            assert board.getMouseListeners().length > 0;
            board.removeMouseListener(mouseListener);
            assert board.getMouseListeners().length == 0;
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
        return PieceType.Queen;
    }

    @Override
    public UserPlayer copy() {
        return new UserPlayer(myColor);
    }
}
