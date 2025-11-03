package com.curso.java.completo.chesssystem;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.board.Piece;
import com.curso.java.completo.chesssystem.core.board.Position;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChessSystemApplication.class, args);
    }
}
