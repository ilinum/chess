package me.ilinskiy.chessBoard;

import me.ilinskiy.game.Move;
import org.junit.Test;

import static me.ilinskiy.chessBoard.ImmutableBoard.BOARD_SIZE;
import static me.ilinskiy.chessBoard.PieceType.*;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class BoardTest {

    @Test
    public void testAllPiecesNotNull() {
        ImmutableBoard b = new ImmutableBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Coordinates c = new Coordinates(i, j);
                //noinspection ConstantConditions
                assertTrue("Element at (" + c + ") is null!", b.getPieceAt(c) != null);
            }
        }
    }

    @Test
    public void testInitialSetup() {
        ImmutableBoard b = new ImmutableBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates coordinates = new Coordinates(row, col);
                if (row == 1 || row == (BOARD_SIZE - 2)) {
                    String message = "A pawn should be here: " + coordinates;
                    assertTrue(message, b.getPieceAt(coordinates).getType() == Pawn);
                } else if (row > 1 && row < BOARD_SIZE - 2) {
                    assertTrue("Should be an empty cell", b.getPieceAt(coordinates) instanceof EmptyCell);
                } else {
                    switch (col) {
                        case 0:
                        case (BOARD_SIZE - 1):
                            String message = "Rook should be here: " + coordinates;
                            assertTrue(message, b.getPieceAt(coordinates).getType() == Rook);
                            break;
                        case 1:
                        case (BOARD_SIZE - 2):
                            message = "Knight should be here: " + coordinates;
                            assertTrue(message, b.getPieceAt(coordinates).getType() == Knight);
                            break;
                        case 2:
                        case (BOARD_SIZE - 3):
                            message = "Bishop should be here: " + coordinates;
                            assertTrue(message, b.getPieceAt(coordinates).getType() == Bishop);
                            break;
                        case 3:
                            message = "Queen should be here: " + coordinates;
                            assertTrue(message, b.getPieceAt(coordinates).getType() == Queen);
                            break;
                        case 4:
                            message = "King should be here: " + coordinates;
                            assertTrue(message, b.getPieceAt(coordinates).getType() == King);
                            break;
                        default:
                            throw new IllegalStateException("Column is wrong: " + col);
                    }
                }

                //check color
                if (row < 2 || row > (BOARD_SIZE - 3)) {
                    String message = "Empty cell where it shouldn't be: " + coordinates;
                    assertTrue(message, b.getPieceAt(coordinates) instanceof Piece);
                    PieceColor expected;
                    if (row < 2) {
                        expected = PieceColor.Black;
                    } else {
                        expected = PieceColor.White;
                    }
                    PieceColor actualColor = b.getPieceAt(coordinates).getColor();
                    message = "Wrong color " + actualColor + ", coordinates: " + coordinates;
                    assertTrue(message, actualColor == expected);
                }
            }
        }
    }

    @Test
    public void testPieceMove() {
        Board b = new Board();
        Coordinates knightPos = new Coordinates(0, 1);
        Coordinates newPos = new Coordinates(3, 4);
        ChessElement element = b.getPieceAt(knightPos);
        assertTrue("Should not be empty cell " + knightPos, element instanceof Piece);
        b.movePiece(new Move(knightPos, newPos));
        assertTrue("Just moved piece from here! Should be empty!", b.getPieceAt(knightPos) instanceof EmptyCell);
        assertTrue("Just moved a knight here: (3, 4)", b.getPieceAt(newPos).getType() == Knight);
    }

    @Test(expected = IllegalStateException.class)
    public void testMoveEmptyCell() {
        Board b = new Board();
        Coordinates initPos = new Coordinates(4, 4);
        Coordinates newPos = new Coordinates(0, 0);
        b.movePiece(new Move(initPos, newPos));
    }
}