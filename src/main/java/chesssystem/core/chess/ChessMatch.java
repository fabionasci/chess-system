package chesssystem.core.chess;

import chesssystem.core.board.Board;
import chesssystem.core.board.Piece;
import chesssystem.core.board.Position;
import chesssystem.core.chess.enums.Color;
import chesssystem.core.chess.exceptions.ChessException;
import chesssystem.core.chess.pieces.*;


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
        // retornar matriz de ChessPiece a partir do board
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
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePos) {
        Position position = sourcePos.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }


    private Piece makeMove(Position source, Position target) {
        Piece piece = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(piece, target);
        return capturedPiece;
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

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
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
