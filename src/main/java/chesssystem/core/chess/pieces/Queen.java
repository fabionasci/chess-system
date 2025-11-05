package chesssystem.core.chess.pieces;

import chesssystem.core.board.Board;
import chesssystem.core.chess.ChessPiece;
import chesssystem.core.chess.enums.Color;

public class Queen extends ChessPiece {

    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "Q";
    }
}
