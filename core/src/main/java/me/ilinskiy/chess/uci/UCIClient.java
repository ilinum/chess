package me.ilinskiy.chess.uci;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class UCIClient {
    private final static int MOVE_TIMEOUT_MILLIS = 10;
    @NotNull
    private final BufferedWriter out;
    @NotNull
    private final BufferedReader in;

    private static final Logger logger = Logger.getLogger(UCIClient.class.getName());

    public UCIClient(int port) throws IOException {
        var socket = new Socket("localhost", port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        initialize();
    }

    private void initialize() throws IOException {
        send("uci");
        while (!read().equals("uciok")) ;
        send("isready");
        while (!read().equals("readyok")) ;
    }

    public String getMove(List<String> moves) throws IOException {
        StringBuilder movesStr = new StringBuilder();
        if (!moves.isEmpty()) {
            movesStr.append(" moves");
        }
        for (String move : moves) {
            movesStr.append(" ");
            movesStr.append(move);
        }

        send("position startpos%s".formatted(movesStr.toString()));
        send("go movetime %d".formatted(MOVE_TIMEOUT_MILLIS));
        String line = read();
        while (!line.startsWith("bestmove")) {
            line = read();
        }
        String[] splits = line.trim().split("\\s+");
        return splits[1];
    }

    private void send(String message) throws IOException {
        logger.info("[uci][snd] %s".formatted(message));
        out.write(message + "\n");
        out.flush();
    }

    private String read() throws IOException {
        String message = in.readLine();
        logger.info("[uci][rcv] %s".formatted(message));
        return message;
    }
}
