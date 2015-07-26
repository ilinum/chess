package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

import static me.ilinskiy.chessBoard.PieceType.Pawn;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ImmutableBoard extends JPanel {
    public static final int BOARD_SIZE = 8;
    private ChessElement[][] board;
    private Optional<Coordinates> selected;
    public static final int WHITE_DIRECTION = -1;
    public static final int BLACK_DIRECTION = 1;

    public ImmutableBoard() {
        ChessBoardUtil.initIcons();
        reset();
    }

    @SuppressWarnings("ConstantConditions")
    public void reset() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        selected = Optional.empty();
        int currRow = 0;
        assert ChessBoardUtil.backRowPieceTypes.length == BOARD_SIZE;

        PieceColor topPieceColor;
        PieceColor bottomPieceColor;
        if (BLACK_DIRECTION == 1) {
            assert WHITE_DIRECTION == -1;
            //we have black pieces at the top
            topPieceColor = PieceColor.Black;
            bottomPieceColor = PieceColor.White;
        } else {
            assert BLACK_DIRECTION == -1;
            assert WHITE_DIRECTION == 1;
            topPieceColor = PieceColor.Black;
            bottomPieceColor = PieceColor.White;
        }
        for (int i = 0; i < board[0].length; i++) {
            board[currRow][i] = new Piece(topPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
        }
        currRow++;
        Arrays.fill(board[currRow], new Piece(topPieceColor, Pawn));

        currRow++;
        for (; currRow < board.length - 2; currRow++) {
            Arrays.fill(board[currRow], EmptyCell.INSTANCE);
        }

        Arrays.fill(board[currRow], new Piece(bottomPieceColor, Pawn));
        currRow++;
        assert (currRow == (BOARD_SIZE - 1)) : "Wrong current row: " + currRow;
        for (int i = 0; i < board[currRow].length; i++) {
            board[currRow][i] = new Piece(bottomPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
        }
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        checkBounds(c);
        return board[c.getY()][c.getX()];
    }

    /**
     * Method that lets you change position of a piece on the board
     * NOTE! This will overwrite anything at the position move.getNewPosition !!!
     *
     * @param move move that was made
     */
    void movePiece(@NotNull Move move) {
        checkBounds(move);
        Coordinates initialPosition = move.getInitialPosition();
        ChessElement initialPositionPiece = board[initialPosition.getY()][initialPosition.getX()];
        if (initialPositionPiece instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        selected = Optional.empty();
        board[move.getNewPosition().getY()][move.getNewPosition().getX()] = initialPositionPiece;
        board[initialPosition.getY()][initialPosition.getX()] = EmptyCell.INSTANCE;
    }


    /**
     * @return copy of selected coordinates
     */
    @NotNull
    public Optional<Coordinates> getSelected() {
        if (selected.isPresent()) {
            return Optional.of(selected.get().copy());
        }
        return Optional.empty();
    }


    void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        checkBounds(pos);
        board[pos.getY()][pos.getX()] = element;
    }

    private void checkBounds(@NotNull Move m) {
        checkBounds(m.getInitialPosition());
        checkBounds(m.getNewPosition());
    }

    private void checkBounds(@NotNull Coordinates c) {
        if (ChessBoardUtil.isOutOfBounds(c)) {
            throw new IllegalArgumentException("Index out of bounds: " + c);
        }
    }

    /**
     * //todo add unset method?
     * set the cell at c selected
     *
     * @return true if a piece is there. False if it's an empty cell and nothing is done
     */
    public boolean setSelected(@NotNull Coordinates c) {
        checkBounds(c);
        if (board[c.getY()][c.getX()] instanceof EmptyCell) {
            return false;
        }
        selected = Optional.of(c);
        paint(getGraphics());
        return true;
    }

    public static final Color WHITE_BG = new Color(0xcdc1b4);
    public static final Color BLACK_BG = new Color(0x53280C);

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        int heightAndWidth = getFrameSize().width;
        graphics.setColor(WHITE_BG);
        graphics.drawRect(0, 0, heightAndWidth, heightAndWidth);

        //draw black squares
        int cellSize = heightAndWidth / BOARD_SIZE;
        int brownStart = 1;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = brownStart; col < BOARD_SIZE; col += 2) {
                graphics.setColor(BLACK_BG);
                drawCell(new Coordinates(col, row), graphics);
//                int initX = col * cellSize;
//                int initY = row * cellSize;
//                graphics.fillRect(initX, initY, cellSize, cellSize);
            }
            brownStart = brownStart == 0 ? 1 : 0;
        }

        //set selected
        if (selected.isPresent()) {
            graphics.setColor(Color.RED);
            Coordinates c = selected.get();
//            drawCell(c.inverse(), graphics);
            int initX = c.getX() * cellSize;
            int initY = c.getY() * cellSize;
            graphics.fillRect(initX, initY, cellSize, cellSize);
//            List<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(c, this);
//            for (Move move : availableMovesForPiece) {
//                if (getPieceAt(move.getNewPosition()) instanceof EmptyCell) {
//                    graphics.setColor(Color.BLUE);
//                }
//            }
        }

        graphics.setColor(Color.RED);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessElement element = getPieceAt(new Coordinates(row, col));
                if (element instanceof Piece) {
                    Piece piece = (Piece) element;
                    Color c = piece.getColor() == PieceColor.Black ? Color.BLACK : Color.WHITE;
                    graphics.setColor(c);
                    Image image = ChessBoardUtil.icons.get(piece);
                    if (image != null) {
                        int initX = col * cellSize;
                        int initY = row * cellSize;
                        graphics.drawImage(image, initX, initY, cellSize / 2, cellSize / 2, this);
                    }
                }
            }
        }
    }

    private void drawCell(Coordinates pos, Graphics graphics) {
        int heightAndWidth = getFrameSize().width;
        int cellSize = heightAndWidth / BOARD_SIZE;
        int initX = pos.getX() * cellSize;
        int initY = pos.getY() * cellSize;
        graphics.fillRect(initX, initY, cellSize, cellSize);
    }

    public Dimension getFrameSize() {
        Optional<Window> frame = Optional.of(SwingUtilities.windowForComponent(this));
        Dimension res = getFrameSize(frame);

        //todo: maybe don't change the size of the frame? Instead just adjust the size of JPanel to fit. Not as pretty
        //but works better when in full-screen
        if ((res.getHeight() != res.getWidth()) || ((res.getHeight() % ImmutableBoard.BOARD_SIZE) != 0)) {
            double average = (res.getHeight() + res.getWidth()) / 2;
            int newSize = ((int) (average / ImmutableBoard.BOARD_SIZE)) * ImmutableBoard.BOARD_SIZE; //so it's divisible by 8
            if (Optional.of(frame).isPresent()) {
                frame.get().setSize(newSize, newSize);
            }
            setSize(newSize, newSize);
            res = getSize();
        }
        assert (res.getHeight() == res.getWidth()) : "height: " + res.getHeight() + ", width: " + res.getWidth();
        assert ((res.getHeight() % ImmutableBoard.BOARD_SIZE) == 0) : "Height: " + res.getHeight();
        return res;
    }

    @NotNull
    private Dimension getFrameSize(Optional<Window> frame) {
        Dimension res;
        Dimension minSize;
        if (frame.isPresent()) {
            res = frame.get().getSize();
            minSize = frame.get().getMinimumSize();
            if (res.getHeight() < minSize.getHeight() || res.getWidth() < minSize.getWidth()) {
                frame.get().setSize(minSize);
                res = minSize;
            }
        } else {
            res = getSize();
            minSize = getMinimumSize();
            if (res.getHeight() < minSize.getHeight() || res.getWidth() < minSize.getWidth()) {
                setSize(minSize);
                res = minSize;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return Arrays.toString(board);
    }
}
