package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.annotations.Nullable;
import me.ilinskiy.chess.game.Castling;
import me.ilinskiy.chess.game.Copyable;
import me.ilinskiy.chess.game.GameUtil;
import me.ilinskiy.chess.game.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.ilinskiy.chess.chessBoard.PieceType.Pawn;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ImmutableBoard extends JPanel implements Copyable {
    public static final int BOARD_SIZE = 8;
    public static final int WHITE_DIRECTION = -1;
    public static final int BLACK_DIRECTION = 1;
    private ChessElement[][] board;
    private Optional<Coordinates> selected;
    private PieceColor turn;
    private List<Coordinates> piecesMoved;

    public ImmutableBoard() {
        super();
        ChessBoardUtil.initIcons();
        reset();
    }

    @SuppressWarnings("ConstantConditions")
    private void reset() {
        turn = PieceColor.White;
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
            board[currRow][i] = Piece.getPiece(topPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
        }
        currRow++;
        Arrays.fill(board[currRow], Piece.getPiece(topPieceColor, Pawn));

        currRow++;
        for (; currRow < board.length - 2; currRow++) {
            Arrays.fill(board[currRow], EmptyCell.INSTANCE);
        }

        Arrays.fill(board[currRow], Piece.getPiece(bottomPieceColor, Pawn));
        currRow++;
        assert (currRow == (BOARD_SIZE - 1)) : "Wrong current row: " + currRow;
        for (int i = 0; i < board[currRow].length; i++) {
            board[currRow][i] = Piece.getPiece(bottomPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
        }
        piecesMoved = new ArrayList<>();
        paint();
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
        if (move instanceof Castling) {
            Castling c = (Castling) move;
            makeActualMove(c.getKingInitialPosition(), c.getKingNewPosition());
            makeActualMove(c.getRookInitialPosition(), c.getRookNewPosition());
        } else {
            makeActualMove(move.getInitialPosition(), move.getNewPosition());
        }
        turn = ChessBoardUtil.inverse(turn);
        selected = Optional.empty();
    }

    private void makeActualMove(@NotNull Coordinates initialPosition, @NotNull Coordinates newPosition) {
        checkBounds(initialPosition);
        checkBounds(newPosition);
        piecesMoved.remove(initialPosition);
        piecesMoved.add(newPosition);
        ChessElement initialPositionPiece = board[initialPosition.getY()][initialPosition.getX()];
        if (initialPositionPiece instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        board[newPosition.getY()][newPosition.getX()] = initialPositionPiece;
        board[initialPosition.getY()][initialPosition.getX()] = EmptyCell.INSTANCE;
    }

    void movePiece(@NotNull Coordinates initPos, @NotNull Coordinates newPos) {
        movePiece(new Move(initPos, newPos));
    }

    public boolean pieceHasMovedSinceStartOfGame(@NotNull Coordinates pos) {
        return piecesMoved.contains(pos);
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

    private void checkBounds(@NotNull Coordinates c) {
        if (ChessBoardUtil.isOutOfBounds(c)) {
            throw new IllegalArgumentException("Index out of bounds: " + c);
        }
    }

    /**
     * set the cell at c selected
     *
     * @return true if a piece is there. False if it's an empty cell and nothing is done
     */
    public boolean setSelected(@NotNull Coordinates c) {
        checkBounds(c);
        if (board[c.getY()][c.getX()] instanceof EmptyCell || getPieceAt(c).getColor() != whoseTurnIsIt()) {
            return false;
        }
        selected = Optional.of(c);
        paint();
        return true;
    }

    @NotNull
    public PieceColor whoseTurnIsIt() {
        return turn;
    }

    private void paint() {
        paint(getGraphics());
    }

    @Override
    public void paint(@Nullable Graphics graphics) {
        if (graphics == null) {
            graphics = getGraphics();
            if (graphics == null) {
                return;
            }
        }
        super.paint(graphics); //this line is necessary for some reason. I have no idea why.
        Painter.paint(graphics, this);
    }


    @NotNull
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (ChessElement[] row : board) {
            res.append(Arrays.toString(row));
            res.append("\n");
        }
        return res.toString();
    }

    /**
     * @return a deep copy of this immutable board
     */
    @NotNull
    @Override
    public ImmutableBoard copy() {
        ImmutableBoard result = new ImmutableBoard();
        result.selected = this.selected;
        result.turn = this.turn;
        result.piecesMoved = GameUtil.copy(piecesMoved);
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(this.board[row], 0, result.board[row], 0, board.length);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImmutableBoard) {
            ImmutableBoard other = ((ImmutableBoard) o);
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!this.board[i][j].equals(other.board[i][j])) {
                        return false;
                    }
                }
            }
            return turn == other.turn && selected.equals(other.selected);
        } else {
            return false;
        }
    }

    public Dimension getFrameSize() {
        return Painter.getFrameSize(this);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board) * 31 + selected.hashCode() + turn.hashCode();
    }
}
