package me.ilinskiy.game;

import me.ilinskiy.chessBoard.Coordinates;
import me.ilinskiy.chessBoard.ImmutableBoard;
import me.ilinskiy.chessBoard.PieceColor;
import me.ilinskiy.chessBoard.PieceType;
import org.jetbrains.annotations.NotNull;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public class UserPlayer implements Player {
    @NotNull
    @Override
    public Move makeMove(@NotNull ImmutableBoard b) {
        b.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                int size = b.getFrameSize().width;
                int cellSize = size / ImmutableBoard.BOARD_SIZE;
                Coordinates location = new Coordinates((int) x / cellSize, (int) y / cellSize);
                b.setSelected(location); //todo: fix stuff with inverse
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        while (true) {

        }
//        return null;
    }

    @NotNull
    @Override
    public PieceColor getPlayerColor() {
        return PieceColor.Empty;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return PieceType.King;
    }
}
