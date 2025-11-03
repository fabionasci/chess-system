package com.curso.java.completo.chesssystem.core.chess;

import com.curso.java.completo.chesssystem.core.board.Board;
import com.curso.java.completo.chesssystem.core.board.Position;
import com.curso.java.completo.chesssystem.core.chess.enums.Color;
import com.curso.java.completo.chesssystem.core.chess.pieces.*;

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
        initialSetup();
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

    private void initialSetup() {
        // white pieces (back rank -> row 7)
        board.placePiece(new Rook(board, Color.WHITE), new Position(7, 0));
        board.placePiece(new Knight(board, Color.WHITE), new Position(7, 1));
        board.placePiece(new Bishop(board, Color.WHITE), new Position(7, 2));
        board.placePiece(new Queen(board, Color.WHITE), new Position(7, 3));
        board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
        board.placePiece(new Bishop(board, Color.WHITE), new Position(7, 5));
        board.placePiece(new Knight(board, Color.WHITE), new Position(7, 6));
        board.placePiece(new Rook(board, Color.WHITE), new Position(7, 7));

        // white pawns (row 6)
        for (int col = 0; col < 8; col++) {
            board.placePiece(new Pawn(board, Color.WHITE), new Position(6, col));
        }

        // black pieces (back rank -> row 0)
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 0));
        board.placePiece(new Knight(board, Color.BLACK), new Position(0, 1));
        board.placePiece(new Bishop(board, Color.BLACK), new Position(0, 2));
        board.placePiece(new Queen(board, Color.BLACK), new Position(0, 3));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
        board.placePiece(new Bishop(board, Color.BLACK), new Position(0, 5));
        board.placePiece(new Knight(board, Color.BLACK), new Position(0, 6));
        board.placePiece(new Rook(board, Color.BLACK), new Position(0, 7));

        // black pawns (row 1)
        for (int col = 0; col < 8; col++) {
            board.placePiece(new Pawn(board, Color.BLACK), new Position(1, col));
        }
    }

}
