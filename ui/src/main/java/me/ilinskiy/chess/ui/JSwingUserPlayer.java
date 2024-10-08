package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.*;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.game.Player;
import me.ilinskiy.chess.impl.game.BoardAnalyzer;
import me.ilinskiy.chess.impl.game.PawnPromotion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;


/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public final class JSwingUserPlayer implements Player {
    private final PieceColor myColor;
    private final JSwingChessPainter painter;

    public JSwingUserPlayer(PieceColor color, JSwingChessPainter painter) {
        myColor = color;
        this.painter = painter;
    }

    @NotNull
    @Override
    public Move getMove(@NotNull MoveAwareBoard board) {
        painter.moveStarted();
        BoardPanel panel = painter.getPanel();
        panel.setBoard(board);
        this.painter.repaint();

        CellSelectListener mouseListener = new CellSelectListener(panel);
        panel.addMouseListener(mouseListener);
        Move move = null;
        try {
            while (move == null) {
                Coordinates selected = painter.getSelected();
                Coordinates location = mouseListener.waitForSelection();
                if (location == null) {
                    // Try getting location again.
                    continue;
                }
                if (selected != null) {
                    Set<Move> availableMoves = BoardAnalyzer.getAvailableMovesForPiece(selected, board);
                    move = getMoveFromSelection(selected, location, availableMoves);
                    if (move == null && board.getPiece(location).getColor() == myColor) {
                        painter.setSelected(location);
                    }
                } else if (board.getPiece(location).getColor() == myColor) {
                    painter.setSelected(location);
                }
            }
            panel.setSelected(null);
            painter.moveFinished();
            return move;
        } finally {
            assert panel.getMouseListeners().length > 0;
            panel.removeMouseListener(mouseListener);
            assert panel.getMouseListeners().length == 0;
        }
    }

    @Nullable
    private static Move getMoveFromSelection(
            @NotNull Coordinates moveStart,
            @NotNull Coordinates moveEnd,
            @NotNull Set<Move> availableMovesForPiece) {
        List<Move> possibleMoves = new ArrayList<>();
        for (Move availableMove : availableMovesForPiece) {
            if (availableMove.getInitialPositions()[0].equals(moveStart) &&
                    availableMove.getNewPositions()[0].equals(moveEnd)) {
                possibleMoves.add(availableMove);
            }
        }
        if (possibleMoves.isEmpty()) {
            return null;
        }
        if (possibleMoves.size() > 1) {
            // Must be promotion!
            PieceType piece = new ChoosePieceTypeForPromotedPawn().getChosenPiece();
            for (Move possibleMove : possibleMoves) {
                if (possibleMove instanceof PawnPromotion pp && pp.promoteTo == piece) {
                    return possibleMove;
                }
            }
        }
        return possibleMoves.getFirst();
    }

    @NotNull
    @Override
    public PieceColor getColor() {
        return myColor;
    }

    private static Set<Move> getMovesStartingAt(List<Move> moves, Coordinates start) {
        Stream<Move> availableMoves = moves.stream().filter((move) -> Arrays.asList(move.getInitialPositions()).contains(
                start));
        return new HashSet<>(availableMoves.toList());
    }
}

class CellSelectListener implements MouseListener {
    @NotNull
    private final Lock mouseLock;
    @NotNull
    private final Condition selectionIsMade;
    @NotNull
    private final BoardPanel panel;
    @Nullable
    private Coordinates selection;

    CellSelectListener(@NotNull BoardPanel panel) {
        mouseLock = new ReentrantLock();
        selectionIsMade = mouseLock.newCondition();
        this.panel = panel;
    }

    @Nullable
    Coordinates waitForSelection() {
        mouseLock.lock();
        try {
            while (selection == null) {
                selectionIsMade.await();
            }
            return selection;
        } catch (InterruptedException e) {
            // The caller will try again.
            return null;
        } finally {
            mouseLock.unlock();
        }
    }

    @Override
    public void mouseClicked(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mouseReleased(@NotNull MouseEvent mouseEvent) {
        mouseLock.lock();
        try {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();
            int size = panel.getSize().width;
            int cellSize = size / Board.BOARD_SIZE;
            selection = new Coordinates((int) x / cellSize, (int) y / cellSize);
            selectionIsMade.signal();
        } finally {
            mouseLock.unlock();
        }
    }

    @Override
    public void mouseEntered(@NotNull MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(@NotNull MouseEvent mouseEvent) {
    }
}

class ChoosePieceTypeForPromotedPawn extends JPanel {
    @Nullable
    private PieceType selectedPiece;
    @NotNull
    private final Lock buttonLock;
    @NotNull
    private final Condition buttonPressed;

    private final JRadioButton[] buttons = new JRadioButton[]{
            new JRadioButton("Queen"),
            new JRadioButton("Rook"),
            new JRadioButton("Bishop"),
            new JRadioButton("Knight")
    };

    ChoosePieceTypeForPromotedPawn() {
        super(new GridLayout(0, 1));

        buttonLock = new ReentrantLock();
        buttonPressed = buttonLock.newCondition();
        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
        }

        add(new JLabel("Choose piece type for promoted pawn:"));
        for (JRadioButton button : buttons) {
            add(button);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(actionEvent -> {
            buttonLock.lock();
            buttonPressed.signal();
            for (JRadioButton button : buttons) {
                if (button.isSelected()) {
                    switch (button.getText()) {
                        case "Queen":
                            selectedPiece = PieceType.Queen;
                            break;
                        case "Rook":
                            selectedPiece = PieceType.Rook;
                            break;
                        case "Bishop":
                            selectedPiece = PieceType.Bishop;
                            break;
                        case "Knight":
                            selectedPiece = PieceType.Knight;
                            break;
                        default:
                            assert false;
                    }
                }
            }
            buttonLock.unlock();
        });
        add(submitButton);
    }

    @NotNull
    PieceType getChosenPiece() {
        buttonLock.lock();
        JFrame frame = new JFrame("Choose Piece Type");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
        try {
            while (selectedPiece == null) {
                try {
                    buttonPressed.await();
                } catch (InterruptedException ignored) {

                }
            }
            PieceType selected = selectedPiece;
            assert selected != null;
            selectedPiece = null;
            return selected;
        } finally {
            frame.dispose();
            buttonLock.unlock();
        }
    }

}
