package me.ilinskiy.chess.game;

import me.ilinskiy.chess.chessBoard.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.ilinskiy.chess.chessBoard.ImmutableBoard.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/30/15.
 */
public class GameUtilTest {

    @Test(timeout = 1000)
    public void testGetAvailableMovesInitialPositionWhite() {
        Board b = new Board();
        List<Move> actual = GameUtil.getAvailableMoves(PieceColor.White, b.getInner());
        List<Move> expected = new ArrayList<>();
        int row = BOARD_SIZE - 2;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Coordinates initPos = new Coordinates(i, row);
            expected.add(new Move(initPos, new Coordinates(i, row + WHITE_DIRECTION)));
            expected.add(new Move(initPos, new Coordinates(i, row + WHITE_DIRECTION * 2)));
        }
        Coordinates bishopPos = new Coordinates(1, BOARD_SIZE - 1);
        expected.add(new Move(bishopPos, new Coordinates(0, BOARD_SIZE - 3)));
        expected.add(new Move(bishopPos, new Coordinates(2, BOARD_SIZE - 3)));
        bishopPos = new Coordinates(BOARD_SIZE - 2, BOARD_SIZE - 1);
        expected.add(new Move(bishopPos, new Coordinates(BOARD_SIZE - 3, BOARD_SIZE - 3)));
        expected.add(new Move(bishopPos, new Coordinates(BOARD_SIZE - 1, BOARD_SIZE - 3)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertThat(expected, is(actual));
    }

    @Test(timeout = 1000)
    public void testGetAvailableMovesInitialPositionBlack() {
        Board b = new Board();
        List<Move> actual = GameUtil.getAvailableMoves(PieceColor.Black, b.getInner());
        List<Move> expected = new ArrayList<>();
        int row = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            Coordinates initPos = new Coordinates(i, row);
            expected.add(new Move(initPos, new Coordinates(i, row + BLACK_DIRECTION)));
            expected.add(new Move(initPos, new Coordinates(i, row + BLACK_DIRECTION * 2)));
        }
        Coordinates bishopPos = new Coordinates(1, 0);
        expected.add(new Move(bishopPos, new Coordinates(0, 2)));
        expected.add(new Move(bishopPos, new Coordinates(2, 2)));
        bishopPos = new Coordinates(BOARD_SIZE - 2, 0);
        expected.add(new Move(bishopPos, new Coordinates(BOARD_SIZE - 3, 2)));
        expected.add(new Move(bishopPos, new Coordinates(BOARD_SIZE - 1, 2)));
        Collections.sort(expected);
        Collections.sort(actual);
        assertThat(expected, is(actual));
    }

    @Test(timeout = 1000)
    public void testAvailableMovesAfterMove() {
        Board b = new Board();

        Coordinates newPos = new Coordinates(0, BOARD_SIZE - 3);
        assertTrue(b.getPieceAt(newPos) instanceof EmptyCell);
        Piece knight = new Piece(PieceColor.White, PieceType.Knight);
        b.setPieceAt(newPos, knight);

        Coordinates initPos = new Coordinates(1, BOARD_SIZE - 1);
        Assert.assertEquals(knight, b.getPieceAt(initPos));
        b.setPieceAt(initPos, EmptyCell.INSTANCE);
        //GameUtil.getAvailableMoves(PieceColor.White, b.getInner());
        GameUtil.getAvailableMoves(PieceColor.Black, b.getInner());
    }
}
