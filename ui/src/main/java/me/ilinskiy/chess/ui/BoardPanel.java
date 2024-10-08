package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.MoveAwareBoard;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.Piece;
import me.ilinskiy.chess.impl.game.EnPassant;
import me.ilinskiy.chess.impl.game.BoardAnalyzer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;

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

    private MoveAwareBoard board;
    @Nullable
    private Coordinates selected;

    BoardPanel(@NotNull MoveAwareBoard board) {
        super();
        setBoard(board);
    }

    synchronized void setBoard(@NotNull MoveAwareBoard board) {
        this.board = board;
    }

    @Override
    public synchronized void paint(Graphics graphics) {
        super.paint(graphics);
        int heightAndWidth = getSize().width;
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(0, 0, heightAndWidth, heightAndWidth);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates c = new Coordinates(col, row);
                graphics.setColor(getBGForCell(c, getSelected(), board));
                drawCell(c, graphics, heightAndWidth);
            }
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawImageForCell(new Coordinates(col, row), graphics, board, heightAndWidth);
            }
        }
    }

    public synchronized void setSelected(@Nullable Coordinates coordinates) {
        selected = coordinates;
    }

    @Nullable
    public synchronized Coordinates getSelected() {
        return selected;
    }

    private static Color getBGForCell(Coordinates c, @Nullable Coordinates selected, MoveAwareBoard board) {
        if (c.equals(selected)) {
            return SELECT_COLOR;
        }
        if (selected != null && getAvailableNewPositions(selected, board).contains(c)) {
            for (Move selectedMove : BoardAnalyzer.getAvailableMovesForPiece(selected, board)) {
                if (selectedMove.getNewPositions()[0].equals(c) && selectedMove instanceof EnPassant) {
                    // En Passant.
                    return EAT_COLOR;
                }
            }
            if (board.getPiece(c) instanceof Piece) {
                return EAT_COLOR;
            }
            return MOVE_COLOR;
        }
        if (((c.getX() + c.getY()) % 2) == 0) {
            return WHITE_BG;
        }
        return BLACK_BG;
    }


    private static Set<Coordinates> getAvailableNewPositions(@NotNull Coordinates pos, @NotNull MoveAwareBoard board) {
        Set<Move> moves = BoardAnalyzer.getAvailableMovesForPiece(pos, board);
        Set<Coordinates> res = new HashSet<>(moves.size());
        for (Move move : moves) {
            Collections.addAll(res, move.getNewPositions());
        }
        return res;
    }


    private static void drawCell(Coordinates pos, Graphics graphics, int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        int initX = pos.getX() * cellSize;
        int initY = pos.getY() * cellSize;
        graphics.fillRect(initX, initY, cellSize, cellSize);
        Color oldColor = graphics.getColor();
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(initX, initY, cellSize, cellSize);
        graphics.setColor(oldColor);
    }

    private static void drawImageForCell(
            @NotNull Coordinates pos,
            @NotNull Graphics graphics,
            @NotNull MoveAwareBoard board,
            int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        ChessElement element = board.getPiece(pos);
        if (element instanceof Piece piece) {
            Image image = JSwingChessPainter.icons.get(piece);
            if (image != null) {
                int initX = pos.getX() * cellSize;
                int initY = pos.getY() * cellSize;
                graphics.drawImage(image, initX, initY, cellSize, cellSize, null);
            }
        }
    }
}
