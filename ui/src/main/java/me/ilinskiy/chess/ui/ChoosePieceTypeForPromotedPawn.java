package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.PieceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
