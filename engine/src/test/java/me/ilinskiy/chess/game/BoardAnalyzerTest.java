package me.ilinskiy.chess.game;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.BoardImpl;
import me.ilinskiy.chess.impl.chessboard.EmptyCell;
import me.ilinskiy.chess.impl.chessboard.MoveAwareBoardImpl;
import me.ilinskiy.chess.impl.chessboard.Piece;
import me.ilinskiy.chess.impl.game.BoardAnalyzer;
import me.ilinskiy.chess.impl.game.Castling;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;

import static me.ilinskiy.chess.api.chessboard.Board.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class BoardAnalyzerTest {

    @Test
    @Timeout(value = 1)
    public void testGetAvailableMovesInitialPositionWhite() {
        MoveAwareBoard b = new MoveAwareBoardImpl();
        List<Move> actual = BoardAnalyzer.getAvailableMoves(PieceColor.White, b);
        List<Move> expected = new ArrayList<>();
        int row = BOARD_SIZE - 2;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Coordinates initPos = new Coordinates(i, row);
            expected.add(new RegularMove(initPos, new Coordinates(i, row + WHITE_DIRECTION)));
            expected.add(new RegularMove(initPos, new Coordinates(i, row + WHITE_DIRECTION * 2)));
        }
        Coordinates bishopPos = new Coordinates(1, BOARD_SIZE - 1);
        expected.add(new RegularMove(bishopPos, new Coordinates(0, BOARD_SIZE - 3)));
        expected.add(new RegularMove(bishopPos, new Coordinates(2, BOARD_SIZE - 3)));
        bishopPos = new Coordinates(BOARD_SIZE - 2, BOARD_SIZE - 1);
        expected.add(new RegularMove(bishopPos, new Coordinates(BOARD_SIZE - 3, BOARD_SIZE - 3)));
        expected.add(new RegularMove(bishopPos, new Coordinates(BOARD_SIZE - 1, BOARD_SIZE - 3)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(value = 1)
    public void testGetAvailableMovesInitialPositionBlack() {
        MoveAwareBoard b = new MoveAwareBoardImpl();
        List<Move> actual = BoardAnalyzer.getAvailableMoves(PieceColor.Black, b);
        List<Move> expected = new ArrayList<>();
        int row = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Coordinates initPos = new Coordinates(i, row);
            expected.add(new RegularMove(initPos, new Coordinates(i, row + BLACK_DIRECTION)));
            expected.add(new RegularMove(initPos, new Coordinates(i, row + BLACK_DIRECTION * 2)));
        }
        Coordinates bishopPos = new Coordinates(1, 0);
        expected.add(new RegularMove(bishopPos, new Coordinates(0, 2)));
        expected.add(new RegularMove(bishopPos, new Coordinates(2, 2)));
        bishopPos = new Coordinates(BOARD_SIZE - 2, 0);
        expected.add(new RegularMove(bishopPos, new Coordinates(BOARD_SIZE - 3, 2)));
        expected.add(new RegularMove(bishopPos, new Coordinates(BOARD_SIZE - 1, 2)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(1)
    public void testAvailableMovesAfterMove() {
        Board board = new BoardImpl();

        Coordinates newPos = new Coordinates(0, BOARD_SIZE - 3);
        assertInstanceOf(EmptyCell.class, board.get(newPos));
        Piece knight = Piece.createPiece(PieceColor.White, PieceType.Knight);
        board.set(newPos, knight);

        Coordinates initPos = new Coordinates(1, BOARD_SIZE - 1);
        assertEquals(knight, board.get(initPos));
        board.set(initPos, EmptyCell.INSTANCE);
        BoardAnalyzer.getAvailableMoves(PieceColor.White, new MoveAwareBoardImpl(board));
        BoardAnalyzer.getAvailableMoves(PieceColor.Black, new MoveAwareBoardImpl(board));
    }

    @Test
    public void testPawnNonEmptyCell() {
        Board b = new BoardImpl();
        Coordinates blackLoc = new Coordinates(3, 3);
        Coordinates whiteLoc = new Coordinates(3, 4);
        b.set(blackLoc, Piece.createPiece(PieceColor.Black, PieceType.Pawn));
        b.set(whiteLoc, Piece.createPiece(PieceColor.White, PieceType.Pawn));
        assertEquals(new HashSet<>(), BoardAnalyzer.getAvailableMovesForPiece(whiteLoc, new MoveAwareBoardImpl(b)));
        assertEquals(new HashSet<>(), BoardAnalyzer.getAvailableMovesForPiece(blackLoc, new MoveAwareBoardImpl(b)));
    }

    @Test
    public void testCastling() {
        Board b = new BoardImpl();
        List<Coordinates> blackKings = BoardAnalyzer.findPiecesByTypeAndColor(PieceType.King,
                                                                              PieceColor.Black,
                                                                              new MoveAwareBoardImpl(b));
        assertEquals(1, blackKings.size());
        Coordinates kingPos = blackKings.getFirst();

        assertEquals(BoardAnalyzer.getAvailableMovesForPiece(kingPos, new MoveAwareBoardImpl(b)), new HashSet<>());

        b.set(new Coordinates(BOARD_SIZE - 2, 0), EmptyCell.INSTANCE);
        b.set(new Coordinates(BOARD_SIZE - 3, 0), EmptyCell.INSTANCE);
        LinkedList<Move> expected = new LinkedList<>();
        Coordinates rookInitPos = new Coordinates(BOARD_SIZE - 1, 0);
        assertSame(b.get(rookInitPos).getType(), PieceType.Rook);
        Coordinates rookNewPos = new Coordinates(BOARD_SIZE - 3, 0);
        expected.add(new Castling(kingPos, new Coordinates(BOARD_SIZE - 2, 0), rookInitPos, rookNewPos));
        expected.add(new RegularMove(kingPos, new Coordinates(BOARD_SIZE - 3, 0)));
        LinkedList<Move> actual = new LinkedList<>(BoardAnalyzer.getAvailableMovesForPiece(kingPos,
                                                                                           new MoveAwareBoardImpl(b)));
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(actual, expected);
    }

    @Test
    public void testPromotion() {
        // Set up.
        Board b = new BoardImpl();
        Coordinates c = new Coordinates(0, 1);
        b.set(c, Piece.createPiece(PieceColor.White, PieceType.Pawn));
        b.set(new Coordinates(0, 0), EmptyCell.INSTANCE);

        // Execute.
        List<Move> moves = new ArrayList<>(BoardAnalyzer.getAvailableMovesForPiece(c, new MoveAwareBoardImpl(b)));

        // Verify.
        List<Move> expected = new ArrayList<>();
        for (PieceType pt : PawnPromotion.PROMOTION_ALLOWED_TO) {
            expected.add(new PawnPromotion(c, new Coordinates(1, 0), pt));
            expected.add(new PawnPromotion(c, new Coordinates(0, 0), pt));
        }
        Collections.sort(moves);
        Collections.sort(expected);
        assertEquals(expected, moves);
    }
}
