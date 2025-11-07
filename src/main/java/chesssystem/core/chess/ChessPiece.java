package chesssystem.core.chess;

import chesssystem.core.board.Board;
import chesssystem.core.board.Piece;
import chesssystem.core.board.Position;
import chesssystem.core.chess.enums.Color;

public abstract class ChessPiece extends Piece {

    private Color color;


    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }


    public Color getColor() {
        return color;
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}

