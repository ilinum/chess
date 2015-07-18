package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.ilinskiy.chessBoard.PieceType.Pawn;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class ImmutableBoard {
    public static final int BOARD_SIZE = 8;
    private ChessElement[][] board;

    public ImmutableBoard() {
        reset();
    }

    public void reset() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        int currRow = 0;
        //we have black pieces at the top
        for (int i = 0; i < board[0].length; i++) {
            board[currRow][i] = new Piece(PieceColor.Black, ChessBoardUtil.backRowPieceTypes[i]);
        }
        currRow++;
        Arrays.fill(board[currRow], new Piece(PieceColor.Black, Pawn));

        currRow++;
        for (; currRow < board.length - 2; currRow++) {
            Arrays.fill(board[currRow], EmptyCell.INSTANCE);
        }

        Arrays.fill(board[currRow], new Piece(PieceColor.White, Pawn));
        currRow++;
        assert (currRow == (BOARD_SIZE - 1)) : "Wrong current row: " + currRow;
        for (int i = 0; i < board[currRow].length; i++) {
            board[currRow][i] = new Piece(PieceColor.White, ChessBoardUtil.backRowPieceTypes[i]);
        }
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        checkBounds(c);
        return board[c.getX()][c.getY()];
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
        ChessElement initialPositionPiece = board[initialPosition.getX()][initialPosition.getY()];
        if (initialPositionPiece instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        board[move.getNewPosition().getX()][move.getNewPosition().getY()] = initialPositionPiece;
        board[initialPosition.getX()][initialPosition.getY()] = EmptyCell.INSTANCE;
    }

    private void checkBounds(@NotNull Move m) {
        checkBounds(m.getInitialPosition());
        checkBounds(m.getNewPosition());
    }

    private void checkBounds(@NotNull Coordinates c) {
        checkBounds(c.getX(), c.getY());
    }

    private void checkBounds(int x, int y) {
        if (x >= BOARD_SIZE || y >= BOARD_SIZE) {
            throw new IllegalArgumentException("Index out of bounds! x: " + x + ", y: " + y);
        }
    }
}
