package me.ilinskiy.chess.uci;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class UCIClient {
    private final static int MOVE_TIMEOUT_MILLIS = 10;
    @NotNull
    private final BufferedWriter out;
    @NotNull
    private final BufferedReader in;

    public UCIClient(int port) throws IOException {
        var socket = new Socket("localhost", port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        initialize();
    }

    private void initialize() throws IOException {
        out.write("uci\n");
        out.flush();
        while (!in.readLine().equals("uciok")) ;
        out.write("isready\n");
        out.flush();
        while (!in.readLine().equals("readyok")) ;
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
        out.write("position startpos%s\n".formatted(movesStr.toString()));
        out.flush();
        out.write("go movetime %d\n".formatted(MOVE_TIMEOUT_MILLIS));
        out.flush();
        String line = "";
        while (!line.startsWith("bestmove")) {
            line = in.readLine();
        }
        String[] splits = line.trim().split("\\s+");
        return splits[1];
    }
}
