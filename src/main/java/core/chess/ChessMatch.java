package core.chess;

import core.board.Board;
import core.board.Piece;
import core.board.Position;
import core.chess.enums.Color;
import core.chess.exceptions.ChessException;
import core.chess.pieces.*;

import java.util.ArrayList;
import java.util.List;


public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;
    private Board board;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();


    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
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
        // return a matrix of ChessPiece from the board
        ChessPiece[][] pieces = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                pieces[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return pieces;
    }

    public ChessPiece performChessMove(ChessPosition sourcePos, ChessPosition targetPos) {
        Position source = sourcePos.toPosition();
        Position target = targetPos.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // special move promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK
                    && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            checkMate = false;
            nextTurn();
        }

        // special move en passant
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePos) {
        Position position = sourcePos.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }


    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece piece = (ChessPiece) board.removePiece(source);
        piece.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(piece, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // special move castling (king)
        if (piece instanceof King && Math.abs(target.getColumn() - source.getColumn()) == 2) {
            int srcRow = source.getRow();
            int srcCol = source.getColumn();
            // kingside
            if (target.getColumn() == srcCol + 2) {
                int rookSourceCol = srcCol + 3; // h1 when king on e1
                int rookTargetCol = srcCol + 1; // f1
                Position rookSource = new Position(srcRow, rookSourceCol);
                Position rookTarget = new Position(srcRow, rookTargetCol);
                ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
                board.placePiece(rook, rookTarget);
                rook.increaseMoveCount();
            } else { // queenside
                int rookSourceCol = srcCol - 4; // a1 when king on e1
                int rookTargetCol = srcCol - 1; // d1
                Position rookSource = new Position(srcRow, rookSourceCol);
                Position rookTarget = new Position(srcRow, rookTargetCol);
                ChessPiece rook = (ChessPiece) board.removePiece(rookSource);
                board.placePiece(rook, rookTarget);
                rook.increaseMoveCount();
            }
        }

        // special move en passant
        if (piece instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                piecesOnTheBoard.remove(capturedPiece);
                capturedPieces.add(capturedPiece);
            }
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece piece = (ChessPiece) board.removePiece(target);
        piece.decreaseMoveCount();
        board.placePiece(piece, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // special move castling (king)
        if (piece instanceof King && Math.abs(target.getColumn() - source.getColumn()) == 2) {
            int srcRow = source.getRow();
            int srcCol = source.getColumn();
            // kingside undo
            if (target.getColumn() == srcCol + 2) {
                int rookSourceCol = srcCol + 3;
                int rookTargetCol = srcCol + 1;
                Position rookSource = new Position(srcRow, rookSourceCol);
                Position rookTarget = new Position(srcRow, rookTargetCol);
                ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
                board.placePiece(rook, rookSource);
                rook.decreaseMoveCount();
            } else { // queenside undo
                int rookSourceCol = srcCol - 4;
                int rookTargetCol = srcCol - 1;
                Position rookSource = new Position(srcRow, rookSourceCol);
                Position rookTarget = new Position(srcRow, rookTargetCol);
                ChessPiece rook = (ChessPiece) board.removePiece(rookTarget);
                board.placePiece(rook, rookSource);
                rook.decreaseMoveCount();
            }
        }

        // special move en passant
        if (piece instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (piece.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (((ChessPiece) board.piece(position)).getColor() != currentPlayer) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        return piecesOnTheBoard.stream()
                .filter(p -> p instanceof King && ((ChessPiece) p).getColor() == color)
                .map(p -> (ChessPiece) p)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no " + color + " king on the board."));
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        return piecesOnTheBoard.stream()
                .filter(p -> p instanceof ChessPiece)
                .map(p -> (ChessPiece) p)
                .anyMatch(p -> p.getColor() == opponent(color) && p.possibleMove(kingPosition));
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }

        // defensive copy of the player's pieces to avoid ConcurrentModificationException
        List<ChessPiece> playerPieces = new ArrayList<>();
        for (Piece p : piecesOnTheBoard) {
            if (p instanceof ChessPiece) {
                ChessPiece cp = (ChessPiece) p;
                if (cp.getColor() == color) {
                    playerPieces.add(cp);
                }
            }
        }

        for (ChessPiece chessPiece : playerPieces) {
            boolean[][] mat = chessPiece.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (!mat[i][j]) continue;
                    Position source = chessPiece.getChessPosition().toPosition();
                    Position target = new Position(i, j);
                    Piece capturedPiece = makeMove(source, target);
                    boolean kingStillInCheck = testCheck(color);
                    undoMove(source, target, capturedPiece);
                    if (!kingStillInCheck) {
                        return false; // there is an escape -> not checkmate
                    }
                }
            }
        }

        return true; // no escape found -> checkmate
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        // white pieces (back rank -> chess row 1)
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));

        // white pawns (chess row 2)
        for (char col = 'a'; col <= 'h'; col++) {
            placeNewPiece(col, 2, new Pawn(board, Color.WHITE, this));
        }

        // black pieces (back rank -> chess row 8)
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));

        // black pawns (chess row 7)
        for (char col = 'a'; col <= 'h'; col++) {
            placeNewPiece(col, 7, new Pawn(board, Color.BLACK, this));
        }
    }

}
