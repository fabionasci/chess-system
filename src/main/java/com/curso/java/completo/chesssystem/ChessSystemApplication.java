package com.curso.java.completo.chesssystem;

import com.curso.java.completo.chesssystem.core.chess.ChessMatch;

public class ChessSystemApplication {

    public static void main(String[] args) {

        ChessMatch match = new ChessMatch();
        UI.printBoard(match.getPieces());
    }
}
