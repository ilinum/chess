package me.ilinskiy.ChessBoard;

import org.junit.Test;

import static me.ilinskiy.ChessBoard.Board.BOARD_SIZE;
import static me.ilinskiy.ChessBoard.ChessBoardUtil.coordinatesToString;
import static me.ilinskiy.ChessBoard.PieceType.*;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class BoardTest {

    @Test
    public void testAllPiecesNotNull() {
        Board b = new Board();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                assertTrue("Element at (" + i + ", " + j + ") is null!", b.getPieceAt(i, j) != null);
            }
        }
    }

    @Test
    public void testInitialSetup() {
        Board b = new Board();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (row == 1 || row == (BOARD_SIZE - 2)) {
                    String message = "A pawn should be here: " + coordinatesToString(row, col);
                    assertTrue(message, b.getPieceAt(row, col).getType() == Pawn);
                } else if (row > 1 && row < BOARD_SIZE - 2) {
                    assertTrue("Should be an empty cell", b.getPieceAt(row, col) instanceof EmptyCell);
                } else {
                    switch (col) {
                        case 0:
                        case (BOARD_SIZE - 1):
                            String message = "Rook should be here: " + coordinatesToString(row, col);
                            assertTrue(message, b.getPieceAt(row, col).getType() == Rook);
                            break;
                        case 1:
                        case (BOARD_SIZE - 2):
                            message = "Knight should be here: " + coordinatesToString(row, col);
                            assertTrue(message, b.getPieceAt(row, col).getType() == Knight);
                            break;
                        case 2:
                        case (BOARD_SIZE - 3):
                            message = "Bishop should be here: " + coordinatesToString(row, col);
                            assertTrue(message, b.getPieceAt(row, col).getType() == Bishop);
                            break;
                        case 3:
                            message = "Queen should be here: " + coordinatesToString(row, col);
                            assertTrue(message, b.getPieceAt(row, col).getType() == Queen);
                            break;
                        case 4:
                            message = "King should be here: " + coordinatesToString(row, col);
                            assertTrue(message, b.getPieceAt(row, col).getType() == King);
                            break;
                        default:
                            throw new IllegalStateException("Column is wrong: " + col);
                    }
                }

                //check color
                if (row < 2 || row > (BOARD_SIZE - 3)) {
                    String message = "Empty cell where it shouldn't be: " + coordinatesToString(row, col);
                    assertTrue(message, b.getPieceAt(row, col) instanceof Piece);
                    PieceColor expected;
                    if (row < 2) {
                        expected = PieceColor.Black;
                    } else {
                        expected = PieceColor.White;
                    }
                    PieceColor actualColor = b.getPieceAt(row, col).getColor();
                    message = "Wrong color " + actualColor + ", coordinates: " + coordinatesToString(row, col);
                    assertTrue(message, actualColor == expected);
                }
            }
        }
    }

    @Test
    public void testPieceMove() {
        Board b = new Board();
        ChessElement element = b.getPieceAt(0, 1);
        assertTrue("Should not be empty cell " + coordinatesToString(0, 1), element instanceof Piece);
        b.movePiece(0, 1, 3, 4);
        assertTrue("Just moved piece from here! Should be empty!", b.getPieceAt(0, 1) instanceof EmptyCell);
        assertTrue("Just moved a knight here: (3, 4)", b.getPieceAt(3, 4).getType() == Knight);
    }

    @Test(expected = IllegalStateException.class)
    public void testMoveEmptyCell() {
        Board b = new Board();
        b.movePiece(4, 4, 0, 0);
    }
}