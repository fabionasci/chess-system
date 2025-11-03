package com.curso.java.completo.chesssystem.services;

import com.curso.java.completo.chesssystem.core.chess.ChessMatch;
import com.curso.java.completo.chesssystem.core.chess.ChessPiece;
import org.springframework.stereotype.Service;

@Service
public class ChessServiceImpl implements ChessService {

    private ChessMatch chessMatch;

    @Override
    public ChessPiece[][] getBoard() {
        chessMatch = new ChessMatch();
        return chessMatch.getPieces();
    }
}
