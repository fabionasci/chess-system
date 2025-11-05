package com.curso.java.completo.chesssystem.core.chess.exceptions;

import com.curso.java.completo.chesssystem.core.board.exceptions.BoardException;

public class ChessException extends BoardException {

    private static final long serialVersionUID = 1L;

    public ChessException(String message) {
        super(message);
    }
}
