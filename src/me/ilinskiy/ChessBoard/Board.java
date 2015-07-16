package me.ilinskiy.ChessBoard;

import com.sun.istack.internal.NotNull;

import java.util.Arrays;

import static me.ilinskiy.ChessBoard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class Board {
    public static final int BOARD_SIZE = 8;
    public static final PieceType[] backRowPieceTypes = new PieceType[]{Rook, Knight, Bishop, Queen, King, Bishop,
            Knight, Rook};
    private ChessElement[][] board;

    public Board() {
        reset();
    }

    public void reset() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        int currRow = 0;
        //we have black pieces at the top
        for (int i = 0; i < board[0].length; i++) {
            board[currRow][i] = new Piece(PieceColor.Black, backRowPieceTypes[i]);
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
            board[currRow][i] = new Piece(PieceColor.White, backRowPieceTypes[i]);
        }
    }

    @NotNull
    public ChessElement getPieceAt(int x, int y) {
        checkBounds(x, y);
        return board[x][y];
    }

    /**
     * Method that lets you change position of a piece on the board
     * NOTE! This will overwrite anything at the position (x1, y1)
     *
     * @param x0 initial x
     * @param y0 initial y
     * @param x1 new x
     * @param y1 new y
     */
    public void movePiece(int x0, int y0, int x1, int y1) {
        checkBounds(x0, y0);
        checkBounds(x1, y1);
        if (board[x0][y0] instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        board[x1][y1] = board[x0][y0];
        board[x0][y0] = EmptyCell.INSTANCE;
    }

    private void checkBounds(int x, int y) {
        if (x >= BOARD_SIZE || y >= BOARD_SIZE) {
            throw new IllegalArgumentException("Index out of bounds! x: " + x + ", y: " + y);
        }
    }
}
