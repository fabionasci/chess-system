package com.curso.java.completo.chesssystem;

import com.curso.java.completo.chesssystem.board.Position;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChessSystemApplication.class, args);

        Position position = new Position(2, 2);
        System.out.println(position);
    }


}
