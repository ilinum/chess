package me.ilinskiy.chess.impl.chessboard;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.ChessElement;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.impl.game.Castling;
import me.ilinskiy.chess.impl.game.EnPasse;
import me.ilinskiy.chess.impl.game.GameUtil;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.ilinskiy.chess.api.chessboard.PieceType.Pawn;

/**
 * A class that represents chess board. It keeps track of the elements, where they are on the board, whose turn it is
 * and which elements have moved.
 * BoardImpl can (and should) be only modified using BoardWrapperImpl. The actual BoardWrapperImpl instance used for making moves
 * is held by GameImpl instance. So there is no way to modify this board from an outside package.
 *
 * @see {@link ChessBoardUtil#makeMoveAndEvaluate}
 * <p>
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public final class BoardImpl implements Board {
    private ChessElement[][] board;
    private PieceColor turn;
    private List<Coordinates> piecesMoved;
    @Nullable
    private Move lastMove;

    BoardImpl() {
        putPiecesOnBoard();
        turn = PieceColor.White;
    }

    @SuppressWarnings("ConstantConditions")
    private void putPiecesOnBoard() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        int currRow = 0;
        assert ChessBoardUtil.backRowPieceTypes.length == BOARD_SIZE;
        lastMove = null;
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
            board[currRow][i] = Piece.createPiece(topPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
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
            board[currRow][i] = Piece.createPiece(bottomPieceColor, ChessBoardUtil.backRowPieceTypes[i]);
        }
        piecesMoved = new ArrayList<>();
    }

    @Override
    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        checkBounds(c);
        return board[c.getY()][c.getX()];
    }

    /**
     * Method that lets you change position of a piece on the board
     * NOTE! This will overwrite anything at the position move.getNewPosition!!!
     *
     * @param move move that was made
     */
    void movePiece(@NotNull Move move) {
        lastMove = move;
        if (move instanceof Castling) {
            Castling c = (Castling) move;
            makeActualMove(c.getKingInitialPosition(), c.getKingNewPosition());
            makeActualMove(c.getRookInitialPosition(), c.getRookNewPosition());
        } else if (move instanceof EnPasse) {
            EnPasse enPasse = (EnPasse) move;
            Coordinates eaten = enPasse.eatenPiece();
            assert getPieceAt(eaten).getType() == Pawn;
            setPieceAt(eaten, EmptyCell.INSTANCE);
            makeActualMove(enPasse.initialPosition, enPasse.newPosition);
        } else {
            RegularMove rm = ((RegularMove) move);
            makeActualMove(rm.initialPosition, rm.newPosition);
        }
        turn = turn.inverse();
    }

    private void makeActualMove(@NotNull Coordinates initialPosition, @NotNull Coordinates newPosition) {
        checkBounds(initialPosition);
        checkBounds(newPosition);
        piecesMoved.remove(initialPosition);
        piecesMoved.add(newPosition);
        ChessElement initialPositionPiece = board[initialPosition.getY()][initialPosition.getX()];
        if (initialPositionPiece instanceof EmptyCell) {
            throw new IllegalStateException("Cannot move an empty cell!");
        }
        board[newPosition.getY()][newPosition.getX()] = initialPositionPiece;
        board[initialPosition.getY()][initialPosition.getX()] = EmptyCell.INSTANCE;
    }

    @Override
    public boolean pieceHasNotMovedSinceStartOfGame(@NotNull Coordinates pos) {
        return !piecesMoved.contains(pos);
    }

    void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        checkBounds(pos);
        board[pos.getY()][pos.getX()] = element;
    }

    private void checkBounds(@NotNull Coordinates c) {
        if (c.isOutOfBounds()) {
            throw new IllegalArgumentException("Index out of bounds: " + c);
        }
    }

    @Override
    @NotNull
    public PieceColor whoseTurnIsIt() {
        return turn;
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

    /**
     * @return a deep copy of this immutable board
     */
    @NotNull
    @Override
    public BoardImpl copy() {
        BoardImpl result = new BoardImpl();

        result.turn = this.turn;
        result.piecesMoved = GameUtil.copy(piecesMoved);
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(this.board[row], 0, result.board[row], 0, board.length);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardImpl) {
            BoardImpl other = ((BoardImpl) o);
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!this.board[i][j].equals(other.board[i][j])) {
                        return false;
                    }
                }
            }
            return turn == other.turn;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board) * 31 + turn.hashCode();
    }

    @Override
    @NotNull
    public Optional<Move> getLastMove() {
        return Optional.ofNullable(lastMove);
    }
}
