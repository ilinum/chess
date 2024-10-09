package me.ilinskiy.chess.uci;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class UCIClient {
    private final static int MOVE_TIMEOUT_MILLIS = 10;
    @NotNull
    private final FileWriter out;
    @NotNull
    private final BufferedReader in;

    public UCIClient(@NotNull String inPath, @NotNull String outPath) throws IOException {
        out = new FileWriter(outPath);
        in = new BufferedReader(new FileReader(inPath));
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
