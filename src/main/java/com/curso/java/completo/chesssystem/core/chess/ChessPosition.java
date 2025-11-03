package com.curso.java.completo.chesssystem.core.chess;

import com.curso.java.completo.chesssystem.core.board.Position;
import com.curso.java.completo.chesssystem.core.chess.exceptions.ChessException;

public class ChessPosition {

    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    /**
     * Converte a posição em notação de xadrez para a Position do board.
     * Mapeamento: chess row 1 -> board row 7, chess row 8 -> board row 0;
     * chess column 'a' -> board column 0.
     */
    protected Position toPosition() {
        int boardRow = 8 - row;
        int boardColumn = column - 'a';
        return new Position(boardRow, boardColumn);
    }

    /**
     * Cria uma ChessPosition a partir de uma Position do board.
     * Mapeamento inverso do método toPosition().
     */
    protected static ChessPosition fromPosition(Position position) {
        int chessRow = 8 - position.getRow();
        char chessColumn = (char) ('a' + position.getColumn());
        return new ChessPosition(chessColumn, chessRow);
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
