package com.curso.java.completo.chesssystem.board;

public class Piece {

    protected Position position;
    private Board board;

    public Piece(Board board) {
        this.board = board;
        this.position = null;
    }

    protected Board getBoard() {
        return board;
    }

    public boolean[][] possibleMoves() {
        return null;
    }

    public boolean possibleMove(Position position) {
        boolean[][] moves = possibleMoves();
        return moves[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] moves = possibleMoves();
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                if (moves[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
