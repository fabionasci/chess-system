package com.curso.java.completo.chesssystem.core.chess.pieces;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.chess.ChessPiece;
import com.curso.java.completo.chesssystem.core.chess.enums.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "N";
    }
}
