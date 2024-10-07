package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.impl.game.GameUtil;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.ilinskiy.chess.api.chessboard.Board.BOARD_SIZE;
import static me.ilinskiy.chess.api.chessboard.PieceType.*;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testAllPiecesNotNull() {
        Board b = new BoardImpl();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Coordinates c = new CoordinatesImpl(i, j);
                assertNotNull(b.get(c), "Element at (" + c + ") is null!");
            }
        }
    }

    @Test
    public void testInitialSetup() {
        Board b = new BoardImpl();
        List<Coordinates> allElements = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Coordinates coordinates = new CoordinatesImpl(col, row);
                if (row == 1 || row == (BOARD_SIZE - 2)) {
                    String message = "A pawn should be here: " + coordinates;
                    allElements.add(coordinates);
                    assertSame(b.get(coordinates).getType(), Pawn, message);
                } else if (row > 1 && row < BOARD_SIZE - 2) {
                    assertInstanceOf(EmptyCell.class, b.get(coordinates), "Should be an empty cell: ");
                } else {
                    switch (col) {
                        case 0:
                        case (BOARD_SIZE - 1):
                            String message = "Rook should be here: " + coordinates;
                            assertSame(b.get(coordinates).getType(), Rook, message);
                            break;
                        case 1:
                        case (BOARD_SIZE - 2):
                            message = "Knight should be here: " + coordinates;
                            assertSame(b.get(coordinates).getType(), Knight, message);
                            break;
                        case 2:
                        case (BOARD_SIZE - 3):
                            message = "Bishop should be here: " + coordinates;
                            assertSame(b.get(coordinates).getType(), Bishop, message);
                            break;
                        case 3:
                            message = "Queen should be here: " + coordinates;
                            assertSame(b.get(coordinates).getType(), Queen, message);
                            break;
                        case 4:
                            message = "King should be here: " + coordinates;
                            assertSame(b.get(coordinates).getType(), King, message);
                            break;
                        default:
                            throw new IllegalStateException("Column is wrong: " + col);
                    }
                    allElements.add(coordinates);
                }

                //check color
                if (row < 2 || row > (BOARD_SIZE - 3)) {
                    String message = "Empty cell where it shouldn't be: " + coordinates;
                    assertInstanceOf(Piece.class, b.get(coordinates), message);
                    PieceColor expected;
                    if (row < 2) {
                        expected = PieceColor.Black;
                    } else {
                        expected = PieceColor.White;
                    }
                    PieceColor actualColor = b.get(coordinates).getColor();
                    message = "Wrong color " + actualColor + ", coordinates: " + coordinates;
                    assertSame(actualColor, expected, message);
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
        MoveAwareBoard b = new MoveAwareBoardImpl();
        CoordinatesImpl knightPos = new CoordinatesImpl(1, 0);
        CoordinatesImpl newPos = new CoordinatesImpl(3, 4);
        ChessElement element = b.getPiece(knightPos);
        assertSame(element.getType(), Knight, "Should be a knight " + knightPos);
        assertSame(element.getColor(), PieceColor.Black, "Should be black " + knightPos);
        b.makeMove(new RegularMove(knightPos, newPos));
        assertInstanceOf(EmptyCell.class, b.getPiece(knightPos), "Just moved piece from here! Should be empty!");
        assertSame(b.getPiece(newPos).getType(), Knight, "Just moved a knight here: " + newPos);
    }

    @Test
    public void testMoveEmptyCell() {
        MoveAwareBoard b = new MoveAwareBoardImpl();
        CoordinatesImpl initPos = new CoordinatesImpl(4, 4);
        CoordinatesImpl newPos = new CoordinatesImpl(0, 0);
        assertThrows(IllegalStateException.class, () -> {
            b.makeMove(new RegularMove(initPos, newPos));
        });
    }
}
