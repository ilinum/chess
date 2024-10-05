package me.ilinskiy.chess.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.impl.chessboard.Piece;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static me.ilinskiy.chess.api.chessboard.PieceType.*;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 9/27/15.
 */
public final class JSwingChessPainter {
    @SuppressWarnings("WeakerAccess")
    public static final int INIT_HEIGHT_AND_WIDTH = 62 * Board.BOARD_SIZE; //approx 500
    private JFrame myFrame;

    @Nullable
    private Thread myUpdateTimeLeftThread = null;

    private final BoardPanel panel;
    private int timeoutSeconds;

    public JSwingChessPainter(@NotNull Board board) {
        Image icon = icons.get(Piece.createPiece(PieceColor.White, PieceType.Pawn));
        try {
            SwingUtilities.invokeAndWait(() -> {
                myFrame = new JFrame();
                myFrame.setTitle("Chess");
                myFrame.setIconImage(icon);
                myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                myFrame.setResizable(false);
                myFrame.setLayout(new BorderLayout());
                myFrame.setSize(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH);
                myFrame.getContentPane().setPreferredSize(new Dimension(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH));
                myFrame.pack();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        panel = new BoardPanel(board);
        myFrame.add(panel, BorderLayout.CENTER);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
        repaintBoard();
    }

    public void gameOver(@NotNull PieceColor winner) {
        this.repaint();
        JOptionPane.showMessageDialog(myFrame, getWinPhrase(winner));
    }

    public boolean askToPlayAgain() {
        return JOptionPane.showConfirmDialog(myFrame, "Would you like to play again?") == 0;
    }

    public void dispose() {
        myFrame.dispose();
    }

    public void setSelected(@Nullable Coordinates coordinates) {
        panel.setSelected(coordinates);
        repaint();
    }

    @Nullable
    public Coordinates getSelected() {
        return panel.getSelected();
    }

    public int askTimeOut() {
        int newTimeout = -1;
        while (newTimeout < 0) {
            String input = JOptionPane.showInputDialog(myFrame, "Enter timeout in seconds \n(0 for no timeout)",
                    "Timeout", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                newTimeout = 0;
            } else {
                try {
                    newTimeout = Integer.parseInt(input);
                } catch (NumberFormatException ignored) {

                }
            }
        }
        timeoutSeconds = newTimeout;
        return timeoutSeconds;
    }

    @Nullable
    private Thread createUpdateTimeLeftThread() {
        if (timeoutSeconds <= 0) {
            return null;
        }
        long deadlineMillis = System.currentTimeMillis() + timeoutSeconds * 1000;
        String oldName = myFrame.getTitle();
        return new Thread(() -> {
            try {
                int secondsLeft = timeoutSeconds - 1;
                while (secondsLeft > 0) {
                    secondsLeft = (int) (deadlineMillis - System.currentTimeMillis()) / 1000;
                    myFrame.setTitle("Chess. Time left: " + secondsLeft);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ignored) {

            } finally {
                myFrame.setTitle(oldName);
            }
        });
    }

    public void moveStarted() {
        assert myUpdateTimeLeftThread == null;
        myUpdateTimeLeftThread = createUpdateTimeLeftThread();
        if (myUpdateTimeLeftThread != null) {
            myUpdateTimeLeftThread.start();
        }
    }

    public void moveFinished() {
        if (myUpdateTimeLeftThread != null) {
            myUpdateTimeLeftThread.interrupt();
            myUpdateTimeLeftThread = null;
        }
    }

    public void repaint() {
        repaintBoard();
    }

    private void repaintBoard() {
        panel.repaint();
    }


    @NotNull
    private static String getWinPhrase(@NotNull PieceColor winner) {
        switch (winner) {
            case Black:
                return "Black won!";
            case White:
                return "White won!";
            case Empty:
                return "It's a draw!";
            default:
                throw new IllegalArgumentException("Something went wrong");
        }
    }

    BoardPanel getPanel() {
        return panel;
    }

    @NotNull
    static final HashMap<Piece, Image> icons = new HashMap<>();

    static {
        if (icons.isEmpty()) {
            InputStream[] images = new InputStream[]{
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-pawn.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-knight.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-bishop.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-rook.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-queen.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/white-king.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-pawn.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-knight.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-bishop.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-rook.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-queen.png"),
                    JSwingChessPainter.class.getResourceAsStream("/ChessPieces/black-king.png")
            };
            Piece[] pieces = new Piece[]{
                    Piece.createPiece(PieceColor.White, Pawn),
                    Piece.createPiece(PieceColor.White, Knight),
                    Piece.createPiece(PieceColor.White, Bishop),
                    Piece.createPiece(PieceColor.White, Rook),
                    Piece.createPiece(PieceColor.White, Queen),
                    Piece.createPiece(PieceColor.White, King),
                    Piece.createPiece(PieceColor.Black, Pawn),
                    Piece.createPiece(PieceColor.Black, Knight),
                    Piece.createPiece(PieceColor.Black, Bishop),
                    Piece.createPiece(PieceColor.Black, Rook),
                    Piece.createPiece(PieceColor.Black, Queen),
                    Piece.createPiece(PieceColor.Black, King)
            };
            assert pieces.length == images.length;
            for (int i = 0; i < pieces.length; i++) {
                InputStream img = images[i];
                try {
                    BufferedImage bf = ImageIO.read(img);
                    icons.put(pieces[i], new ImageIcon(bf).getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
