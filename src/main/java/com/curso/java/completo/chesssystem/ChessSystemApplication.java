package com.curso.java.completo.chesssystem;

import com.curso.java.completo.chesssystem.board.Board;
import com.curso.java.completo.chesssystem.board.Piece;
import com.curso.java.completo.chesssystem.board.Position;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChessSystemApplication.class, args);

        Position position = new Position(2, 2);
        System.out.println("Teste position: " + position);

        Board board = new Board(8, 8);
        System.out.println("Tabuleiro criado com " + board.getRows() + " linhas e " + board.getColumns() + " colunas.");

        Piece piece = new Piece(board);
        System.out.println("Peça criada para o tabuleiro: " + piece);
    }


}
