package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Castling;
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
    private static final Color WHITE_BG = new Color(0xCFB884);
    private static final Color BLACK_BG = new Color(0x76350F);


    static void paint(@NotNull Graphics graphics, @NotNull ImmutableBoard board) {
        int heightAndWidth = getFrameSize(board).width;
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, heightAndWidth, heightAndWidth);

        //draw black squares
        int brownStart = 1;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = brownStart; col < BOARD_SIZE; col += 2) {
                graphics.setColor(BLACK_BG);
                drawCell(new Coordinates(col, row), graphics, heightAndWidth);
            }
            brownStart = brownStart == 0 ? 1 : 0;
            for (int col = brownStart; col < BOARD_SIZE; col += 2) {
                graphics.setColor(WHITE_BG);
                drawCell(new Coordinates(col, row), graphics, heightAndWidth);
            }
        }

        paintSelected(graphics, board);

        graphics.setColor(Color.RED);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                drawImageForCell(new Coordinates(col, row), graphics, board, heightAndWidth);
            }
        }
    }

    public static void unPaintSelected(@NotNull Graphics graphics, @NotNull ImmutableBoard board) {
        paintSelected(graphics, board, true);
    }


    public static void paintSelected(@NotNull Graphics graphics, @NotNull ImmutableBoard board) {
        paintSelected(graphics, board, false);
    }

    private static void paintSelected(@NotNull Graphics graphics, @NotNull ImmutableBoard board, boolean unpaint) {
        Optional<Coordinates> selected = board.getSelected();
        if (selected.isPresent()) {
            if (unpaint) {
                if (((selected.get().getX() + selected.get().getY()) % 2) == 0) {
                    graphics.setColor(WHITE_BG);
                } else {
                    graphics.setColor(BLACK_BG);
                }
            } else {
                graphics.setColor(Color.RED);
            }
            Coordinates c = selected.get();
            int heightAndWidth = getFrameSize(board).width;
            drawCell(c, graphics, heightAndWidth);
            drawImageForCell(c, graphics, board, heightAndWidth);
            Set<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(c, board);
            for (Move move : availableMovesForPiece) {
                if (unpaint) {
                    if (((move.getNewPosition().getX() + move.getNewPosition().getY()) % 2) == 0) {
                        graphics.setColor(WHITE_BG);
                    } else {
                        graphics.setColor(BLACK_BG);
                    }
                } else {
                    if (board.getPieceAt(move.getNewPosition()) instanceof EmptyCell) {
                        graphics.setColor(Color.BLUE);
                    } else if (board.getPieceAt(move.getNewPosition()).getColor() != board.getPieceAt(selected.get()).getColor()) {
                        graphics.setColor(Color.GREEN);
                    }
                }
                drawCell(move.getNewPosition(), graphics, heightAndWidth);
                drawImageForCell(move.getNewPosition(), graphics, board, heightAndWidth);
            }
        }
    }

    private static void drawImageForCell(@NotNull Coordinates pos, @NotNull Graphics graphics,
                                         @NotNull ImmutableBoard board, int heightAndWidth) {
        int cellSize = heightAndWidth / BOARD_SIZE;
        ChessElement element = board.getPieceAt(pos);
        if (element instanceof Piece) {
            Piece piece = (Piece) element;
            Color c = piece.getColor() == PieceColor.Black ? Color.BLACK : Color.WHITE;
            graphics.setColor(c);
            Image image = ChessBoardUtil.icons.get(piece);
            if (image != null) {
                int initX = pos.getX() * cellSize;
                int initY = pos.getY() * cellSize;
                graphics.drawImage(image, initX, initY, cellSize, cellSize, null);
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

    public static void paint(@NotNull Graphics graphics, @NotNull ImmutableBoard board, @NotNull Move move) {
        if (move instanceof Castling) {
            Castling castling = (Castling) move;
            paint(graphics, board, new Move(castling.getKingInitialPosition(), castling.getKingNewPosition()));
            paint(graphics, board, new Move(castling.getRookInitialPosition(), castling.getRookNewPosition()));
        } else {
            int heightAndWidth = getFrameSize(board).width;
            Coordinates initPos = move.getInitialPosition();
            Coordinates newPos = move.getNewPosition();
            if (((initPos.getX() + initPos.getY()) % 2) == 0) {
                graphics.setColor(WHITE_BG);
            } else {
                graphics.setColor(BLACK_BG);
            }
            drawCell(initPos, graphics, heightAndWidth);
            drawImageForCell(initPos, graphics, board, heightAndWidth);

            if (((newPos.getX() + newPos.getY()) % 2) == 0) {
                graphics.setColor(WHITE_BG);
            } else {
                graphics.setColor(BLACK_BG);
            }
            drawCell(newPos, graphics, heightAndWidth);
            drawImageForCell(newPos, graphics, board, heightAndWidth);
        }
    }
}
