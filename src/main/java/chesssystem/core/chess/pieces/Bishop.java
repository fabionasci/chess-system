package chesssystem.core.chess.pieces;

import chesssystem.core.board.Board;
import chesssystem.core.chess.ChessPiece;
import chesssystem.core.chess.enums.Color;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
    }
}
