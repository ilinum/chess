package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.GameUtil;
import me.ilinskiy.chess.game.Move;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

import static me.ilinskiy.chess.chessBoard.ImmutableBoard.BOARD_SIZE;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/5/15.
 */
class Painter {
    //public static final Color WHITE_BG = new Color(0xFFF3E4);
    private static final Color BLACK_BG = new Color(0x53280C);

    static void paint(@NotNull Graphics graphics, @NotNull ImmutableBoard board) {
        int heightAndWidth = getFrameSize(board).width;
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, heightAndWidth, heightAndWidth);

        //draw black squares
        int cellSize = heightAndWidth / BOARD_SIZE;
        int brownStart = 1;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = brownStart; col < BOARD_SIZE; col += 2) {
                graphics.setColor(BLACK_BG);
                drawCell(new Coordinates(col, row), graphics, heightAndWidth);
            }
            brownStart = brownStart == 0 ? 1 : 0;
        }

        paintSelected(graphics, board);

        graphics.setColor(Color.RED);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessElement element = board.getPieceAt(new Coordinates(row, col));
                if (element instanceof Piece) {
                    Piece piece = (Piece) element;
                    Color c = piece.getColor() == PieceColor.Black ? Color.BLACK : Color.WHITE;
                    graphics.setColor(c);
                    Image image = ChessBoardUtil.icons.get(piece);
                    if (image != null) {
                        int initX = row * cellSize;
                        int initY = col * cellSize;
                        graphics.drawImage(image, initX, initY, cellSize, cellSize, null);
                    }
                }
            }
        }
    }


    private static void paintSelected(@NotNull Graphics graphics, @NotNull ImmutableBoard board) {
        Optional<Coordinates> selected = board.getSelected();
        if (selected.isPresent()) {
            graphics.setColor(Color.RED);
            Coordinates c = selected.get();
            int heightAndWidth = getFrameSize(board).width;
            drawCell(c, graphics, heightAndWidth);
            Set<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(c, board);
            for (Move move : availableMovesForPiece) {
                if (board.getPieceAt(move.getNewPosition()) instanceof EmptyCell) {
                    graphics.setColor(Color.BLUE);
                    drawCell(move.getNewPosition(), graphics, heightAndWidth);
                } else if (board.getPieceAt(move.getNewPosition()).getColor() != board.getPieceAt(selected.get()).getColor()) {
                    graphics.setColor(Color.GREEN);
                    drawCell(move.getNewPosition(), graphics, heightAndWidth);
                }
            }
        }
    }

    static Dimension getFrameSize(@NotNull ImmutableBoard board) {
        return board.getSize();
    }

    private static void drawCell(@NotNull Coordinates pos, @NotNull Graphics graphics, int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        int initX = pos.getX() * cellSize;
        int initY = pos.getY() * cellSize;
        graphics.fillRect(initX, initY, cellSize, cellSize);
        Color oldColor = graphics.getColor();
        graphics.setColor(Color.BLACK);
        graphics.drawRect(initX, initY, cellSize, cellSize);
        graphics.setColor(oldColor);
    }

}
