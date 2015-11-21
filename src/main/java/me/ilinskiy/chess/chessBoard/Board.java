package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.annotations.Nullable;
import me.ilinskiy.chess.game.*;
import me.ilinskiy.chess.ui.ChessPainter;

import java.util.*;

import static me.ilinskiy.chess.chessBoard.PieceType.Pawn;

/**
 * A class that represents chess board. It keeps track of the elements, where they are on the board, whose turn it is
 * and which elements have moved.
 * Board can (and should) be only modified using BoardWrapper. The actual BoardWrapper instance used for making moves
 * is held by Game instance. So there is no way to modify this board from an outside package.
 * @see {@link ChessBoardUtil#makeMoveAndEvaluate}
 *
 * Author: Svyatoslav Ilinskiy
 * Date: 7/16/15
 */
public class Board implements Copyable {
    public static final int BOARD_SIZE = 8;
    public static final int WHITE_DIRECTION = -1;
    public static final int BLACK_DIRECTION = 1;
    private ChessElement[][] board;
    private Optional<Coordinates> selected;
    private PieceColor turn;
    private List<Coordinates> piecesMoved;
    private Optional<Move> lastMove;
    public final Optional<ChessPainter> myPainter;

    public Board(@Nullable ChessPainter painter) {
        selected = Optional.empty();
        putPiecesOnBoard();
        myPainter = Optional.ofNullable(painter);
        myPainter.ifPresent(p -> p.initialize(this));
        turn = PieceColor.White;
    }

    public Board() {
        this(null);
    }

    @SuppressWarnings("ConstantConditions")
    private void putPiecesOnBoard() {
        board = new ChessElement[BOARD_SIZE][BOARD_SIZE];
        int currRow = 0;
        assert ChessBoardUtil.backRowPieceTypes.length == BOARD_SIZE;
        lastMove = Optional.empty();
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
        lastMove = Optional.of(move);
        List<Coordinates> cellsToRepaint = new LinkedList<>();
        cellsToRepaint.addAll(GameUtil.getAvailableNewPositions(move.getInitialPosition(), this));
        cellsToRepaint.add(move.getInitialPosition());
        if (move instanceof Castling) {
            Castling c = (Castling) move;
            cellsToRepaint.add(c.getRookInitialPosition());
            cellsToRepaint.add(c.getRookNewPosition());
            makeActualMove(c.getKingInitialPosition(), c.getKingNewPosition());
            makeActualMove(c.getRookInitialPosition(), c.getRookNewPosition());
        } else if (move instanceof EnPasse) {
            Coordinates eaten = ((EnPasse) move).eatenPiece();
            assert getPieceAt(eaten).getType() == Pawn;
            setPieceAt(eaten, EmptyCell.INSTANCE);
            makeActualMove(move.getInitialPosition(), move.getNewPosition());
            cellsToRepaint.add(eaten);
            cellsToRepaint.add(move.getNewPosition());
        } else {
            makeActualMove(move.getInitialPosition(), move.getNewPosition());
        }
        turn = turn.inverse();
        selected = Optional.empty();
        cellsToRepaint.forEach(this::paintCell);
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

    public boolean pieceHasNotMovedSinceStartOfGame(@NotNull Coordinates pos) {
        return !piecesMoved.contains(pos);
    }

    @NotNull
    public Optional<Coordinates> getSelected() {
        if (selected == null) throw new RuntimeException("selected is null!");
        return selected; //can return selected without copying because both Optional and Coordinates are immutable
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

    /**
     * set the cell at c selected
     *
     * @return true if a piece is there. False if it's an empty cell and nothing is done
     */
    public boolean setSelected(@NotNull Coordinates newSelected) {
        checkBounds(newSelected);
        if (getPieceAt(newSelected).getColor() != whoseTurnIsIt()) {
            return false;
        }
        @Nullable Coordinates selectedCopy = selected.orElse(null);
        selected = Optional.of(newSelected);
        if (selectedCopy != null) {
            paintCell(selectedCopy);
            GameUtil.getAvailableNewPositions(selectedCopy, this).forEach(this::paintCell);
        }
        paintCell(newSelected);
        GameUtil.getAvailableNewPositions(newSelected, this).forEach(this::paintCell);
        return true;
    }

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
    public Board copy() {
        Board result = new Board();

        result.selected = this.selected;
        result.turn = this.turn;
        result.piecesMoved = GameUtil.copy(piecesMoved);
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(this.board[row], 0, result.board[row], 0, board.length);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            Board other = ((Board) o);
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (!this.board[i][j].equals(other.board[i][j])) {
                        return false;
                    }
                }
            }
            return turn == other.turn && selected.equals(other.selected);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board) * 31 + selected.hashCode() + turn.hashCode();
    }

    public void paintCell(@NotNull Coordinates pos) {
        myPainter.ifPresent(painter -> painter.paintCell(pos));
    }

    @NotNull
    public Optional<Move> getLastMove() {
        return lastMove;
    }
}
