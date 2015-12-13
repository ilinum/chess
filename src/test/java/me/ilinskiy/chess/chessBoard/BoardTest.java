package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.game.GameUtil;
import me.ilinskiy.chess.game.moves.RegularMove;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.ilinskiy.chess.chessBoard.Board.BOARD_SIZE;
import static me.ilinskiy.chess.chessBoard.PieceType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for BoardWrapper and Board classes
 *
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class BoardTest {

    @Test
    public void testAllPiecesNotNull() {
        Board b = new Board();
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
        Board b = new Board();
        List<Coordinates> allElements = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates coordinates = new Coordinates(col, row);
                if (row == 1 || row == (BOARD_SIZE - 2)) {
                    String message = "A pawn should be here: " + coordinates;
                    allElements.add(coordinates);
                    assertTrue(message, b.getPieceAt(coordinates).getType() == Pawn);
                } else if (row > 1 && row < BOARD_SIZE - 2) {
                    assertTrue("Should be an empty cell: ", b.getPieceAt(coordinates) instanceof EmptyCell);
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
                    allElements.add(coordinates);
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
        List<Coordinates> gameUtilAllPieces = GameUtil.getAllPieces(PieceColor.White, b);
        gameUtilAllPieces.addAll(GameUtil.getAllPieces(PieceColor.Black, b));
        Collections.sort(gameUtilAllPieces);
        Collections.sort(allElements);
        assertEquals(allElements, gameUtilAllPieces);
    }

    @Test
    public void testPieceMove() {
        BoardWrapper b = new BoardWrapper();
        Coordinates knightPos = new Coordinates(1, 0);
        Coordinates newPos = new Coordinates(3, 4);
        ChessElement element = b.getPieceAt(knightPos);
        assertTrue("Should be a knight " + knightPos, element.getType() == Knight);
        assertTrue("Should be black " + knightPos, element.getColor() == PieceColor.Black);
        b.movePiece(new RegularMove(knightPos, newPos));
        assertTrue("Just moved piece from here! Should be empty!", b.getPieceAt(knightPos) instanceof EmptyCell);
        assertTrue("Just moved a knight here: " + newPos, b.getPieceAt(newPos).getType() == Knight);
    }

    @Test(expected = IllegalStateException.class)
    public void testMoveEmptyCell() {
        BoardWrapper b = new BoardWrapper();
        Coordinates initPos = new Coordinates(4, 4);
        Coordinates newPos = new Coordinates(0, 0);
        b.movePiece(new RegularMove(initPos, newPos));
    }

    @Test
    public void testSelected() {
        Board b = new Board();
        assertTrue("selected should be empty!", !b.getSelected().isPresent());
        Coordinates coordinates = new Coordinates(7, 7);
        assertTrue(b.setSelected(coordinates));
        assertTrue(b.getSelected().get().equals(coordinates));
        Coordinates newCoordinates = new Coordinates(5, 5);
        assertTrue(!b.setSelected(newCoordinates));
        assertTrue(b.getSelected().get().equals(coordinates));
        b.movePiece(new RegularMove(coordinates, newCoordinates));
        assertTrue("selected should be empty!", !b.getSelected().isPresent());
        assertTrue(!b.setSelected(newCoordinates));
        assertTrue(!b.getSelected().isPresent());
        b.movePiece(new RegularMove(new Coordinates(0, 0), new Coordinates(4, 4)));
        assertTrue(b.setSelected(newCoordinates));
        assertTrue(b.getSelected().get().equals(newCoordinates));
    }
}
