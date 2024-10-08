package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static me.ilinskiy.chess.api.chessboard.PieceType.*;

public final class BoardImpl implements Board {
    private static final PieceType[] backRowPieceTypes = new PieceType[]{
            Rook,
            Knight,
            Bishop,
            Queen,
            King,
            Bishop,
            Knight,
            Rook,
    };

    private ChessElement[][] board;

    public BoardImpl() {
        putPiecesOnBoard();
    }

    private void putPiecesOnBoard() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        int currRow = 0;
        assert backRowPieceTypes.length == BOARD_SIZE;
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
            board[currRow][i] = Piece.createPiece(topPieceColor, backRowPieceTypes[i]);
        }
        currRow++;
        Arrays.fill(board[currRow], Piece.createPiece(topPieceColor, Pawn));

        currRow++;
        for (; currRow < board.length - 2; currRow++) {
            Arrays.fill(board[currRow], EmptyCell.INSTANCE);
        }

        Arrays.fill(board[currRow], Piece.createPiece(bottomPieceColor, Pawn));
        currRow++;
        assert (currRow == (BOARD_SIZE - 1)) : "Wrong current row: " + currRow;
        for (int i = 0; i < board[currRow].length; i++) {
            board[currRow][i] = Piece.createPiece(bottomPieceColor, backRowPieceTypes[i]);
        }
    }

    @Override
    @NotNull
    public ChessElement get(@NotNull Coordinates c) {
        checkBounds(c);
        return board[c.getY()][c.getX()];
    }

    @Override
    public void set(@NotNull Coordinates pos, @NotNull ChessElement element) {
        checkBounds(pos);
        board[pos.getY()][pos.getX()] = element;
    }

    private void checkBounds(@NotNull Coordinates c) {
        if (c.isOutOfBounds()) {
            throw new IllegalArgumentException("Index out of bounds: " + c);
        }
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

    @NotNull
    @Override
    public BoardImpl copy() {
        BoardImpl result = new BoardImpl();
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(this.board[row], 0, result.board[row], 0, board.length);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardImpl other) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!this.board[i][j].equals(other.board[i][j])) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
