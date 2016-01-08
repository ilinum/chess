package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessboard.Board;
import me.ilinskiy.chess.chessboard.ChessElement;
import me.ilinskiy.chess.chessboard.Coordinates;
import me.ilinskiy.chess.chessboard.Piece;
import me.ilinskiy.chess.game.GameUtil;
import me.ilinskiy.chess.game.moves.EnPasse;
import me.ilinskiy.chess.game.moves.Move;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.Set;

import static me.ilinskiy.chess.chessboard.Board.BOARD_SIZE;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 9/27/15.
 */
final class BoardPanel extends JPanel {
    private static final Color WHITE_BG = new Color(0xCFB884);
    private static final Color BLACK_BG = new Color(0x76350F);
    private static final Color EAT_COLOR = new Color(0x0FCE3C);
    private static final Color MOVE_COLOR = new Color(0x5447FF);
    private static final Color SELECT_COLOR = new Color(0xFA1843);
    private static final Color BORDER_COLOR = Color.BLACK;

    private final Board myBoard;

    public BoardPanel(@NotNull Board board) {
        super();
        myBoard = board;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        int heightAndWidth = getSize().width;
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0, 0, heightAndWidth, heightAndWidth);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates c = new Coordinates(col, row);
                setBGForCell(c, graphics, myBoard);
                drawCell(c, graphics, heightAndWidth);
            }
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawImageForCell(new Coordinates(col, row), graphics, myBoard, heightAndWidth);
            }
        }
    }

    private static void setBGForCell(@NotNull Coordinates c, @NotNull Graphics graphics, @NotNull Board b) {
        Optional<Coordinates> selected = b.getSelected();

        if (c.equals(selected.orElse(null))) {
            graphics.setColor(SELECT_COLOR);
        } else if (selected.isPresent() && GameUtil.getAvailableNewPositions(selected.get(), b).contains(c)) {
            boolean isEnPasse = false;
            Set<Move> selectedMoves = GameUtil.getAvailableMovesForPiece(selected.get(), b);
            for (Move selectedMove : selectedMoves) {
                if (selectedMove.getNewPositions()[0].equals(c) && selectedMove instanceof EnPasse) {
                    isEnPasse = true;
                }
            }
            if (b.getPieceAt(c) instanceof Piece || isEnPasse) {
                graphics.setColor(EAT_COLOR);
            } else {
                graphics.setColor(MOVE_COLOR);
            }
        } else if (((c.getX() + c.getY()) % 2) == 0) {
            graphics.setColor(WHITE_BG);
        } else {
            graphics.setColor(BLACK_BG);
        }
    }

    private static void drawCell(@NotNull Coordinates pos, @NotNull Graphics graphics, int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        int initX = pos.getX() * cellSize;
        int initY = pos.getY() * cellSize;
        graphics.fillRect(initX, initY, cellSize, cellSize);
        Color oldColor = graphics.getColor();
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(initX, initY, cellSize, cellSize);
        graphics.setColor(oldColor);
    }

    private static void drawImageForCell(@NotNull Coordinates pos, @NotNull Graphics graphics,
                                         @NotNull Board board, int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        ChessElement element = board.getPieceAt(pos);
        if (element instanceof Piece) {
            Piece piece = (Piece) element;
            Image image = JSwingChessPainter.icons.get(piece);
            if (image != null) {
                int initX = pos.getX() * cellSize;
                int initY = pos.getY() * cellSize;
                graphics.drawImage(image, initX, initY, cellSize, cellSize, null);
            }
        }
    }
}
