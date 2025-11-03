package com.curso.java.completo.chesssystem.core.chess;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.chess.enums.Color;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    private Board board;


    public ChessMatch() {
        board = new Board(8, 8);
    }


    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece[][] getPieces() {
        // retornar matriz de ChessPiece a partir do board
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                pieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return pieces;
    }

//    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
//        // TODO: calcular movimentos possíveis para a peça na sourcePosition
//        throw new UnsupportedOperationException("possibleMoves not implemented");
//    }
//
//    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
//        // TODO: executar movimento de xadrez (inclui validações, captura, promoção, en passant, verificação, etc.)
//        throw new UnsupportedOperationException("performChessMove not implemented");
//    }

    public ChessPiece replacePromotedPiece(String type) {
        // TODO: substituir peça promovida por outra do tipo informado (e.g. "Q", "R", "B", "N")
        throw new UnsupportedOperationException("replacePromotedPiece not implemented");
    }

    // Métodos auxiliares privados podem ser adicionados conforme necessário
}
