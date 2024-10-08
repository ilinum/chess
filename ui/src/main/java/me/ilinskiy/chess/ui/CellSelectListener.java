package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CellSelectListener implements MouseListener {
    @NotNull
    private final Lock mouseLock;
    @NotNull
    private final Condition selectionIsMade;
    @NotNull
    private final BoardPanel panel;
    @Nullable
    private Coordinates selection;

    CellSelectListener(@NotNull BoardPanel panel) {
        mouseLock = new ReentrantLock();
        selectionIsMade = mouseLock.newCondition();
        this.panel = panel;
    }

    @Nullable
    Coordinates waitForSelection() {
        mouseLock.lock();
        try {
            while (selection == null) {
                selectionIsMade.await();
            }
            return selection;
        } catch (InterruptedException e) {
            // The caller will try again.
            return null;
        } finally {
            mouseLock.unlock();
        }
    }

    @Override
    public void mouseClicked(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(@NotNull MouseEvent mouseEvent) {
        mouseLock.lock();
        try {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            int size = panel.getSize().width;
            int cellSize = size / Board.BOARD_SIZE;
            selection = new Coordinates((int) x / cellSize, (int) y / cellSize);
            selectionIsMade.signal();
        } finally {
            mouseLock.unlock();
        }
    }

    @Override
    public void mouseEntered(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(@NotNull MouseEvent mouseEvent) {
    }
}
