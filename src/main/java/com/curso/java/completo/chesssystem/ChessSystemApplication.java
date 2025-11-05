package com.curso.java.completo.chesssystem;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.board.Piece;
import com.curso.java.completo.chesssystem.core.board.Position;

public class ChessSystemApplication {

    public static void main(String[] args) {

        Position position = new Position(2, 2);
        System.out.println("Teste position: " + position);

        Board board = new Board(8, 8);
        System.out.println("Tabuleiro criado com " + board.getRows() + " linhas e " + board.getColumns() + " colunas.");

        Piece piece = new Piece(board);
        System.out.println("Peça criada para o tabuleiro: " + piece);
    }
}
