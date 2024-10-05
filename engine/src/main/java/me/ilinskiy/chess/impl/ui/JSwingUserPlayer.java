package me.ilinskiy.chess.impl.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.game.Move;
import me.ilinskiy.chess.api.ui.Player;
import me.ilinskiy.chess.impl.chessboard.CoordinatesImpl;
import me.ilinskiy.chess.impl.game.GameUtil;
import me.ilinskiy.chess.impl.game.RegularMove;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static me.ilinskiy.chess.impl.game.GameUtil.println;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/20/15
 */
public final class JSwingUserPlayer implements Player {
    @Nullable
    private Move moveMade;
    @NotNull
    private final Lock mouseLock;
    @NotNull
    private final Condition moveIsMade;
    private final PieceColor myColor;
    private final JSwingChessPainter painter;

    public JSwingUserPlayer(PieceColor color, JSwingChessPainter painter) {
        moveMade = null;
        myColor = color;
        this.painter = painter;
        mouseLock = new ReentrantLock();
        moveIsMade = mouseLock.newCondition();
    }

    @NotNull
    @Override
    public Move getMove(@NotNull Board board) {
        this.painter.repaint();
        BoardPanel panel = painter.getPanel();
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(@NotNull MouseEvent mouseEvent) {
                mouseLock.lock();
                double x = mouseEvent.getX();
                double y = mouseEvent.getY();
                int size = panel.getSize().width;
                int cellSize = size / Board.BOARD_SIZE;
                Coordinates location = new CoordinatesImpl((int) x / cellSize, (int) y / cellSize);
                Coordinates selected = painter.getSelected();
                if (selected != null) {
                    Set<Move> availableMovesForPiece = GameUtil.getAvailableMovesForPiece(selected, board);
                    Move m = new RegularMove(selected, location);
                    Optional<Move> res = Optional.empty();
                    for (Move move : availableMovesForPiece) {
                        if (move.equals(m)) {
                            res = Optional.of(move);
                        }
                    }
                    if (res.isPresent()) {
                        moveMade = res.get();
                        moveIsMade.signal();
                    } else if (board.getPieceAt(location).getColor() == myColor) {
                        painter.setSelected(location);
                    }
                } else if (board.getPieceAt(location).getColor() == myColor){
                    painter.setSelected(location);
                }
                mouseLock.unlock();
            }

            @Override
            public void mouseEntered(@NotNull MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(@NotNull MouseEvent mouseEvent) {
            }
        };
        panel.addMouseListener(mouseListener);

        try {
            mouseLock.lock();
            while (moveMade == null) { //"You shall always wait in a while loop," - Alison Norman
                try {
                    moveIsMade.await();
                } catch (InterruptedException ignored) {

                }
            }
            Move move = moveMade;
            assert move != null;
            moveMade = null;
            panel.setSelected(null);
            return move;
        } finally {
            assert panel.getMouseListeners().length > 0;
            panel.removeMouseListener(mouseListener);
            assert panel.getMouseListeners().length == 0;
            mouseLock.unlock();
        }
    }

    @NotNull
    @Override
    public PieceColor getPlayerColor() {
        return myColor;
    }

    @Override
    @NotNull
    public PieceType getPieceTypeForPromotedPawn() {
        return new ChoosePieceTypeForPromotedPawn().getChosenPiece();
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
            while (selectedPiece != null) {
                try {
                    buttonPressed.await();
                } catch (InterruptedException ignored) {

                }
            }
            PieceType selected = selectedPiece;
            assert selected != null;
            println(selected.toString());
            selectedPiece = null;
            return selected;
        } finally {
            frame.dispose();
            buttonLock.unlock();
        }
    }

}
