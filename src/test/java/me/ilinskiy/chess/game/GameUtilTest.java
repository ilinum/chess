package me.ilinskiy.chess.game;

import me.ilinskiy.chess.api.chessboard.BoardWrapper;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.chessboard.BoardWrapperImpl;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.chessboard.EmptyCell;
import me.ilinskiy.chess.impl.chessboard.Piece;
import me.ilinskiy.chess.impl.game.Castling;
import me.ilinskiy.chess.impl.game.GameUtil;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;

import static me.ilinskiy.chess.impl.chessboard.BoardImpl.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class GameUtilTest {

    @Test
    @Timeout(value = 1)
    public void testGetAvailableMovesInitialPositionWhite() {
        BoardWrapper b = new BoardWrapperImpl();
        List<Move> actual = GameUtil.getAvailableMoves(PieceColor.White, b.getInner());
        List<Move> expected = new ArrayList<>();
        int row = BOARD_SIZE - 2;
        for (int i = 0; i < BOARD_SIZE; i++) {
            CoordinatesImpl initPos = new CoordinatesImpl(i, row);
            expected.add(new RegularMove(initPos, new CoordinatesImpl(i, row + WHITE_DIRECTION)));
            expected.add(new RegularMove(initPos, new CoordinatesImpl(i, row + WHITE_DIRECTION * 2)));
        }
        CoordinatesImpl bishopPos = new CoordinatesImpl(1, BOARD_SIZE - 1);
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(0, BOARD_SIZE - 3)));
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(2, BOARD_SIZE - 3)));
        bishopPos = new CoordinatesImpl(BOARD_SIZE - 2, BOARD_SIZE - 1);
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(BOARD_SIZE - 3, BOARD_SIZE - 3)));
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(BOARD_SIZE - 1, BOARD_SIZE - 3)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(value = 1)
    public void testGetAvailableMovesInitialPositionBlack() {
        BoardWrapper b = new BoardWrapperImpl();
        List<Move> actual = GameUtil.getAvailableMoves(PieceColor.Black, b.getInner());
        List<Move> expected = new ArrayList<>();
        int row = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            CoordinatesImpl initPos = new CoordinatesImpl(i, row);
            expected.add(new RegularMove(initPos, new CoordinatesImpl(i, row + BLACK_DIRECTION)));
            expected.add(new RegularMove(initPos, new CoordinatesImpl(i, row + BLACK_DIRECTION * 2)));
        }
        CoordinatesImpl bishopPos = new CoordinatesImpl(1, 0);
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(0, 2)));
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(2, 2)));
        bishopPos = new CoordinatesImpl(BOARD_SIZE - 2, 0);
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(BOARD_SIZE - 3, 2)));
        expected.add(new RegularMove(bishopPos, new CoordinatesImpl(BOARD_SIZE - 1, 2)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(1)
    public void testAvailableMovesAfterMove() {
        BoardWrapper b = new BoardWrapperImpl();

        Coordinates newPos = new CoordinatesImpl(0, BOARD_SIZE - 3);
        assertInstanceOf(EmptyCell.class, b.getPieceAt(newPos));
        Piece knight = Piece.createPiece(PieceColor.White, PieceType.Knight);
        b.setPieceAt(newPos, knight);

        Coordinates initPos = new CoordinatesImpl(1, BOARD_SIZE - 1);
        assertEquals(knight, b.getPieceAt(initPos));
        b.setPieceAt(initPos, EmptyCell.INSTANCE);
        GameUtil.getAvailableMoves(PieceColor.White, b.getInner());
        GameUtil.getAvailableMoves(PieceColor.Black, b.getInner());
    }

    @Test
    public void testPawnNonEmptyCell() {
        BoardWrapper b = new BoardWrapperImpl();
        CoordinatesImpl blackLoc = new CoordinatesImpl(3, 3);
        CoordinatesImpl whiteLoc = new CoordinatesImpl(3, 4);
        b.setPieceAt(blackLoc, Piece.createPiece(PieceColor.Black, PieceType.Pawn));
        b.setPieceAt(whiteLoc, Piece.createPiece(PieceColor.White, PieceType.Pawn));
        assertEquals(new HashSet<>(), GameUtil.getAvailableMovesForPiece(whiteLoc, b.getInner()));
        assertEquals(new HashSet<>(), GameUtil.getAvailableMovesForPiece(blackLoc, b.getInner()));
    }

    @Test
    public void testCastling() {
        BoardWrapper b = new BoardWrapperImpl();
        List<Coordinates> blackKings = GameUtil.findPiecesByTypeAndColor(PieceType.King, PieceColor.Black, b.getInner());
        assertEquals(1, blackKings.size());
        Coordinates kingPos = blackKings.get(0);

        assertEquals(GameUtil.getAvailableMovesForPiece(kingPos, b.getInner()), new HashSet<>());

        b.setPieceAt(new CoordinatesImpl(BOARD_SIZE - 2, 0), EmptyCell.INSTANCE);
        b.setPieceAt(new CoordinatesImpl(BOARD_SIZE - 3, 0), EmptyCell.INSTANCE);
        LinkedList<Move> expected = new LinkedList<>();
        CoordinatesImpl rookInitPos = new CoordinatesImpl(BOARD_SIZE - 1, 0);
        assertSame(b.getPieceAt(rookInitPos).getType(), PieceType.Rook);
        CoordinatesImpl rookNewPos = new CoordinatesImpl(BOARD_SIZE - 3, 0);
        expected.add(new Castling(kingPos, new CoordinatesImpl(BOARD_SIZE - 2, 0), rookInitPos, rookNewPos));
        expected.add(new RegularMove(kingPos, new CoordinatesImpl(BOARD_SIZE - 3, 0)));
        LinkedList<Move> actual = new LinkedList<>(GameUtil.getAvailableMovesForPiece(kingPos, b.getInner()));
        Collections.sort(actual);
        Collections.sort(expected);
        assertEquals(actual, expected);
    }
}
