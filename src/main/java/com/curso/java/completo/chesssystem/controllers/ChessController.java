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
    public ResponseEntity<ChessPiece[][]> getBoard() {
        ChessPiece[][] board = service.getBoard();
        return ResponseEntity.ok(board);
    }
}
