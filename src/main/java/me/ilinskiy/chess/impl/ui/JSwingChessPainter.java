package me.ilinskiy.chess.impl.ui;

import me.ilinskiy.chess.api.chessboard.Board;
import me.ilinskiy.chess.api.chessboard.Coordinates;
import me.ilinskiy.chess.api.chessboard.PieceColor;
import me.ilinskiy.chess.api.chessboard.PieceType;
import me.ilinskiy.chess.api.ui.ChessPainter;
import me.ilinskiy.chess.impl.chessboard.Piece;
import me.ilinskiy.chess.impl.game.GameRunnerImpl;
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
public final class JSwingChessPainter implements ChessPainter {
    @SuppressWarnings("WeakerAccess")
    public static final int INIT_HEIGHT_AND_WIDTH = 62 * Board.BOARD_SIZE; //approx 500
    private JFrame myFrame;

    @Nullable
    private Thread myUpdateTimeLeftThread = null;

    private BoardPanel panel;

    public JSwingChessPainter() {
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
    }

    @Override
    public void initialize(@NotNull Board board) {
        if (panel == null) {
            //playing for the first time
            panel = new BoardPanel(board);
            myFrame.add(panel, BorderLayout.CENTER);
            myFrame.setLocationRelativeTo(null);
            myFrame.setVisible(true);
        } else {
            //playing again
            myFrame.remove(panel);
            panel = new BoardPanel(board);
            myFrame.add(panel, BorderLayout.CENTER);
            myFrame.revalidate();
            myFrame.repaint();
        }
        repaintBoard();
    }

    @Override
    public void gameOver(@NotNull PieceColor winner) {
        JOptionPane.showMessageDialog(myFrame, getWinPhrase(winner));
    }

    @Override
    public boolean askToPlayAgain() {
        return JOptionPane.showConfirmDialog(myFrame, "Would you like to play again?") == 0;
    }

    @Override
    public void dispose() {
        myFrame.dispose();
    }

    @Override
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
        return newTimeout;
    }

    @Nullable
    private Thread createUpdateTimeLeftThread() {
        if (GameRunnerImpl.TIMEOUT_IN_SECONDS <= 0) {
            return null;
        }
        long moveMustBeMadeByMillis = System.currentTimeMillis() + (GameRunnerImpl.TIMEOUT_IN_SECONDS + 1) * 1000;
        String oldName = myFrame.getTitle();
        return new Thread(() -> {
            try {
                int secondsLeft = GameRunnerImpl.TIMEOUT_IN_SECONDS - 1;
                while (secondsLeft > 0) {
                    secondsLeft = (int) (moveMustBeMadeByMillis - System.currentTimeMillis()) / 1000;
                    myFrame.setTitle("Chess. Time left: " + secondsLeft);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ignored) {

            } finally {
                myFrame.setTitle(oldName);
            }
        });
    }

    @Override
    public void moveStarted() {
        assert myUpdateTimeLeftThread == null;
        myUpdateTimeLeftThread = createUpdateTimeLeftThread();
        if (myUpdateTimeLeftThread != null) {
            myUpdateTimeLeftThread.start();
        }
    }

    @Override
    public void moveFinished() {
        if (myUpdateTimeLeftThread != null) {
            myUpdateTimeLeftThread.interrupt();
            myUpdateTimeLeftThread = null;
        }
    }

    @Override
    public void paintCell(@NotNull Coordinates pos) {
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
