package com.curso.java.completo.chesssystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.curso.java.completo.chesssystem.core.chess.ChessPiece;
import com.curso.java.completo.chesssystem.services.ChessService;

@RestController
@RequestMapping("/chess")
public class ChessController {

    private final ChessService service;

    @Autowired
    public ChessController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/pieces")
    public ResponseEntity<String[][]> getBoard() {
        ChessPiece[][] board = service.getBoard();
        if (board == null || board.length == 0) {
            return ResponseEntity.ok(new String[0][0]);
        }

        int rows = board.length;
        int cols = board[0].length;
        String[][] view = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ChessPiece p = board[i][j];
                view[i][j] = (p == null) ? null : p.toString();
            }
        }

        return ResponseEntity.ok(view);
    }
}
