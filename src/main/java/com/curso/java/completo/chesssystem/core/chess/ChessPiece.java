package com.curso.java.completo.chesssystem.core.chess;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.board.Piece;
import com.curso.java.completo.chesssystem.core.chess.enums.Color;

public class ChessPiece extends Piece {

    private Color color;
    private int moveCount;


    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }


    protected void increaseMoveCount() {
        moveCount++;
    }

    protected void decreaseMoveCount() {
        moveCount--;
    }

}

